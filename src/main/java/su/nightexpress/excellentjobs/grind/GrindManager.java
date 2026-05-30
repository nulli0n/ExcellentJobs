package su.nightexpress.excellentjobs.grind;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsFiles;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.grind.GrindAdapter;
import su.nightexpress.excellentjobs.api.grind.GrindAdapterFamily;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.adapter.DefaultGrindAdapters;
import su.nightexpress.excellentjobs.grind.adapter.impl.CustomCropsAdapter;
import su.nightexpress.excellentjobs.grind.adapter.impl.EvenMoreFishAdapter;
import su.nightexpress.excellentjobs.grind.adapter.impl.MythicMobAdapter;
import su.nightexpress.excellentjobs.grind.bar.GrindBarElement;
import su.nightexpress.excellentjobs.grind.codec.GrindObjectiveCodec;
import su.nightexpress.excellentjobs.grind.core.GrindLang;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.excellentjobs.grind.listener.GrindListenerProvider;
import su.nightexpress.excellentjobs.grind.listener.GrindWorkstationListener;
import su.nightexpress.excellentjobs.grind.listener.impl.BlockLootGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.BreedingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.BrewingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.BrewingTypeGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.BuildingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.CookingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.CraftingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.EatingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.EnchantingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.FertilizingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.FishingItemsGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.FishingMobsGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.ForgingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.GrindstoneGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.KillingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.MilkingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.BreakBlockGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.MobLootGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.ShearingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.SmithingGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.StripBlockGrindListener;
import su.nightexpress.excellentjobs.grind.listener.impl.TamingGrindListener;
import su.nightexpress.excellentjobs.grind.modifier.impl.BrewingGrindModifier;
import su.nightexpress.excellentjobs.grind.modifier.impl.CookingGrindModifier;
import su.nightexpress.excellentjobs.grind.modifier.impl.EnchantingGrindModifier;
import su.nightexpress.excellentjobs.grind.modifier.impl.MobKillGrindModifier;
import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.excellentjobs.grind.protection.GrindProtectionManager;
import su.nightexpress.excellentjobs.grind.table.SourceReward;
import su.nightexpress.excellentjobs.grind.table.SourceTable;
import su.nightexpress.excellentjobs.grind.type.GenericGrindType;
import su.nightexpress.excellentjobs.grind.visual.GrindBarManager;
import su.nightexpress.excellentjobs.grind.workstation.WorkstationMode;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobGrinding;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Randomizer;

@NullMarked
public class GrindManager extends AbstractManager<JobsPlugin> {

    private final JobManager    jobManager;
    private final GrindRegistry registry;
    private final GrindSettings settings;

    private final Path settingsFile;

    private final NamespacedKey stationOwnerKey;
    private final NamespacedKey stationModeKey;

    private @Nullable GrindProtectionManager protectionManager;
    private @Nullable GrindBarManager        barManager;


    public GrindManager(JobsPlugin plugin, JobManager jobManager) {
        super(plugin);
        this.jobManager = jobManager;
        this.registry = new GrindRegistry();
        this.settings = new GrindSettings();

        this.settingsFile = plugin.dataPath().resolve(GrindFiles.FILE_SETTINGS);

        this.stationOwnerKey = new NamespacedKey(plugin, "workstation.owner_id");
        this.stationModeKey = new NamespacedKey(plugin, "workstation.craft_mode");
    }

    @Override
    protected void onLoad() {
        this.plugin.injectLang(GrindLang.class);

        this.loadSettings();
        this.loadProtection();

        this.registerCodecs();
        this.registerAdapters();
        this.registerTypes();

        this.loadGrindBar();
        this.loadObjectives();
        this.updateJobObjectives();

        this.addListener(new GrindWorkstationListener(this.plugin, this));
    }

    @Override
    protected void onShutdown() {
        if (this.protectionManager != null) this.protectionManager.shutdown();
        if (this.barManager != null) this.barManager.shutdown();
    }

    public GrindRegistry getRegistry() {
        return this.registry;
    }

    public @Nullable GrindProtection getProtection() {
        return this.protectionManager;
    }

    private void loadSettings() {
        FileConfig.load(this.settingsFile).edit(this.settings::load);
    }

