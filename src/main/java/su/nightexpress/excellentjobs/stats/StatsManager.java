package su.nightexpress.excellentjobs.stats;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsFiles;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.api.event.StatsLoadTrackersEvent;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.api.stats.TopEntry;
import su.nightexpress.excellentjobs.api.stats.TopTracker;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.stats.core.StatsLang;
import su.nightexpress.excellentjobs.stats.core.StatsSettings;
import su.nightexpress.excellentjobs.stats.data.StatsDataManager;
import su.nightexpress.excellentjobs.stats.listener.StatsListener;
import su.nightexpress.excellentjobs.stats.menu.StatTrackersMenu;
import su.nightexpress.excellentjobs.stats.menu.TopPlayersMenu;
import su.nightexpress.excellentjobs.stats.model.JobStats;
import su.nightexpress.excellentjobs.stats.model.JobTracker;
import su.nightexpress.nightcore.bridge.placeholder.PlaceholderRegistry;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.format.adaptive.AdaptiveFormatter;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class StatsManager extends AbstractManager<JobsPlugin> {

    private final JobManager      jobManager;
    private final UserDataManager userManager;

    private final StatsSettings    settings;
    private final StatsDataManager dataManager;

    private final Map<String, TopTracker> trackerMap;

    private StatTrackersMenu trackersMenu;
    private TopPlayersMenu   topPlayersMenu;

    public StatsManager(JobsPlugin plugin,
                        Database database,
                        UserDataManager userManager,
                        JobManager jobManager) {
        super(plugin);
        this.jobManager = jobManager;
        this.userManager = userManager;

        this.settings = new StatsSettings();
        this.dataManager = new StatsDataManager(plugin, database, this.settings);

        this.trackerMap = new ConcurrentHashMap<>();

        this.trackersMenu = new StatTrackersMenu(this.plugin, this);
        this.topPlayersMenu = new TopPlayersMenu(this.plugin, this);
    }

    @Override
    protected void onLoad() {
        this.plugin.injectLang(StatsLang.class);

        this.loadSettings();
        this.loadData();
        this.loadTrackers();
        this.loadUI();

        this.addListener(new StatsListener(this.plugin, this));

        this.addAsyncTask(this::updateTopTrackers, this.settings.getTopTrackerUpdateInterval());
    }

    @Override
    protected void onShutdown() {
        this.userManager.removeLoginListener(this.dataManager);
        this.dataManager.shutdown();
    }

    private void loadSettings() {
        Path file = this.plugin.dataPath().resolve(StatsFiles.FILE_SETTINGS);
        FileConfig.load(file).edit(this.settings::load);
    }

    private void loadData() {
        this.dataManager.setup();
        this.userManager.addLoginListener(this.dataManager);
    }

    private void loadTrackers() {
        StatsLoadTrackersEvent event = new StatsLoadTrackersEvent(new ArrayList<>());
        this.plugin.getPluginManager().callEvent(event);

        event.getTrackers().forEach(this::addTopTracker);

        this.plugin.info("Registered %s top trackers.".formatted(this.trackerMap.size()));
    }

    private void loadUI() {
        Path dir = this.plugin.dataPath().resolve(JobsFiles.DIR_UI);

        this.initMenu(this.trackersMenu, dir.resolve(StatsFiles.UI_TRACKERS));
        this.initMenu(this.topPlayersMenu, dir.resolve(StatsFiles.UI_TOP_PLAYERS));
    }

    public boolean hasTrackers() {
        return !this.trackerMap.isEmpty();
    }

    public @Nullable JobStats getStats(Player player, Job job) {
        return this.dataManager.getData(player.getUniqueId(), job.getId());
    }

    public JobStats getStatsOrCreate(Player player, Job job) {
        return this.dataManager.getDataOrCreateAndCache(player.getUniqueId(), job.getId());
    }

    public @Nullable TopTracker getTopTracker(String name) {
        return this.trackerMap.get(LowerCase.INTERNAL.apply(name));
    }

    public Set<TopTracker> getTrackers() {
        return Set.copyOf(this.trackerMap.values());
    }

    public boolean backToJobMenu(Player player, Job job) {
        return this.jobManager.openJobOptions(player, job);
    }

    public boolean showTrackersMenu(Player player, Job job) {
        return this.trackersMenu.show(player, job);
    }

    public boolean showTopPlayersMenu(Player player, Job job, TopTracker tracker) {
        return this.topPlayersMenu.show(player, new JobTracker(job, tracker));
    }

    public boolean openTrackersOrSingleTopMenu(Player player, Job job) {
        Set<TopTracker> trackers = this.getTrackers();
        if (trackers.size() > 1) {
            return this.showTrackersMenu(player, job);
        }

        TopTracker tracker = trackers.stream().findFirst().orElse(null);
        return tracker != null && this.showTopPlayersMenu(player, job, tracker);
    }

    public void handleJobMenuDefineLayout(JobMenuDefineLayoutEvent event) {
        AbstractObjectMenu<Job> jobMenu = event.getJobMenu();

        jobMenu.addDefaultButton("leaderboards", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GOLD_BLOCK)
                    .setDisplayName(TagWrappers.GRADIENT.with("#f7971e", "#ffd200").and(TagWrappers.BOLD).wrap(
                        "Leaderboards"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("View and compete with the"),
                        TagWrappers.GRAY.wrap("best players in this job!"),
                        "",
                        TagWrappers.COLOR.with("#ffd200").wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(jobMenu.getObject(context).placeholders())
                ))
                .action(context -> {
                    this.showTrackersMenu(context.getPlayer(), jobMenu.getObject(context));
                })
                .condition(context -> this.hasTrackers())
                .build()
            )
            .slots(32)
            .build()
        );
    }

    public void handleGrindReward(GrindRewardProceedEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        JobStats stats = this.getStatsOrCreate(player, job);

        GrindReward reward = event.getReward();

        stats.addIncome(reward.get(GrindObjectiveProperty.INCOME));
        stats.markDirty();
    }

    public void updateTopTrackers() {
        this.getTrackers().forEach(this::updateTopTracker);
    }

    public void updateTopTracker(TopTracker tracker) {
        tracker.update();
    }

    public void addTopTracker(TopTracker tracker) {
        this.trackerMap.put(LowerCase.INTERNAL.apply(tracker.getId()), tracker);

        this.plugin.addGlobalPlaceholders(registry -> {
            this.registerTopTrackerPlaceholders(registry, tracker);
        });
    }

    private void registerTopTrackerPlaceholders(PlaceholderRegistry registry, TopTracker tracker) {
        // Dynmaic placeholder for Jobs Browse menu.
        this.registerJobFormatterTrackerPositionPlaceholder(tracker);

        this.registerGlobalTrackerPositionPlaceholder(tracker, registry);
        this.registerGlobalTrackerLeaderboardPlaceholders(tracker, 10, registry);
    }

    private void registerJobFormatterTrackerPositionPlaceholder(TopTracker tracker) {
        AdaptiveFormatter<Job> formatter = this.jobManager.getJobFormatter();

        String varName = StatsConstants.JOB_FORMATTER_TOP_POSITION_TEMPLATE
            .formatted(tracker.getId());

        formatter.registerVariable(varName, (job, player) -> {
            TopEntry entry = tracker.getTop(job, player.getUniqueId());
            return entry == null ? Lang.OTHER_N_A.text() : String.valueOf(entry.position());
        });
    }

    private void registerGlobalTrackerPositionPlaceholder(TopTracker tracker, PlaceholderRegistry registry) {
        String key = StatsConstants.GLOBAL_POSITION_TEMPLATE.formatted(tracker.getId());

        registry.registerMapped(key, Job.class, (player, job) -> {
            TopEntry entry = tracker.getTop(job, player.getUniqueId());
            return entry == null ? Lang.OTHER_N_A.text() : String.valueOf(entry.position());
        });
    }

    private void registerGlobalTrackerLeaderboardPlaceholders(TopTracker tracker,
                                                              int positions,
                                                              PlaceholderRegistry registry) {
        String name = tracker.getId();
        String none = StatsLang.TOP_ENTRY_EMPTY.text();

        for (int count = 0; count < positions; count++) {
            int position = count + 1;
            String keyName = StatsConstants.GLOBAL_TOP_NAME_TEMPLATE.formatted(name, position);
            String keyValue = StatsConstants.GLOBAL_TOP_SCORE_TEMPLATE.formatted(name, position);

            registry.registerMapped(keyName, Job.class, (player, job) -> {
                TopEntry entry = tracker.getTop(job, position);
                if (entry == null) return none;

                return entry.user().getName();
            });

            registry.registerMapped(keyValue, Job.class, (player, job) -> {
                TopEntry entry = tracker.getTop(job, position);
                if (entry == null) return none;

                return entry.score();
            });
        }
    }
}
