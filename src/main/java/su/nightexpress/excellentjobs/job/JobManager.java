package su.nightexpress.excellentjobs.job;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsFiles;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.JobJoinEvent;
import su.nightexpress.excellentjobs.api.event.JobLeaveEvent;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.job.command.JobCommands;
import su.nightexpress.excellentjobs.job.command.argument.JobArgumentType;
import su.nightexpress.excellentjobs.job.core.JobLang;
import su.nightexpress.excellentjobs.job.core.JobPerms;
import su.nightexpress.excellentjobs.job.core.JobSettings;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.data.JobDataManager;
import su.nightexpress.excellentjobs.job.dialog.JobDialogKeys;
import su.nightexpress.excellentjobs.job.dialog.impl.JobJoinDialog;
import su.nightexpress.excellentjobs.job.dialog.impl.JobQuitConfirmDialog;
import su.nightexpress.excellentjobs.job.listener.JobGenericListener;
import su.nightexpress.excellentjobs.job.menu.JobOptionsMenu;
import su.nightexpress.excellentjobs.job.menu.JobsMenu;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobContracts;
import su.nightexpress.excellentjobs.job.model.JobDefinition;
import su.nightexpress.excellentjobs.job.model.JobGrinding;
import su.nightexpress.excellentjobs.job.model.JobInfo;
import su.nightexpress.excellentjobs.job.model.JobLeveling;
import su.nightexpress.excellentjobs.job.placeholder.JobPlaceholderProvider;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.RankTable;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.format.adaptive.AdaptiveFormatter;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class JobManager extends AbstractManager<JobsPlugin> {

    private final UserDataManager userDataManager;
    private final JobSettings     settings;
    private final JobDataManager  dataManager;

    private final Map<String, Job> jobByIdMap;

    private final AdaptiveFormatter<Job> jobFormatter;

    private JobsMenu       jobsMenu;
    private JobOptionsMenu optionsMenu;

    public JobManager(JobsPlugin plugin, Database database, UserDataManager userDataManager) {
        super(plugin);
        this.userDataManager = userDataManager;

        this.settings = new JobSettings();
        this.dataManager = new JobDataManager(plugin, database, this.settings);

        this.jobByIdMap = new HashMap<>();

        this.jobFormatter = new AdaptiveFormatter<>();

        this.jobsMenu = new JobsMenu(plugin, this, this.jobFormatter);
        this.optionsMenu = new JobOptionsMenu(plugin, this);

        // Register placeholder mapping right here so other modules can register job related placeholders.
        this.plugin.addGlobalPlaceholders(registry -> {
            registry.addResolver(Job.class, (player, payload) -> this.getJobById(payload));
        });
    }

    @Override
    protected void onLoad() {
        this.plugin.injectLang(JobLang.class);
        this.plugin.registerPermissions(JobPerms.ROOT);

        this.registerArgumentTypes();
        this.registerCodecs();

        this.loadSettings();
        this.loadFormatter();
        this.loadData();
        this.loadCommands();
        this.loadJobs();
        this.loadDialogs();
        this.loadUI();
        this.loadPlaceholders();

        this.addListener(new JobGenericListener(this.plugin, this));

        this.addAsyncTask(this::updateEmployeesAmount, this.settings.getJobEmployeeCountUpdateInterval());
    }

    @Override
    protected void onShutdown() {
        this.userDataManager.removeLoginListener(this.dataManager);
        this.dataManager.shutdown();

        this.jobByIdMap.clear();
    }

    private void registerArgumentTypes() {
        Arguments.register(Job.class, new JobArgumentType(this));
    }

    private void registerCodecs() {
        ConfigCodecs.register(JobGrinding.class, JobGrinding.CODEC);
        ConfigCodecs.register(JobLeveling.class, JobLeveling.CODEC);
        ConfigCodecs.register(JobContracts.class, JobContracts.CODEC);
        ConfigCodecs.register(JobDefinition.class, JobDefinition.CODEC);
    }

    private void loadSettings() {
        Path file = this.plugin.dataPath().resolve(JobFiles.FILE_SETTINGS);
        FileConfig.load(file).edit(this.settings::load);
    }

    private void loadFormatter() {
        this.jobFormatter.registerCondition("employed", (job, player) -> this.isEmployed(player, job));
        this.jobFormatter.registerCondition("not_employed", (job, player) -> !this.isEmployed(player, job));
        this.jobFormatter.registerCondition("can_join", (job, player) -> this.canJoinJob(player, job));
        this.jobFormatter.registerCondition("can_not_join", (job, player) -> !this.canJoinJob(player, job));

        this.jobFormatter.registerVariable("employees", (job, player) -> NumberUtil.format(job.getEmployees()));
    }

    private void loadData() {
        this.dataManager.setup();
        this.userDataManager.addLoginListener(this.dataManager);
    }

    private void loadCommands() {
        this.plugin.addCommandProvider(new JobCommands(this));
    }

    private void loadJobs() {
        Path dir = this.plugin.dataPath().resolve(JobFiles.DIR_JOBS);
        if (!Files.exists(dir)) {
            JobDefaults.createDefaultJobs().forEach(job -> {
                Path file = dir.resolve(FileConfig.withExtension(job.getId()));
                FileConfig config = FileConfig.load(file);

                config.set("Definition", job.getDefinition());
                config.set("Grind", job.getGrinding());
                config.set("Leveling", job.getLeveling());
                config.set("Contract", job.getContracts());
                config.saveChanges();
            });
        }

        FileUtil.findYamlFiles(dir).forEach(this::loadJob);

        this.plugin.info("Loaded %s jobs.".formatted(this.jobByIdMap.size()));
    }

    public void loadJob(Path file) {
        FileConfig config = FileConfig.load(file);

        if (!config.contains("Definition")) {
            String name = config.getOrSet("Name", ConfigCodecs.STRING, "Unnamed");
            List<String> description = config.getOrSet("Description", ConfigCodecs.STRING_LIST, List.of());
            NightItem icon = config.getOrSet("Icon", ConfigCodecs.NIGHT_ITEM, NightItem.fromType(Material.GOLDEN_HOE));
            boolean permissionRequired = config.getOrSet("Permission_Required", ConfigCodecs.BOOLEAN, false);
            NightBarColor barColor = config.getOrSet("ProgressBar.Color", ConfigCodecs.forEnum(NightBarColor.class),
                NightBarColor.GREEN);
            List<String> joinCommands = config.getOrSet("General.JoinCommands", ConfigCodecs.STRING_LIST, List.of());
            List<String> leaveCommands = config.getOrSet("General.LeaveCommands", ConfigCodecs.STRING_LIST, List.of());

            int maxLevel = config.getOrSet("Leveling.Max_Level", ConfigCodecs.INT, 100);

            config.remove("Name");
            config.remove("Description");
            config.remove("Icon");
            config.remove("Permission_Required");
            config.remove("ProgressBar");
            config.remove("General");
            config.remove("Leveling");

            config.set("Definition.Name", name);
            config.set("Definition.Description", description);
            config.set("Definition.Icon", icon);
            config.set("Definition.Permission-Required", permissionRequired);
            config.set("Definition.Join-Commands", joinCommands);
            config.set("Definition.Leave-Commands", leaveCommands);

            config.set("Grind.Bar-Color", barColor);

            config.set("Leveling.Max-Level", maxLevel);
        }

        String name = FileUtil.getNameWithoutExtension(file);
        String id = Strings.varStyle(name).orElse(null);
        if (id == null) {
            this.plugin.error("Unacceptable job file name: '%s'".formatted(file));
            return;
        }

        JobDefinition definition = config.get("Definition", JobDefinition.class);
        if (definition == null) {
            this.plugin.error("Could not read definition for '%s' job.".formatted(file));
            return;
        }

        JobLeveling leveling = config.get("Leveling", JobLeveling.class);
        JobGrinding grinding = config.get("Grind", JobGrinding.class);
        JobContracts contracts = config.get("Contract", JobContracts.class);

        Job job = new Job(id, definition, leveling, grinding, contracts);

        this.jobByIdMap.put(job.getId(), job);

        config.saveChanges();
    }

    private void loadDialogs() {
        this.plugin.dialogRegistry().register(JobDialogKeys.JOB_QUIT, new JobQuitConfirmDialog(this));
        this.plugin.dialogRegistry().register(JobDialogKeys.JOB_JOIN, new JobJoinDialog(this));
    }

    private void loadUI() {
        Path uiDir = this.plugin.dataPath().resolve(JobsFiles.DIR_UI);

        this.initMenu(this.jobsMenu, uiDir.resolve(JobFiles.UI_JOB_BROWSE));
        this.initMenu(this.optionsMenu, uiDir.resolve(JobFiles.UI_JOB_OPTIONS));
    }

    private void loadPlaceholders() {
        this.plugin.addGlobalPlaceholders(new JobPlaceholderProvider(this));
    }

    private void updateEmployeesAmount() {
        Map<String, Integer> count = this.dataManager.countEmployees();

        this.getJobs().forEach(job -> {
            int employees = count.getOrDefault(job.getId(), 0);
            job.setEmployees(employees);
        });
    }

    public Map<String, Map<UUID, Integer>> getJobLevels() {
        return this.dataManager.getJobLevels();
    }

    public Path getJobPath(Job job) {
        return this.plugin.dataPath().resolve(JobsFiles.DIR_JOBS).resolve(FileConfig.withExtension(job.getId()));
    }

    public JobSettings getSettings() {
        return settings;
    }

    public AdaptiveFormatter<Job> getJobFormatter() {
        return jobFormatter;
    }

    public Map<String, Job> getJobByIdMap() {
        return jobByIdMap;
    }

    public Set<Job> getJobs() {
        return new HashSet<>(this.jobByIdMap.values());
    }

    public List<String> getJobIds() {
        return new ArrayList<>(this.jobByIdMap.keySet());
    }

    @Nullable
    public Job getJobById(String id) {
        return this.jobByIdMap.get(id.toLowerCase());
    }

    public @Nullable Job getJob(JobData data) {
        return this.getJobById(data.getJobId());
    }

    public @Nullable JobData getData(Player player, Job job) {
        return this.getData(player, job.getId());
    }

    public @Nullable JobData getData(Player player, String jobId) {
        return this.dataManager.getData(player.getUniqueId(), jobId);
    }

    public @Nullable JobData getActiveData(Player player, Job job) {
        return this.getActiveData(player, job.getId());
    }

    public @Nullable JobData getActiveData(Player player, String jobId) {
        JobData data = this.getData(player, jobId);
        return data == null || !data.isActive() || data.isRemoved() ? null : data;
    }

    public Set<JobInfo> getAllJobs(Player player) {
        Set<JobInfo> set = new HashSet<>();

        this.getJobs().forEach(job -> {
            JobData data = this.getData(player, job);
            if (data == null || data.isRemoved()) return;

            set.add(new JobInfo(job, data));
        });

        return set;
    }

    public Set<JobInfo> getActiveJobs(Player player) {
        Set<JobInfo> set = new HashSet<>();

        this.getJobs().forEach(job -> {
            JobData data = this.getActiveData(player, job);
            if (data == null) return;

            set.add(new JobInfo(job, data));
        });

        return set;
    }

    public void loadAndManageUserJobDataAsync(String name,
                                              Job job,
                                              BiConsumer<@Nullable UserData, @Nullable JobData> consumer) {
        this.userDataManager.loadByNameAndCacheAsync(name).thenCompose(opt -> {
            if (opt.isEmpty()) {
                consumer.accept(null, null);
                return CompletableFuture.completedFuture(null);
            }

            UserData userData = opt.get();
            UUID userId = userData.getId();

            return this.dataManager.loadOrCreateAndCacheAsync(userId, job.getId()).thenAcceptAsync(optData -> {
                if (optData.isEmpty()) {
                    consumer.accept(userData, null);
                    return;
                }

                JobData data = optData.get();
                consumer.accept(userData, data);
            }, this.plugin::runTask);
        });
    }

    public boolean canGetMoreJobs(Player player) {
        return this.countAvailableJobs(player) != 0;
    }

    public boolean isEmployed(Player player, Job job) {
        return this.getActiveData(player, job) != null;
    }

    public boolean canJoinJob(Player player, Job job) {
        return !this.isEmployed(player, job) && job.hasPermission(player) && this.canGetMoreJobs(player);
    }

    public boolean hasJobLeaveCooldown(Player player, Job job) {
        JobData data = this.getActiveData(player, job);
        return data != null && !data.isLeaveCooldownExpired();
    }

    public int getJobsLimit(Player player) {
        RankTable table = this.settings.getJobAmountLimits();
        return table.getGreatestOrNegative(player).intValue();
    }

    public int countActiveJobs(Player player) {
        return this.getActiveJobs(player).size();
    }

    public int countAvailableJobs(Player player) {
        int limit = this.getJobsLimit(player);
        if (limit < 0) return limit;

        return Math.max(0, limit - this.countActiveJobs(player));
    }

    public int countTotalLevel(Player player) {
        return this.getAllJobs(player).stream()
            .mapToInt(info -> info.data().getLevel())
            .sum();
    }

    public int countTotalEffectiveLevel(Player player) {
        return this.getActiveJobs(player).stream()
            .mapToInt(info -> info.data().getLevel())
            .sum();
    }

    public void showJobsMenu(Player player) {
        this.jobsMenu.show(player);
    }

    public boolean showJobSettingsOrSelection(Player player, Job job) {
        if (this.isEmployed(player, job)) {
            this.showJobOptions(player, job);
        }
        else {
            this.showJobsMenu(player);
        }
        return true;
    }

    public boolean showJobOptions(Player player, Job job) {
        return this.optionsMenu.show(player, job);
    }

    public boolean openJobOptions(Player player, Job job) {
        if (!this.isEmployed(player, job)) {
            Lang.JOB_ERROR_NOT_EMPLOYED.message().sendWith(player, ctx -> ctx.with(job.placeholders()));
            return false;
        }

        return this.showJobOptions(player, job);
    }

    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.revokeAccessIfPermissionLost(player);
    }

    private void runJobCommands(Player player, Job job, List<String> commands) {
        PlaceholderContext ctx = PlaceholderContext.builder()
            .with(job.placeholders())
            .build();

        List<String> replaced = ctx.apply(commands);
        Players.dispatchCommands(player, replaced);
    }

    private void revokeAccessIfPermissionLost(Player player) {
        this.getActiveJobs(player).forEach(info -> {
            Job job = info.job();
            if (job.hasPermission(player)) return;

            JobData data = info.data();
            data.setActive(false);
            data.markDirty();
        });
    }

    public boolean leaveJob(Player player, Job job) {
        return this.leaveJob(player, job, false);
    }

    public boolean leaveJob(Player player, Job job, boolean bypass) {
        JobData data = this.getActiveData(player, job);
        if (data == null) {
            Lang.JOB_ERROR_NOT_EMPLOYED.message().sendWith(player, ctx -> ctx.with(job.placeholders()));
            return false;
        }

        if (!bypass && !data.isLeaveCooldownExpired()) {
            JobLang.JOB_LEAVE_COOLDOWN.message().sendWith(player, ctx -> ctx
                .with(job.placeholders())
                .with(CommonPlaceholders.GENERIC_TIME, () -> {
                    return TimeFormats.formatDuration(data.getLeaveCooldown(), TimeFormatType.LITERAL);
                })
            );
            return false;
        }

        JobLeaveEvent event = new JobLeaveEvent(player, job, data);
        this.plugin.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        job.removeEmployee(1);
        this.runJobCommands(player, job, job.getLeaveCommands());

        data.setActive(false);
        data.markDirty();

        JobLang.JOB_LEAVE_SUCCESS.message().sendWith(player, ctx -> ctx
            .with(job.placeholders())
        );
        return true;
    }

    public boolean joinJob(Player player, Job job) {
        return this.joinJob(player, job, false, true);
    }

    public boolean joinJob(Player player, Job job, boolean bypass, boolean callEvent) {
        if (!bypass) {
            if (!job.hasPermission(player)) {
                Lang.JOB_ERROR_NO_PERMISSION.message().sendWith(player, ctx -> ctx.with(job.placeholders()));
                return false;
            }

            if (!this.canGetMoreJobs(player)) {
                JobLang.JOB_JOIN_LIMIT_REACHED.message().sendWith(player, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> String.valueOf(this.getJobsLimit(player)))
                );
                return false;
            }
        }

        JobData data = this.getData(player, job);
        if (data == null || data.isRemoved()) {
            data = new JobData(player.getUniqueId(), job.getId());
        }
        else {
            if (data.isActive()) {
                JobLang.JOB_JOIN_ALREADY.message().sendWith(player, ctx -> ctx.with(job.placeholders()));
                return false;
            }
        }

        if (callEvent) {
            JobJoinEvent event = new JobJoinEvent(player, job, data);
            this.plugin.getPluginManager().callEvent(event);
            if (event.isCancelled()) return false;
        }

        job.addEmployee(1);

        if (this.settings.isLeaveCooldownEnabled()) {
            long cooldown = this.settings.getLeaveCooldownValue();
            data.setLeaveCooldown(TimeUtil.createFutureTimestamp(cooldown));
        }

        data.setActive(true);
        data.markDirty();
        this.dataManager.cachePermanently(data);

        this.runJobCommands(player, job, job.getJoinCommands());

        JobLang.JOB_JOIN_SUCCESS.message().sendWith(player, ctx -> ctx
            .with(job.placeholders())
        );

        return true;
    }
}