    private void registerAdapters() {
        this.registerAdapter(DefaultGrindAdapters.VANILLA_MOB, DefaultAdapterFamilies.ENTITY);
        this.registerAdapter(DefaultGrindAdapters.VANILLA_BLOCK, DefaultAdapterFamilies.BLOCK);
        this.registerAdapter(DefaultGrindAdapters.VANILLA_BLOCK_STATE, DefaultAdapterFamilies.BLOCK_STATE);
        this.registerAdapter(DefaultGrindAdapters.VANILLA_ITEM, DefaultAdapterFamilies.ITEM);
        this.registerAdapter(DefaultGrindAdapters.VANILLA_ENCHANTMENT, DefaultAdapterFamilies.ENCHANTMENT);

        this.registerExternalAdapter(GrindConstants.MYTHIC_MOBS, MythicMobAdapter::new, DefaultAdapterFamilies.ENTITY);
        this.registerExternalAdapter(GrindConstants.EVEN_MORE_FISH, EvenMoreFishAdapter::new,
            DefaultAdapterFamilies.ITEM);
        this.registerExternalAdapter(GrindConstants.CUSTOM_CROPS, CustomCropsAdapter::new,
            DefaultAdapterFamilies.BLOCK);
    }

    private void registerTypes() {
        // ItemStack
        this.registerType(DefaultGrindTypes.BLOCK_LOOT, BlockLootGrindListener::new);
        this.registerType(DefaultGrindTypes.BREW_INGREDIENT, BrewingGrindListener::new);
        this.registerType(DefaultGrindTypes.BREW_POTION, BrewingTypeGrindListener::new);
        this.registerType(DefaultGrindTypes.COOKING, CookingGrindListener::new);
        this.registerType(DefaultGrindTypes.CRAFTING, CraftingGrindListener::new);
        this.registerType(DefaultGrindTypes.EATING, EatingGrindListener::new);
        this.registerType(DefaultGrindTypes.FISH_ITEM, FishingItemsGrindListener::new);
        this.registerType(DefaultGrindTypes.FORGING, ForgingGrindListener::new);
        this.registerType(DefaultGrindTypes.MOB_LOOT, MobLootGrindListener::new);
        this.registerType(DefaultGrindTypes.GRINDSTONE, GrindstoneGrindListener::new);
        this.registerType(DefaultGrindTypes.SMITHING, SmithingGrindListener::new);

        // Entity
        this.registerType(DefaultGrindTypes.KILLING, KillingGrindListener::new);
        this.registerType(DefaultGrindTypes.BREEDING, BreedingGrindListener::new);
        this.registerType(DefaultGrindTypes.FISH_MOBS, FishingMobsGrindListener::new);
        this.registerType(DefaultGrindTypes.MILKING, MilkingGrindListener::new);
        this.registerType(DefaultGrindTypes.SHEARING, ShearingGrindListener::new);
        this.registerType(DefaultGrindTypes.TAMING, TamingGrindListener::new);

        // Block + Block State
        this.registerType(DefaultGrindTypes.MINING, BreakBlockGrindListener::new);
        this.registerType(DefaultGrindTypes.BUILDING, BuildingGrindListener::new);
        this.registerType(DefaultGrindTypes.FERTILIZING, FertilizingGrindListener::new);
        this.registerType(DefaultGrindTypes.STRIP_BLOCK, StripBlockGrindListener::new);

        // Independent
        this.registerType(DefaultGrindTypes.ENCHANTING, EnchantingGrindListener::new);
    }

    public <B, A extends GrindAdapter<B>> void registerExternalAdapter(String pluginName,
                                                                       Function<String, A> function,
                                                                       GrindAdapterFamily<B> family) {
        if (!Plugins.isInstalled(pluginName)) return;

        this.plugin.info("Registering adapter for the '" + pluginName + "' plugin.");
        this.registerAdapter(pluginName, function, family);
    }

    public <B, A extends GrindAdapter<B>> void registerAdapter(String name,
                                                               Function<String, A> function,
                                                               GrindAdapterFamily<B> family) {
        A adapter = function.apply(name);
        this.registerAdapter(adapter, family);
    }

    public <B, A extends GrindAdapter<B>> void registerAdapter(A adapter, GrindAdapterFamily<B> family) {
        family.addAdapter(adapter);
        this.registry.registerAdapter(adapter);
    }

