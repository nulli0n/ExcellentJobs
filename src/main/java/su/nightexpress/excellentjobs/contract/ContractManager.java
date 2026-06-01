package su.nightexpress.excellentjobs.contract;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsConstants;
import su.nightexpress.excellentjobs.JobsFiles;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.event.JobJoinEvent;
import su.nightexpress.excellentjobs.api.event.JobLeaveEvent;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.contract.bar.ContractBarElement;
import su.nightexpress.excellentjobs.contract.condition.ConditionRegistry;
import su.nightexpress.excellentjobs.contract.condition.UnlockCondition;
import su.nightexpress.excellentjobs.contract.condition.impl.ActiveContractsCondition;
import su.nightexpress.excellentjobs.contract.condition.impl.LevelCondition;
import su.nightexpress.excellentjobs.contract.core.ContractLang;
import su.nightexpress.excellentjobs.contract.core.ContractPerms;
import su.nightexpress.excellentjobs.contract.data.ContractData;
import su.nightexpress.excellentjobs.contract.data.ContractDataManager;
import su.nightexpress.excellentjobs.contract.definition.ContractBehavior;
import su.nightexpress.excellentjobs.contract.definition.ContractDefinition;
import su.nightexpress.excellentjobs.contract.definition.ContractPromotion;
import su.nightexpress.excellentjobs.contract.definition.ContractUnlocking;
import su.nightexpress.excellentjobs.contract.definition.Timeframe;
import su.nightexpress.excellentjobs.contract.dialog.ContractDialogKeys;
import su.nightexpress.excellentjobs.contract.dialog.impl.ContractActivationDialog;
import su.nightexpress.excellentjobs.contract.dialog.impl.ContractCancelDialog;
import su.nightexpress.excellentjobs.contract.dialog.impl.ContractPromotionDialog;
import su.nightexpress.excellentjobs.contract.listener.ContractListener;
import su.nightexpress.excellentjobs.contract.menu.ContractOptionsMenu;
import su.nightexpress.excellentjobs.contract.menu.ContractSelectionMenu;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.contract.model.ContractJob;
import su.nightexpress.excellentjobs.contract.model.ObjectiveModifier;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobContracts;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.format.adaptive.AdaptiveFormatter;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class ContractManager extends AbstractManager<JobsPlugin> {

    private final UserDataManager userDataManager;
    private final JobManager      jobManager;

    private final ContractSettings    settings;
    private final ContractDataManager dataManager;
    private final ConditionRegistry   conditionRegistry;

    private final AdaptiveFormatter<ContractJob> contractFormatter;

    private final Map<String, Contract> contractsMap;

    private ContractSelectionMenu selectionMenu;
    private ContractOptionsMenu   optionsMenu;

    public ContractManager(JobsPlugin plugin,
                           Database dataHandler,
                           UserDataManager userDataManager,
                           JobManager jobManager) {
        super(plugin);
        this.userDataManager = userDataManager;
        this.jobManager = jobManager;

        this.settings = new ContractSettings();
        this.dataManager = new ContractDataManager(plugin, dataHandler, this.settings);
        this.conditionRegistry = new ConditionRegistry();

        this.contractFormatter = new AdaptiveFormatter<>();

        this.contractsMap = new HashMap<>();

        this.selectionMenu = new ContractSelectionMenu(plugin, this, contractFormatter);
        this.optionsMenu = new ContractOptionsMenu(plugin, this);
    }

    @Override
    protected void onLoad() {
        this.plugin.injectLang(ContractLang.class);
        this.plugin.registerPermissions(ContractPerms.ROOT);

        ConditionRegistry.bind(this.conditionRegistry);
        this.registerCodecs();

        this.loadSettings();
        this.loadConditions();
        this.loadFormatter();
        this.loadJobFormatter();
        this.loadContracts();
        this.loadDialogs();
        this.loadUI();

        this.dataManager.setup();
        this.userDataManager.addLoginListener(this.dataManager);

        this.addListener(new ContractListener(this.plugin, this));
    }

    @Override
    protected void onShutdown() {
        this.userDataManager.removeLoginListener(this.dataManager);
        this.dataManager.shutdown();

        this.contractsMap.clear();
        this.conditionRegistry.clear();

        ConditionRegistry.unbind();
    }

    // TODO Level Reward Contract Requirement

    private void registerCodecs() {
        ConfigCodecs.register(Timeframe.class, Timeframe.CODEC);

        ConfigCodecs.register(ObjectiveModifier.class, ObjectiveModifier.CODEC);
        ConfigCodecs.register(ContractBehavior.class, ContractBehavior.CODEC);
        ConfigCodecs.register(ContractDefinition.class, ContractDefinition.CODEC);
        ConfigCodecs.register(ContractPromotion.class, ContractPromotion.CODEC);
        ConfigCodecs.register(ContractUnlocking.class, ContractUnlocking.CODEC);
    }

    private void loadSettings() {
        Path file = this.plugin.dataPath().resolve(ContractFiles.CFG_SETTINGS);
        FileConfig.load(file).edit(this.settings::load);
    }

    private void loadFormatter() {
        this.contractFormatter.registerCondition("available", (source, player) -> {
            Contract contract = source.contract();
            Job job = source.job();

            return !this.isContractActive(player, contract, job) && this.canUseContract(player, contract, job);
        });

        this.contractFormatter.registerCondition("can_not_use", (source, player) -> {
            return !this.canUseContract(player, source.contract(), source.job());
        });

        this.contractFormatter.registerCondition("leave_cooldown", (source, player) -> {
            ContractData data = this.getActiveContractData(player, source.job());
            return data != null && !data.isLeaveCooldownExpired();
        });

        this.contractFormatter.registerCondition("active", (source, player) -> {
            return this.isContractActive(player, source.contract(), source.job());
        });

        this.contractFormatter.registerCondition("has_cost", (source, player) -> {
            return source.contract().getUnlocking().isCostEnabled();
        });

        this.contractFormatter.registerCondition("locked", (source, player) -> {
            return !this.isContractUnlocked(player, source.contract(), source.job());
        });

        this.contractFormatter.registerCondition("unaffordable", (source, player) -> {
            return !this.isContractAffordable(player, source.contract());
        });

        this.contractFormatter.registerCondition("not_met_conditions", (source, player) -> {
            return !this.isMetContractConditions(player, source.job(), source.contract());
        });

        this.contractFormatter.registerCondition("completed", (source, player) -> {
            return this.isContractCompleted(player, source.contract(), source.job());
        });

        this.contractFormatter.registerVariable("leave_cooldown", (source, player) -> {
            ContractData data = this.getActiveContractData(player, source.job());
            if (data == null) return CoreLang.OTHER_NONE.text();

            if (data.isLeaveCooldownExpired()) {
                long millis = source.contract().getBehavior().getLeaveCooldown();
                return TimeFormats.formatAmount(millis, TimeFormatType.LITERAL);
            }

            long leaveCooldown = data.getLeaveCooldown();
            return TimeFormats.formatDuration(leaveCooldown, TimeFormatType.LITERAL);
        });
    }

    private void loadJobFormatter() {
        AdaptiveFormatter<Job> formatter = this.jobManager.getJobFormatter();

        formatter.registerVariable("contract_name", (job, player) -> {
            Contract contract = this.getCurrenctContract(player, job);
            return contract == null ? Lang.OTHER_N_A.text() : contract.getDefinition().getName();
        });

        formatter.registerVariable("contract_points", (job, player) -> {
            ContractData data = this.getActiveContractData(player, job);
            return data == null ? Lang.OTHER_N_A.text() : NumberUtil.format(data.getContractPoints());
        });
    }

    private void loadConditions() {
        this.registerCondition("job_level", LevelCondition.class, new LevelCondition.Codec(this.jobManager));

        this.registerCondition(
            ActiveContractsCondition.NAME,
            ActiveContractsCondition.class,
            new ActiveContractsCondition.Codec(this.jobManager, this)
        );
    }

    public <T extends UnlockCondition> void registerCondition(String name, Class<T> type, ConfigCodec<T> codec) {
        this.conditionRegistry.register(name, type);
        ConfigCodecs.register(type, codec);
    }

    private void loadContracts() {
        Path dir = this.plugin.dataPath().resolve(ContractFiles.DIR_CONTRACTS);
        if (!Files.exists(dir)) {
            this.writeDefaultContracts();
        }

        FileUtil.findYamlFiles(dir).forEach(file -> {
            try {
                Contract contract = this.loadContract(file);

                this.contractsMap.put(contract.getId(), contract);
            }
            catch (Exception exception) {
                this.plugin.error("Failed to read contract from file: " + file);
                this.plugin.error("Reason: " + exception.getMessage());

                exception.printStackTrace();
            }
        });

        this.plugin.info("Loaded %s contracts.".formatted(this.contractsMap.size()));
    }

    private Contract loadContract(Path file) throws ModelLoadException {
        String name = FileUtil.getNameWithoutExtension(file);
        String id = Strings.varStyle(name).orElse(null);
        if (id == null) throw new ModelLoadException("Invalid file name format. Could not be parsed into ID string.");

        if (id.length() > JobsConstants.MAX_MODEL_ID_LENGTH) {
            throw new ModelLoadException("File name exceeds max length of " + JobsConstants.MAX_MODEL_ID_LENGTH +
                " characters.");
        }

        try {
            FileConfig config = FileConfig.load(file);

            ContractDefinition definition = config.getOrSet("Definition", ContractDefinition.class, ContractDefinition
                .defaults());

            ContractBehavior behavior = config.read("Behavior", ContractBehavior.class, ContractBehavior.defaults());

            ContractUnlocking unlocking = config.read("Unlocking", ContractUnlocking.class, ContractUnlocking
                .defaults());

            ContractPromotion completion = config.read("Promotion", ContractPromotion.class, ContractPromotion
                .defaults());

            config.saveChanges();

            return new Contract(id, definition, behavior, unlocking, completion);
        }
        catch (CodecReadException exception) {
            throw new ModelLoadException("Failed to parse contract configuration.", exception);
        }
    }

    private void writeDefaultContracts() {
        new ContractDefaults(this.jobManager, this).createDefaultContracts().forEach(this::writeContract);
    }

    private void writeContract(Contract contract) {
        Path file = this.plugin.dataPath().resolve(ContractFiles.DIR_CONTRACTS).resolve(FileConfig.withExtension(
            contract.getId()));

        FileConfig.load(file).edit(config -> {
            config.set("Definition", contract.getDefinition());
            config.set("Unlocking", contract.getUnlocking());
            config.set("Behavior", contract.getBehavior());
            config.set("Promotion", contract.getPromotion());
        });
    }

    private void loadDialogs() {
        this.plugin.dialogRegistry().register(
            ContractDialogKeys.CONTRACT_ACTIVATION,
            new ContractActivationDialog(this)
        );

        this.plugin.dialogRegistry().register(
            ContractDialogKeys.CONTRACT_CANCELLATION,
            new ContractCancelDialog(this)
        );

        this.plugin.dialogRegistry().register(
            ContractDialogKeys.CONTRACT_PROMOTION,
            new ContractPromotionDialog(this)
        );
    }

    private void loadUI() {
        Path menuDir = this.plugin.dataPath().resolve(JobsFiles.DIR_UI);

        this.initMenu(this.selectionMenu, menuDir.resolve(ContractFiles.UI_SELECTION));
        this.initMenu(this.optionsMenu, menuDir.resolve(ContractFiles.UI_OPTIONS));
    }

    public ContractSettings getSettings() {
        return this.settings;
    }

    public void handleJobMenuDefineLayout(JobMenuDefineLayoutEvent event) {
        AbstractObjectMenu<Job> jobMenu = event.getJobMenu();

        jobMenu.addDefaultButton("contracts", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.MAP)
                    .setDisplayName(TagWrappers.GRADIENT.with("#eb3349", "#f45c43")
                        .and(TagWrappers.BOLD).wrap("Contracts"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Earn more from your job"),
                        TagWrappers.GRAY.wrap("with special contracts!"),
                        "",
                        TagWrappers.COLOR.with(ContractConstants.ACCENT)
                            .wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(jobMenu.getObject(context).placeholders())
                ))
                .action(context -> this.openContractOptionsOrSelection(context.getPlayer(), jobMenu.getObject(context)))
                .build()
            )
            .slots(11)
            .build()
        );
    }

    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.dataManager.getCache().getAllByOwner(player.getUniqueId()).forEach(contractData -> {
            if (contractData.isPromotionStarted()) {
                this.validatePromotion(player, contractData);
            }
            if (!contractData.isPromotionCompleted()) {
                this.checkPromotionCompletion(player, contractData);
            }
        });
    }

    public void handlePlayerQuit(PlayerQuitEvent event) {
        // Nothing currently
    }

    public void handleJobJoin(JobJoinEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        JobContracts contracts = job.getContracts();
        if (contracts == null) return;

        if (contracts.isRequired() && !this.hasActiveContract(player, job)) {
            event.setCancelled(true);
            this.openContractSelection(player, job);
        }
    }

    public void handleJobQuit(JobLeaveEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();

        ContractData data = this.getActiveContractData(player, job);
        if (data != null && !data.isLeaveCooldownExpired()) {
            event.setCancelled(true);
            ContractLang.JOB_LEAVE_ACTIVE_CONTRACT.message().sendWith(player, builder -> builder
                .with(job.placeholders())
            );
            return;
        }

        this.stopContract(player, job);
    }

    public void handleGrindRewardPre(GrindRewardEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        JobData jobData = event.getJobData();

        Contract contract = this.getCurrenctContract(player, job);
        if (contract == null) return;

        GrindReward reward = event.getReward();
        ContractBehavior behavior = contract.getBehavior();

        LocalDateTime localDate = TimeUtil.getCurrentDateTime();
        Timeframe timeframe = behavior.getTimeframe(localDate.getDayOfWeek());
        boolean isGoodTime = timeframe == null || timeframe.isInRange(localDate.toLocalTime());

        for (GrindObjectiveProperty property : GrindObjectiveProperty.values()) {
            ObjectiveModifier modifier = behavior.getObjectiveModifier(property);
            if (modifier == null) continue;

            double baseMultiplier = modifier.getBaseMultiplier();
            double levelBonus = modifier.getLevelBonus();
            double timeframePenalty = modifier.getTimeframePenalty();

            double mainMult = baseMultiplier + levelBonus * jobData.getLevel();

            reward.modifyIfPresent(property, value -> {
                value *= mainMult;
                if (!isGoodTime) value *= timeframePenalty;

                return value;
            });
        }
    }

    public void handleGrindRewardPost(GrindRewardProceedEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        if (!this.hasActiveContract(player, job)) return;

        ContractData data = this.getActiveContractData(player, job);
        if (data == null || !data.isPromotionStarted()) return;
        if (data.isPromotionDeadlineExceeded() || data.isPromotionCompleted()) return;

        double contractPoints = event.getReward().get(GrindObjectiveProperty.CONTRACT_POINTS);
        if (contractPoints == 0D) return;

        this.addContractPoints(player, job, contractPoints);

        if (this.settings.isBarElementEnabled()) {
            String format = this.settings.getBarElementFormat();
            ContractBarElement element = new ContractBarElement(format, contractPoints);
            event.getBarElements().add(element);
        }
    }

    public boolean showJobSettingsOrSelection(Player player, Job job) {
        return this.jobManager.showJobSettingsOrSelection(player, job);
    }

    public boolean openContractOptionsOrSelection(Player player, Job job) {
        if (this.hasActiveContract(player, job)) {
            return this.openContractOptions(player, job);
        }
        else {
            return this.openContractSelection(player, job);
        }
    }

    public boolean showContractSelection(Player player, Job job) {
        return this.selectionMenu.show(player, job);
    }

    public boolean openContractSelection(Player player, Job job) {
        if (!this.hasValidContracts(job)) {
            ContractLang.CONTRACT_SELECTION_JOB_NOTHING.message().sendWith(player, builder -> builder
                .with(job.placeholders())
            );
            return false;
        }

        return this.showContractSelection(player, job);
    }

    public boolean showContractOptions(Player player, Contract contract, Job job) {
        return this.optionsMenu.show(player, new ContractJob(contract, job));
    }

    public boolean openContractOptions(Player player, Job job) {
        Contract contract = this.getCurrenctContract(player, job);
        if (contract == null) {
            ContractLang.ERROR_NO_ACTIVE_CONTRACT.message().send(player);
            return false;
        }

        return this.showContractOptions(player, contract, job);
    }

    public boolean hasUnlockedContracts(Player player, Job job) {
        return !this.getUnlockedContracts(player, job).isEmpty();
    }

    public boolean hasValidContracts(Job job) {
        return !this.getContractsForJob(job).isEmpty();
    }

    public boolean hasActiveContract(Player player, Job job) {
        return this.getCurrenctContract(player, job) != null;
    }

    public boolean hasContractLeaveCooldown(Player player, Job job) {
        ContractData data = this.getActiveContractData(player, job);
        return data != null && !data.isLeaveCooldownExpired();
    }

    public Set<Contract> getContractsForJob(Job job) {
        JobContracts contracts = job.getContracts();
        if (contracts == null) return Set.of();

        if (contracts.getAllowedIds().contains(Placeholders.WILDCARD)) return this.getContracts();

        return contracts.getAllowedIds().stream()
            .map(this::getContract)
            .collect(Collectors.toSet());
    }

    public List<Contract> getUnlockedContracts(Player player, Job job) {
        Contract current = this.getCurrenctContract(player, job);

        return this.getContractsForJob(job).stream()
            .filter(contract -> contract != current && this.isContractUnlocked(player, contract, job))
            .sorted(Comparator.comparingInt(Contract::getPriority))
            .toList();
    }

    public @Nullable Contract findBestContract(Player player, Job job) {
        return this.contractsMap.values().stream()
            .filter(contract -> this.canUseContract(player, contract, job))
            .max(Comparator.comparingInt(Contract::getPriority))
            .orElse(null);
    }

    public @Nullable Contract getCurrenctContract(Player player, Job job) {
        ContractData data = this.getContractData(player, job);
        if (data == null || !data.isEffectivelyActive()) return null;

        return this.getContract(data.getContractId());
    }

    public @Nullable ContractData getContractData(Player player, Job job) {
        return this.dataManager.getData(player.getUniqueId(), job.getId());
    }

    public @Nullable ContractData getActiveContractData(Player player, Job job) {
        ContractData data = this.getContractData(player, job);
        if (data == null || !data.isActive() || data.isRemoved()) return null;

        return data;
    }

    public boolean activateContract(Player player,
                                    Job job,
                                    Contract contract,
                                    boolean force) {
        if (!force && !this.canUseContract(player, contract, job)) {
            ContractLang.CONTRACT_ACTIVATION_NOT_AVAILABLE.message().sendWith(player, builder -> builder
                .with(job.placeholders())
                .with(contract.placeholders())
            );
            return false;
        }

        ContractData data = this.getContractData(player, job);

        if (!force && data != null && !data.isLeaveCooldownExpired()) {
            long leaveCooldown = data.getLeaveCooldown();
            ContractLang.CONTRACT_ACTIVATION_COOLDOWN.message().sendWith(player, builder -> builder
                .with(CommonPlaceholders.GENERIC_TIME, () -> {
                    return TimeFormats.formatDuration(leaveCooldown, TimeFormatType.LITERAL);
                })
                .with(job.placeholders())
            );
            return false;
        }

        JobData jobData = this.jobManager.getActiveData(player, job);
        boolean isHired = jobData != null;

        if (!isHired && !this.jobManager.joinJob(player, job, true, false)) {
            return false;
        }

        if (data == null) {
            data = ContractData.create(player, job, contract);
            data.setLeaveCooldown(contract.getBehavior().generateLeaveCooldownTimestamp());
            data.markDirty();
            this.dataManager.cachePermanently(data);
        }
        else {
            data.resetPoints();
            data.resetPromotion();
            data.setActive(true);
            data.setContractId(contract.getId());
            data.setLeaveCooldown(contract.getBehavior().generateLeaveCooldownTimestamp());
            data.markDirty();
        }

        ContractLang.CONTRACT_ACTIVATION_SUCCESS.message().sendWith(player, builder -> builder
            .with(contract.placeholders())
            .with(job.placeholders())
        );
        return true;
    }

    public boolean cancelContract(Player player, Job job) {
        ContractData data = this.getActiveContractData(player, job);
        if (data == null) {
            ContractLang.ERROR_NO_ACTIVE_CONTRACT.message().send(player);
            return false;
        }

        long leaveCooldown = data.getLeaveCooldown();
        if (!data.isLeaveCooldownExpired()) {
            ContractLang.CONTRACT_CANCEL_COOLDOWN.message().sendWith(player, builder -> builder
                .with(CommonPlaceholders.GENERIC_TIME, () -> {
                    return TimeFormats.formatDuration(leaveCooldown, TimeFormatType.LITERAL);
                })
                .with(job.placeholders())
            );
            return false;
        }

        this.stopContract(player, job);

        ContractLang.CONTRACT_CANCEL_SUCCESS.message().sendWith(player, builder -> builder
            .with(job.placeholders())
        );

        return true;
    }

    private void stopContract(Player player, Job job) {
        ContractData data = this.getContractData(player, job);
        if (data == null || !data.isEffectivelyActive()) return;

        Contract contract = this.getContract(data.getContractId());

        // Force apply promotion cooldown, so players can't bypass it by re-applying the contract
        // when they are out of time to complete it.
        if (data.isPromotionStarted() && contract != null) {
            data.setPromotionCooldown(contract.getPromotion().generateCooldownTimestamp());
        }
        data.resetPromotionButCooldown();
        data.resetPoints();
        data.setActive(false);
        data.markDirty();
    }

    public boolean startPromotion(Player player, Contract contract, Job job) {
        ContractData data = this.getContractData(player, job);
        if (data == null || !data.isEffectivelyActive()) {
            ContractLang.ERROR_NO_ACTIVE_CONTRACT.message().send(player);
            return false;
        }

        ContractPromotion promotion = contract.getPromotion();
        if (!promotion.isEnabled()) {
            ContractLang.CONTRACT_PROMOTION_START_DISABLED.message().sendWith(player, builder -> builder
                .with(contract.placeholders())
                .with(job.placeholders())
            );
            return false;
        }

        if (data.isPromotionCompleted() || data.isCompleted(contract.getId())) {
            ContractLang.CONTRACT_PROMOTION_START_COMPLETED.message().sendWith(player, builder -> builder
                .with(contract.placeholders())
                .with(job.placeholders())
            );
            return false;
        }

        if (data.isPromotionStarted()) {
            ContractLang.CONTRACT_PROMOTION_START_ALREADY.message().sendWith(player, builder -> builder
                .with(contract.placeholders())
                .with(job.placeholders())
            );
            return false;
        }

        if (data.isPromotionOnCooldown() && !data.isPromotionCooldownExpired()) {
            long timestamp = data.getPromotionCooldown();
            ContractLang.CONTRACT_PROMOTION_START_COOLDOWN.message().sendWith(player, builder -> builder
                .with(contract.placeholders())
                .with(job.placeholders())
                .with(CommonPlaceholders.GENERIC_TIME, () -> TimeFormats.formatDuration(timestamp,
                    TimeFormatType.LITERAL)
                )
            );
            return false;
        }

        data.resetPromotion();
        data.resetPoints();
        data.setPromotionStarted(true);
        data.setPromotionDeadline(promotion.generateDurationTimestamp());
        data.markDirty();

        ContractLang.CONTRACT_PROMOTION_START_SUCCESS.message().sendWith(player, builder -> builder
            .with(contract.placeholders())
            .with(job.placeholders())
        );

        ContractLang.CONTRACT_PROMOTION_START_DETAILS.message().sendWith(player, builder -> builder
            .with(contract.placeholders())
            .with(job.placeholders())
            .with(CommonPlaceholders.GENERIC_TIME, () -> TimeFormats.formatDuration(data.getPromotionDeadline(),
                TimeFormatType.LITERAL))
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(promotion.getPointsGoal()))
        );

        return true;
    }

    public void addContractPoints(Player player, Job job, double amount) {
        ContractData data = this.getContractData(player, job);
        if (data == null || !data.isEffectivelyActive() || data.isPromotionCompleted()) return;

        this.validatePromotion(player, data);

        if (!data.isPromotionStarted()) return;

        data.addPoints(amount);
        data.markDirty();

        this.checkPromotionCompletion(player, data);
    }

    private void validatePromotion(Player player, ContractData data) {
        if (!data.isPromotionStarted() || data.isPromotionCompleted()) return;

        Contract contract = this.getContract(data.getContractId());
        Job job = this.jobManager.getJobById(data.getJobId());

        if (contract == null || !contract.getPromotion().isEnabled() || data.isPromotionDeadlineExceeded()) {
            data.resetPromotion();
            data.resetPoints();
            data.markDirty();

            if (contract != null && job != null) {
                ContractLang.CONTRACT_PROMOTION_RESULT_FAIL.message().sendWith(player, builder -> builder
                    .with(contract.placeholders())
                    .with(job.placeholders())
                );
            }
        }
    }

    private void checkPromotionCompletion(Player player, ContractData data) {
        Contract contract = this.getContract(data.getContractId());
        if (contract == null) return;

        Job job = this.jobManager.getJobById(data.getJobId());
        if (job == null) return;

        JobData jobData = this.jobManager.getActiveData(player, job);
        if (jobData == null) return;

        ContractPromotion promotion = contract.getPromotion();
        if (!promotion.isEnabled()) return;

        int pointsGoal = promotion.getPointsGoal();
        if (data.getContractPoints() < pointsGoal) return;

        data.setPromotionCompleted(true);
        data.setPromotionStarted(false);
        data.setPromotionDeadline(0L);
        data.addCompletedId(contract.getId());
        data.markDirty();

        ContractLang.CONTRACT_PROMOTION_RESULT_COMPLETED.message().sendWith(player, builder -> builder
            .with(contract.placeholders())
            .with(job.placeholders())
        );

        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(contract.placeholders())
            .with(job.placeholders())
            .with(jobData.placeholders())
            .build();

        promotion.getRewardCommands().forEach(command -> {
            Players.dispatchCommand(player, placeholderContext.apply(command));
        });

        promotion.getRewardMessages().forEach(message -> {
            Players.sendMessage(player, placeholderContext.apply(message));
        });
    }

    public boolean canUseContract(Player player, Contract contract, Job job) {
        if (this.isContractCompleted(player, contract, job)) return false;
        if (!this.isContractUnlocked(player, contract, job)) return false;
        if (!this.isContractAffordable(player, contract)) return false;

        return this.isMetContractConditions(player, job, contract);
    }

    public boolean isContractActive(Player player, Contract contract, Job job) {
        return this.getCurrenctContract(player, job) == contract;
    }

    public boolean isContractCompleted(Player player, Contract contract, Job job) {
        ContractData data = this.getContractData(player, job);
        return data != null && data.isCompleted(contract.getId());
    }

    public boolean isContractUnlocked(Player player, Contract contract, Job job) {
        ContractUnlocking unlocking = contract.getUnlocking();

        boolean needPermission = unlocking.isPermissionRequired();
        return !needPermission || player.hasPermission(contract.getPermission(job));
    }

    public boolean isContractAffordable(Player player, Contract contract) {
        ContractUnlocking unlocking = contract.getUnlocking();
        if (!unlocking.isCostEnabled()) return true;

        for (var entry : unlocking.getCost().entrySet()) {
            String id = entry.getKey();
            double amount = entry.getValue();

            if (!EconomyBridge.api().canAfford(player, id, amount)) return false;
        }

        return true;
    }

    public boolean isMetContractConditions(Player player, Job job, Contract contract) {
        ContractUnlocking unlocking = contract.getUnlocking();
        if (!unlocking.isConditionsEnabled()) return true;
        if (unlocking.getConditions().isEmpty()) return true;

        return unlocking.getConditions().stream().allMatch(condition -> condition.check(player, contract, job));
    }


    public Map<String, Contract> getContractsMap() {
        return this.contractsMap;
    }

    public Set<Contract> getContracts() {
        return Set.copyOf(this.contractsMap.values());
    }

    public @Nullable Contract getContract(String id) {
        return this.contractsMap.get(id);
    }
}
