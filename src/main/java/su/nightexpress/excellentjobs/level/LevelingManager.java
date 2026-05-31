package su.nightexpress.excellentjobs.level;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsFiles;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.event.JobLeaveEvent;
import su.nightexpress.excellentjobs.api.event.JobLevelDownEvent;
import su.nightexpress.excellentjobs.api.event.JobLevelUpEvent;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.api.event.StatsLoadTrackersEvent;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.leveling.Progression;
import su.nightexpress.excellentjobs.api.leveling.Reward;
import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.api.stats.TopTracker;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.job.JobFiles;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobLeveling;
import su.nightexpress.excellentjobs.level.bar.LevelingBarElement;
import su.nightexpress.excellentjobs.level.command.LevelingCommands;
import su.nightexpress.excellentjobs.level.core.LevelingLang;
import su.nightexpress.excellentjobs.level.core.LevelingPerms;
import su.nightexpress.excellentjobs.level.core.LevelingSettings;
import su.nightexpress.excellentjobs.level.menu.LevelsMenu;
import su.nightexpress.excellentjobs.level.placeholder.LevelingPlaceholderProvider;
import su.nightexpress.excellentjobs.level.progression.GeometricProgression;
import su.nightexpress.excellentjobs.level.progression.LinearProgression;
import su.nightexpress.excellentjobs.level.reward.RequirementRegistry;
import su.nightexpress.excellentjobs.level.reward.model.RewardBase;
import su.nightexpress.excellentjobs.level.reward.model.RewardModel;
import su.nightexpress.excellentjobs.level.reward.model.RewardPool;
import su.nightexpress.excellentjobs.level.reward.model.RewardValue;
import su.nightexpress.excellentjobs.level.reward.requirement.PermissionRequirement;
import su.nightexpress.excellentjobs.level.reward.requirement.RankRequirement;
import su.nightexpress.excellentjobs.level.stats.LevelTopTracker;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.format.adaptive.AdaptiveFormatter;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class LevelingManager extends AbstractManager<JobsPlugin> {

    private final UserDataManager userManager;
    private final JobManager      jobManager;

    private final Map<String, ConfigCodec<? extends Progression>> progressionCodecs;

    private final LevelingSettings    settings;
    private final RequirementRegistry requirementRegistry;

    private final Map<String, RewardPool> rewardPoolMap;

    private Progression progression;
    private LevelsMenu  levelsMenu;

    public LevelingManager(JobsPlugin plugin, UserDataManager userManager, JobManager jobManager) {
        super(plugin);
        this.userManager = userManager;
        this.jobManager = jobManager;

        this.progressionCodecs = new HashMap<>();
        this.rewardPoolMap = new HashMap<>();

        this.settings = new LevelingSettings();
        this.progression = LinearProgression.createDefault();
        this.requirementRegistry = new RequirementRegistry();

        this.levelsMenu = new LevelsMenu(plugin, this);
    }

    @Override
    protected void onLoad() {
        this.plugin.injectLang(LevelingLang.class);
        this.plugin.registerPermissions(LevelingPerms.ROOT);
        this.plugin.addCommandProvider(new LevelingCommands(this));

        this.initRegistries();
        this.registerProgressionCodecs();
        this.registerRequirementCodecs();
        this.regsiterRewardCodecs();

        this.loadSettings();
        this.loadFormatter();
        this.loadRewards();
        this.loadUI();
        this.loadPlaceholders();

        this.addListener(new LevelingListener(this.plugin, this));
    }

    @Override
    protected void onShutdown() {
        this.progressionCodecs.clear();

        RequirementRegistry.shutdown();
    }


    private void initRegistries() {
        RequirementRegistry.init(this.requirementRegistry);
    }

    private void registerProgressionCodecs() {
        this.registerProgressionCodec(LinearProgression.NAME, LinearProgression.class, LinearProgression.CODEC);
        this.registerProgressionCodec(GeometricProgression.NAME, GeometricProgression.class,
            GeometricProgression.CODEC);
    }

    private void registerRequirementCodecs() {
        this.registerRewardRequirement("rank", RankRequirement.class, RankRequirement.CODEC);
        this.registerRewardRequirement("permission", PermissionRequirement.class, PermissionRequirement.CODEC);
    }

    private void regsiterRewardCodecs() {
        ConfigCodecs.register(RewardBase.class, RewardBase.CODEC);
        ConfigCodecs.register(RewardModel.class, RewardModel.CODEC);
        ConfigCodecs.register(RewardValue.class, RewardValue.CODEC);
        ConfigCodecs.register(RewardPool.class, RewardPool.CODEC);
    }

    public <T extends RewardRequirement> void registerRewardRequirement(String name,
                                                                        Class<T> type,
                                                                        ConfigCodec<T> codec) {
        this.requirementRegistry.register(name, type);
        ConfigCodecs.register(type, codec);
    }

    private <T extends Progression> void registerProgressionCodec(String name, Class<T> type, ConfigCodec<T> codec) {
        this.progressionCodecs.put(name, codec);
        ConfigCodecs.register(type, codec);
    }

    private void loadSettings() {
        Path file = this.plugin.dataPath().resolve(JobFiles.FILE_LEVELING);
        FileConfig.load(file).edit(config -> {
            this.settings.load(config);
            this.loadProgression(config);
        });
    }

    private void loadFormatter() {
        AdaptiveFormatter<Job> formatter = this.jobManager.getJobFormatter();

        formatter.registerVariable("max_level", (job, player) -> NumberUtil.format(this.getMaxLevel(job)));

        formatter.registerVariable("xp", (job, player) -> {
            JobData data = this.jobManager.getActiveData(player, job);
            return NumberUtil.format(data == null ? 0D : data.getXP());
        });

        formatter.registerVariable("level", (job, player) -> {
            JobData data = this.jobManager.getActiveData(player, job);
            return NumberUtil.format(data == null ? 0 : data.getLevel());
        });

        formatter.registerVariable("xp_required", (job, player) -> {
            JobData data = this.jobManager.getActiveData(player, job);
            int level = data == null ? LevelingConstants.START_LEVEL : data.getLevel();
            return NumberUtil.format(this.getRequiredXP(job, level));
        });
    }

    private void loadProgression(FileConfig config) {
        String progressionType = this.settings.getProgressionType();

        ConfigCodec<? extends Progression> codec = this.progressionCodecs.get(progressionType);
        if (codec == null) {
            this.plugin.error("Unknown progression type '%s'. Using '%s' progression."
                .formatted(progressionType, LinearProgression.NAME)
            );
            codec = LinearProgression.CODEC;
        }

        Progression configuredProgression = this.settings.loadProgression(config, progressionType, codec,
            this.progression);
        if (configuredProgression != null) {
            this.progression = configuredProgression;
        }
        else {
            this.plugin.error("Could not load progression settings.");
        }
    }

    private void loadRewards() {
        Path dir = this.plugin.dataPath().resolve(LevelingFiles.DIR_REWARDS);
        if (!Files.exists(dir)) {
            LevelingDefaults.getDefaultRewardPools().forEach((poolName, pool) -> {
                FileConfig config = FileConfig.load(dir.resolve(FileConfig.withExtension(poolName)));
                config.set("", pool);
                config.save();
            });
        }

        FileUtil.findYamlFiles(dir).forEach(this::loadRewardPool);
        this.plugin.info("Loaded %s level rewards.".formatted(this.rewardPoolMap.size()));
    }

    private void loadRewardPool(Path file) {
        String id = FileUtil.getNameWithoutExtension(file);
        FileConfig config = FileConfig.load(file);
        RewardPool container = config.get("", RewardPool.class);
        if (container == null) {
            this.plugin.error("Unable to load '%s' reward pool.".formatted(file));
            return;
        }

        config.saveChanges();
        this.rewardPoolMap.put(LowerCase.INTERNAL.apply(id), container);
    }

    private void loadUI() {
        Path uiDir = this.plugin.dataPath().resolve(JobsFiles.DIR_UI);

        this.initMenu(this.levelsMenu, uiDir.resolve(LevelingFiles.UI_LEVELS));
    }

    private void loadPlaceholders() {
        this.plugin.addGlobalPlaceholders(new LevelingPlaceholderProvider(this.jobManager, this));
    }

    public Progression getProgression() {
        return this.progression;
    }

    public RequirementRegistry getRequirementRegistry() {
        return this.requirementRegistry;
    }

    public boolean showJobMenu(Player player, Job job) {
        return this.jobManager.openJobOptions(player, job);
    }

    public boolean openLevelsMenu(Player player, Job job, JobData data) {
        return this.levelsMenu.openAtLevel(player, job, data);
    }

    public boolean showLevels(Player player, Job job) {
        JobData data = this.jobManager.getActiveData(player, job);
        if (data == null) {
            Lang.JOB_ERROR_NOT_EMPLOYED.message().sendWith(player, ctx -> ctx.with(job.placeholders()));
            return false;
        }

        return this.openLevelsMenu(player, job, data);
    }

    public int countTotalLevel(Player player) {
        return this.jobManager.getAllJobs(player).stream()
            .mapToInt(info -> info.data().getLevel())
            .sum();
    }

    public int countTotalEffectiveLevel(Player player) {
        return this.jobManager.getActiveJobs(player).stream()
            .mapToInt(info -> info.data().getLevel())
            .sum();
    }

    public int getMaxLevel(Job job) {
        JobLeveling leveling = job.getLeveling();
        if (leveling == null) return LevelingConstants.START_LEVEL;

        return leveling.getMaxLevel();
    }

    public double getRequiredXP(Job job, int level) {
        int maxLevel = this.getMaxLevel(job);
        if (level >= maxLevel) return -1D;

        return this.progression.getRequiredXPForLevel(level);
    }

    public @Nullable Reward getLevelReward(Job job, int level) {
        JobLeveling leveling = job.getLeveling();
        if (leveling == null) return null;

        RewardPool bestPool = null;

        for (String poolName : leveling.getRewardPoolIds()) {
            RewardPool pool = this.getRewardPool(poolName);
            if (pool == null || !pool.hasReward(level)) continue;

            if (bestPool == null || pool.getPriority() > bestPool.getPriority()) {
                bestPool = pool;
            }
        }
        if (bestPool == null) return null;

        return bestPool.getReward(level);
    }

    public @Nullable RewardPool getRewardPool(String name) {
        return this.rewardPoolMap.get(LowerCase.INTERNAL.apply(name));
    }

    public void handleJobMenuDefineLayout(JobMenuDefineLayoutEvent event) {
        AbstractObjectMenu<Job> jobMenu = event.getJobMenu();

        jobMenu.addDefaultButton("levels", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.EXPERIENCE_BOTTLE)
                    .setDisplayName(TagWrappers.GRADIENT.with("#56ab2f", "#a8e063")
                        .and(TagWrappers.BOLD).wrap("Levels & Rewards"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Track your progression and"),
                        TagWrappers.GRAY.wrap("claim level rewards!"),
                        "",
                        TagWrappers.COLOR.with(LevelingConstants.ACCENT_COLOR)
                            .wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(jobMenu.getObject(context).placeholders())
                ))
                .action(context -> {
                    Job job = jobMenu.getObject(context);
                    this.showLevels(context.getPlayer(), job);
                })
                .build()
            )
            .slots(13)
            .build()
        );
    }

    public void handleStatsTrackerLoad(StatsLoadTrackersEvent event) {
        if (!this.settings.isLeaderboardTrackerEnabled()) return;

        TopTracker tracker = new LevelTopTracker(this.userManager, this.jobManager, this.settings);
        event.getTrackers().add(tracker);
    }

    public void handleGrindRewardEvent(GrindRewardProceedEvent event) {
        double xp = event.getReward().get(GrindObjectiveProperty.XP);

        Player player = event.getPlayer();
        Job job = event.getJob();
        JobData data = event.getJobData();

        if (xp != 0D) {
            this.addXP(player, job, xp);
        }

        if (this.settings.isBarElementEnabled()) {
            String format = this.settings.getBarElementFormat();
            double needXP = this.getRequiredXP(job, data.getLevel());
            double progress = data.getXP() / needXP;

            LevelingBarElement element = new LevelingBarElement(format, xp, progress);
            event.getBarElements().add(element);
        }
    }

    public void handleJobLeave(JobLeaveEvent event) {
        if (!this.settings.isProgressionResetOnLeave()) return;

        JobData data = event.getJobData();

        data.resetLeveling();
    }

    public boolean claimLevelReward(Player player, Job job, int level) {
        JobData data = this.jobManager.getActiveData(player, job);
        if (data == null) return false;
        if (data.getClaimedLevelRewards().contains(level)) return false;
        if (data.getLevel() < level) return false;
        if (!this.settings.isRewardClaimRequired()) return false;

        Reward reward = this.getLevelReward(job, level);
        if (reward == null || !reward.isAvailable(player, job)) return false;

        reward.give(player);
        data.addClaimedLevelReward(level);
        data.markDirty();
        return true;
    }

    public void giveXP(CommandSender sender, String name, Job job, double amount, boolean silent) {
        this.jobManager.loadAndManageUserJobDataAsync(name, job, (userData, jobData) -> {
            if (userData == null) {
                CoreLang.ERROR_INVALID_PLAYER.message().send(sender);
                return;
            }
            if (jobData == null) {
                return;
            }

            Player player = userData.getPlayer();
            this.grantXP(player, job, jobData, amount);

            LevelingLang.XP_GIVE_FEEDBACK.message().sendWith(sender, ctx -> ctx
                .with(job.placeholders())
                .with(jobData.placeholders())
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            );

            if (player != null && !silent) {
                LevelingLang.XP_GIVE_NOTIFY.message().sendWith(player, ctx -> ctx
                    .with(job.placeholders())
                    .with(jobData.placeholders())
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
                );
            }
        });
    }

    public void takeXP(CommandSender sender, String name, Job job, double amount, boolean silent) {
        this.jobManager.loadAndManageUserJobDataAsync(name, job, (userData, jobData) -> {
            if (userData == null) {
                CoreLang.ERROR_INVALID_PLAYER.message().send(sender);
                return;
            }
            if (jobData == null) {
                return;
            }

            Player player = userData.getPlayer();
            this.subtractXP(player, job, jobData, amount);

            LevelingLang.XP_TAKE_FEEDBACK.message().sendWith(sender, ctx -> ctx
                .with(job.placeholders())
                .with(jobData.placeholders())
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            );

            if (player != null && !silent) {
                LevelingLang.XP_TAKE_NOTIFY.message().sendWith(player, ctx -> ctx
                    .with(job.placeholders())
                    .with(jobData.placeholders())
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
                );
            }
        });
    }

    public void giveLevel(CommandSender sender, String name, Job job, int amount, boolean silent) {
        this.jobManager.loadAndManageUserJobDataAsync(name, job, (userData, jobData) -> {
            if (userData == null) {
                CoreLang.ERROR_INVALID_PLAYER.message().send(sender);
                return;
            }
            if (jobData == null) {
                return;
            }

            Player player = userData.getPlayer();
            this.grantLevel(player, job, jobData, amount);

            LevelingLang.LEVEL_GIVE_FEEDBACK.message().sendWith(sender, ctx -> ctx
                .with(job.placeholders())
                .with(jobData.placeholders())
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            );

            if (player != null && !silent) {
                LevelingLang.LEVEL_GIVE_NOTIFY.message().sendWith(player, ctx -> ctx
                    .with(job.placeholders())
                    .with(jobData.placeholders())
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
                );
            }
        });
    }

    public void takeLevel(CommandSender sender, String name, Job job, int amount, boolean silent) {
        this.jobManager.loadAndManageUserJobDataAsync(name, job, (userData, jobData) -> {
            if (userData == null) {
                CoreLang.ERROR_INVALID_PLAYER.message().send(sender);
                return;
            }
            if (jobData == null) {
                return;
            }

            Player player = userData.getPlayer();
            this.subtractLevel(player, job, jobData, amount);

            LevelingLang.LEVEL_TAKE_FEEDBACK.message().sendWith(sender, ctx -> ctx
                .with(job.placeholders())
                .with(jobData.placeholders())
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            );

            if (player != null && !silent) {
                LevelingLang.LEVEL_TAKE_NOTIFY.message().sendWith(player, ctx -> ctx
                    .with(job.placeholders())
                    .with(jobData.placeholders())
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
                );
            }
        });
    }

    public boolean addXP(Player player, Job job, double amount) {
        JobData data = this.jobManager.getActiveData(player, job);
        if (data == null) return false;
        if (Math.abs(amount) == 0D) return false;

        if (amount < 0) {
            this.subtractXP(player, job, data, amount);
        }
        else {
            this.grantXP(player, job, data, amount);
        }
        return true;
    }

    private void grantXP(@Nullable Player player, Job job, JobData data, double add) {
        double newXP = data.getXP() + Math.abs(add);

        int level = data.getLevel();
        double xpRequired = this.getRequiredXP(job, level);

        while (newXP >= xpRequired && xpRequired > 0) {
            newXP -= xpRequired;
            level++;
            data.setLevel(level);

            xpRequired = this.getRequiredXP(job, level);

            if (player != null) {
                this.triggerLevelUp(player, job, data);
            }
        }

        data.setXP(newXP);
        data.setLevel(level);
        data.markDirty();
    }

    private void subtractXP(@Nullable Player player, Job job, JobData data, double remove) {
        double newXP = data.getXP() - Math.abs(remove);
        int level = data.getLevel();

        while (newXP < 0 && level > 0) {
            level--;
            data.setLevel(level);

            if (player != null) {
                this.triggerLevelDown(player, job, data);
            }

            double previousXP = this.getRequiredXP(job, level);

            newXP += previousXP;
        }

        if (newXP < 0) {
            newXP = 0;
        }

        data.setXP(newXP);
        data.setLevel(level);
        data.markDirty();
    }

    public boolean addLevel(Player player, Job job, int amount) {
        JobData data = this.jobManager.getActiveData(player, job);
        if (data == null) return false;

        int absolute = Math.abs(amount);
        if (absolute == 0) return false;

        if (amount < 0) {
            this.subtractLevel(player, job, data, absolute);
        }
        else {
            this.grantLevel(player, job, data, absolute);
        }
        return true;
    }

    private void grantLevel(@Nullable Player player, Job job, JobData data, int amount) {
        data.setXP(0);

        for (int count = 0; count < amount; count++) {
            if (data.getLevel() >= this.getMaxLevel(job)) break;

            data.setLevel(data.getLevel() + 1);

            if (player != null) {
                this.triggerLevelUp(player, job, data);
            }
        }

        data.markDirty();
    }

    private void subtractLevel(@Nullable Player player, Job job, JobData data, int amount) {
        data.setXP(0);

        for (int count = 0; count < amount; count++) {
            if (data.getLevel() == LevelingConstants.START_LEVEL) break;

            data.setLevel(data.getLevel() - 1);

            if (player != null) {
                this.triggerLevelDown(player, job, data);
            }
        }

        data.markDirty();
    }

    private void triggerLevelUp(Player player, Job job, JobData data) {
        int level = data.getLevel();
        int oldLevel = level - 1;
        List<Reward> rewards = this.getAvailableRewards(player, job, level);

        JobLevelUpEvent event = new JobLevelUpEvent(player, job, data, oldLevel, rewards);
        this.plugin.getPluginManager().callEvent(event);

        LevelingLang.LEVEL_UP.message().sendWith(player, ctx -> ctx
            .with(job.placeholders())
            .with(data.placeholders())
        );

        this.triggerLevelUpRewards(player, data, event.getRewards());
    }

    private void triggerLevelDown(Player player, Job job, JobData data) {
        int level = data.getLevel();
        int oldLevel = level + 1;

        JobLevelDownEvent event = new JobLevelDownEvent(player, job, data, oldLevel);
        plugin.getPluginManager().callEvent(event);

        LevelingLang.LEVEL_DOWN.message().sendWith(player, ctx -> ctx
            .with(job.placeholders())
            .with(data.placeholders())
        );
    }

    public List<Reward> getAvailableRewards(Player player, Job job, int currentLevel) {
        JobData data = this.jobManager.getActiveData(player, job);
        if (data == null) return List.of();

        List<Reward> levelRewards = new ArrayList<>();

        for (int level = 0; level < currentLevel + 1; level++) {
            if (data.hasClaimedLevelReward(level)) continue;

            Reward reward = this.getLevelReward(job, level);
            if (reward != null && reward.isAvailable(player, job)) {
                levelRewards.add(reward);
            }
        }

        return levelRewards;
    }

    private void triggerLevelUpRewards(Player player, JobData data, List<Reward> rewards) {
        if (rewards.isEmpty()) return;

        int level = data.getLevel();
        boolean claimRequired = this.settings.isRewardClaimRequired();
        boolean needClaim = claimRequired && !player.hasPermission(LevelingPerms.REWARD_AUTO_CLAIM);

        if (needClaim) {
            LevelingLang.LEVEL_REWARDS_NOTIFY.message().sendWith(player, ctx -> ctx
                .with(data.placeholders())
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> String.valueOf(rewards.size()))
            );
            return;

        }

        // Mark all claimed.
        data.addClaimedLevelReward(IntStream.range(0, level).toArray());
        data.markDirty();

        // Run reward commands.
        rewards.forEach(reward -> reward.give(player));

        LevelingLang.LEVEL_REWARDS_AUTO_CLAIMED.message().sendWith(player, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_ENTRY, () -> {
                String entry = LevelingLang.LEVEL_REWARDS_ENTRY.text();

                return rewards.stream()
                    .map(reward -> PlaceholderContext.builder()
                        .with(reward.placeholders())
                        .build()
                        .apply(entry)
                    )
                    .collect(Collectors.joining(TagWrappers.BR));
            })
        );
    }
}