    public <T> void registerType(String id,
                                 GrindAdapterFamily<T> family,
                                 GrindListenerProvider<T> listenerProvider) {
        this.registerType(id, family, null, listenerProvider);
    }

    public <T> void registerType(String id,
                                 GrindAdapterFamily<T> family,
                                 @Nullable Class<? extends GrindModifier> modifier,
                                 GrindListenerProvider<T> listenerProvider) {
        GrindType<T> grindType = new GenericGrindType<>(id, family, modifier);

        this.registerType(grindType, listenerProvider);
    }

    public <T> void registerType(GrindType<T> type, GrindListenerProvider<T> listenerProvider) {
        if (this.settings.isTypeDisabled(type.getId())) return;

        GrindListener<T> listener = listenerProvider.provide(this.plugin, this, this.protectionManager, type);

        this.registerType(type, listener);
    }

    public <T> void registerType(GrindType<T> type, GrindListener<T> listener) {
        this.addListener(listener);
        this.registry.registerType(type);
    }

    private void registerCodecs() {
        ConfigCodecs.register(BrewingGrindModifier.class, BrewingGrindModifier.CODEC);
        ConfigCodecs.register(CookingGrindModifier.class, CookingGrindModifier.CODEC);
        ConfigCodecs.register(EnchantingGrindModifier.class, EnchantingGrindModifier.CODEC);
        ConfigCodecs.register(MobKillGrindModifier.class, MobKillGrindModifier.CODEC);

        ConfigCodecs.register(SourceReward.class, SourceReward.CODEC);
        ConfigCodecs.register(SourceTable.class, SourceTable.CODEC);

        ConfigCodecs.register(GrindObjective.class, new GrindObjectiveCodec(this.registry));
    }

    private void loadProtection() {
        if (!this.settings.isProtectionEnabled()) return;

        this.protectionManager = new GrindProtectionManager(this.plugin, this.settingsFile);
        this.protectionManager.setup();
    }

    private void loadGrindBar() {
        if (!this.settings.isGrindBarEnabled()) return;

        this.barManager = new GrindBarManager(this.plugin, this.settingsFile);
        this.barManager.setup();
    }

    public void loadObjectives() {
        Path dir = this.plugin.dataPath().resolve(JobsFiles.DIR_OBJECTIVES);
        if (!Files.exists(dir)) {
            this.createDefaultObjectives();
        }

        FileUtil.findYamlFiles(dir).forEach(this::loadObjective);

        this.plugin.info("Loaded %s job objectives.".formatted(this.registry.countObjectives()));
    }

    public void loadObjective(Path file) {
        FileConfig config = FileConfig.load(file);
        String id = FileUtil.getNameWithoutExtension(file);

        GrindObjective objective = config.get("", GrindObjective.class);
        if (objective == null) return;

        config.saveChanges();

        this.registry.registerObjective(id, objective);
    }

    public void createDefaultObjectives() {
        DefaultGrindObjectives.createDefaultObjectives().forEach((id, objective) -> {
            Path path = this.plugin.dataPath().resolve(JobsFiles.DIR_OBJECTIVES).resolve(FileConfig.withExtension(id));
            FileConfig config = FileConfig.load(path);
            config.set("", objective);
            config.saveChanges();
        });
    }

    private void updateJobObjectives() {
        this.jobManager.getJobs().forEach(this::updateJobObjectives);
    }

    private void updateJobObjectives(Job job) {
        String path = "Objectives";
        Path file = this.jobManager.getJobPath(job);
        FileConfig config = FileConfig.load(file);

        if (!config.contains(path) || config.getSection(path).isEmpty()) return;

        List<String> newIds = new ArrayList<>();

        config.getSection(path).forEach(sId -> {
            String newID = "v1_" + job.getId() + "_" + sId;
            newIds.add(newID);

            GrindObjective objective = config.get(path + "." + sId, GrindObjective.class);
            if (objective == null) return;

            Path objPath = this.plugin.dataPath()
                .resolve(JobsFiles.DIR_OBJECTIVES)
                .resolve(FileConfig.withExtension(newID));

            FileConfig objConfig = FileConfig.load(objPath);
            objConfig.set("", objective);
            objConfig.saveChanges();
        });

        config.remove(path);
        config.set("Grind.Objective-Ids", newIds);
        config.saveChanges();
    }

    private <B> List<GrindObjective> getObjectives(Job job, GrindType<B> grindType) {
        JobGrinding grinding = job.getGrinding();
        if (grinding == null) return List.of();

        Set<String> objectiveIds = grinding.getObjectiveIds();
        if (objectiveIds.isEmpty()) return List.of();

        List<GrindObjective> objectives = new ArrayList<>();
        objectiveIds.forEach(id -> {
            GrindObjective objective = this.registry.getObjective(id);
            if (objective == null || !objective.getTypeId().equals(grindType.getId())) return;

            objectives.add(objective);
        });

        return objectives;
    }

    public <T, G extends GrindType<T>> void proceedObjectives(Player player, G grindType, T source,
                                                              GrindContext context) {
        this.jobManager.getActiveJobs(player).forEach(jobInfo -> {
            Job job = jobInfo.job();
            JobData jobData = jobInfo.data();

            for (GrindObjective objective : this.getObjectives(job, grindType)) {
                SourceTable srcTable = objective.getSourceTable();
                SourceReward srcReward = this.findReward(source, grindType, srcTable);
                if (srcReward == null) continue;

                this.handleObjective(player, job, jobData, srcReward, context, objective);
            }
        });
    }

    private void handleObjective(Player player,
                                 Job job,
                                 JobData data,
                                 SourceReward srcReward,
                                 GrindContext context,
                                 GrindObjective objective) {
        Currency currency = EconomyBridge.api().getCurrency(this.settings.getPreferredCurrency());
        if (currency == null) return;

        GrindReward origReward = new GrindReward();
        GrindModifier origModifier = objective.getModifier();

        for (GrindObjectiveProperty property : GrindObjectiveProperty.values()) {
            double roll = srcReward.roll(property);
            origReward.put(property, roll);
        }

        GrindRewardEvent rewardEvent = new GrindRewardEvent(player, job, data, objective, origModifier, origReward, currency);
        this.plugin.getPluginManager().callEvent(rewardEvent);
        if (rewardEvent.isCancelled()) return;

        GrindReward finalReward = rewardEvent.getReward();
        GrindModifier modifier = rewardEvent.getModifier();
        if (modifier != null) {
            modifier.modify(finalReward, context);
        }

        double chance = finalReward.get(GrindObjectiveProperty.PROBABILITY);
        double income = finalReward.get(GrindObjectiveProperty.INCOME);

        if (!Randomizer.checkChance(chance)) return;

        GrindRewardProceedEvent proceedEvent = new GrindRewardProceedEvent(player, job, data, objective, finalReward, currency);

        if (income > 0 && this.settings.isBarElementEnabled()) {
            String format = this.settings.getBarElementFormat();
            proceedEvent.getBarElements().add(new GrindBarElement(currency, income, format));
        }

        this.plugin.getPluginManager().callEvent(proceedEvent);

        currency.deposit(player, income);
    }

    private <B, G extends GrindType<B>> @Nullable SourceReward findReward(B source,
                                                                          G grindType,
                                                                          SourceTable table) {
        GrindAdapter<B> adapter = grindType.getFamily().getBestAdapterFor(source);
        if (adapter == null) return null;

        String fullName = adapter.serializeFromBukkit(source);
        if (fullName == null) return null;

        return table.getByNameOrDefault(fullName);
    }

    public void setWorkstationOwnerId(TileState station, UUID uuid) {
        PDCUtil.set(station, this.stationOwnerKey, uuid);
    }

    @Nullable
    public UUID getWorkstationOwnerId(TileState station) {
        return PDCUtil.getUUID(station, this.stationOwnerKey).orElse(null);
    }

    @Nullable
    public Player getWorkstationOwner(TileState station) {
        UUID uuid = getWorkstationOwnerId(station);
        return uuid == null ? null : Players.getPlayer(uuid);
    }

    public void setWorkstationMode(TileState station, WorkstationMode mode) {
        PDCUtil.set(station, this.stationModeKey, mode.getId());
    }

    @Nullable
    public WorkstationMode getWorkstationMode(TileState station) {
        return PDCUtil.getInt(station, this.stationModeKey).map(WorkstationMode::byId).orElse(null);
    }
}
