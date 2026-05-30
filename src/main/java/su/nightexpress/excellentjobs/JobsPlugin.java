package su.nightexpress.excellentjobs;

import org.bukkit.plugin.ServicePriority;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.config.CoreSettings;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.config.Perms;
import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.level.LevelingManager;
import su.nightexpress.excellentjobs.stats.StatsManager;
import su.nightexpress.excellentjobs.zone.ZoneManager;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.userdata.UserDataManager;

public class JobsPlugin extends NightPlugin {

    private final JobsAPIProvider api      = new JobsAPIProvider(this);
    private final CoreSettings    settings = new CoreSettings();

    Database   dataHandler;
    JobManager jobManager;

    @Nullable
    GrindManager    grindManager;
    @Nullable
    LevelingManager levelingManager;
    @Nullable
    ContractManager contractManager;

    @Nullable
    ZoneManager  zoneManager;
    @Nullable
    StatsManager statsManager;

    @Override
    protected @NonNull PluginDetails getDefaultDetails() {
        return PluginDetails.create("Jobs", new String[]{"jobs", "job", "excellentjobs"});
    }

    @Override
    protected void addRegistries() {
        this.registerLang(Lang.class);
    }

    @Override
    protected boolean disableCommandManager() {
        return true;
    }

    @Override
    public PermissionNamespace getCorePermissions() {
        return Perms.ROOT;
    }

    @Override
    public void enable() {
        this.settings.load(this.getEngineConfig());

        UserDataManager userDataManager = this.getUserDataManager();

        this.dataHandler = new Database(this);
        this.dataHandler.setup();

        this.jobManager = new JobManager(this, this.dataHandler, userDataManager);

        if (this.settings.isGrindEnabled()) {
            this.loadGrinding(this.jobManager);
        }

        if (this.settings.isLevelingEnabled()) {
            this.loadLeveling(userDataManager, this.jobManager);
        }

        if (this.settings.isContractsEnabled()) {
            this.loadContracts(this.dataHandler, userDataManager, this.jobManager);
        }

        if (this.settings.isZonesEnabled()) {
            this.loadZones(this.jobManager);
        }

        if (this.settings.isStatsEnabled()) {
            this.loadStats(this.dataHandler, userDataManager, this.jobManager);
        }

        this.jobManager.setup();

        this.registerGlobalPlaceholders();

        this.getServer().getServicesManager().register(JobsAPIProvider.class, this.api, this,
            ServicePriority.Normal
        );
    }

    @Override
    public void disable() {
        if (this.statsManager != null) this.statsManager.shutdown();
        if (this.zoneManager != null) this.zoneManager.shutdown();
        if (this.levelingManager != null) this.levelingManager.shutdown();
        if (this.grindManager != null) this.grindManager.shutdown();
        if (this.contractManager != null) this.contractManager.shutdown();
        if (this.jobManager != null) this.jobManager.shutdown();

        this.dataHandler.shutdown();
    }

    private void loadGrinding(JobManager jobManager) {
        GrindManager manager = new GrindManager(this, jobManager);
        manager.setup();

        this.grindManager = manager;
    }

    private void loadLeveling(UserDataManager userManager, JobManager jobManager) {
        LevelingManager manager = new LevelingManager(this, userManager, jobManager);
        manager.setup();

        this.levelingManager = manager;
    }

    private void loadContracts(Database database, UserDataManager userManager, JobManager jobManager) {
        ContractManager manager = new ContractManager(this, database, userManager, jobManager);
        manager.setup();

        this.contractManager = manager;
    }

    private void loadZones(JobManager jobManager) {
        ZoneManager manager = new ZoneManager(this, jobManager);
        manager.setup();

        this.zoneManager = manager;
    }

    private void loadStats(Database database, UserDataManager userManager, JobManager jobManager) {
        StatsManager manager = new StatsManager(this, database, userManager, jobManager);
        manager.setup();

        this.statsManager = manager;
    }

    public JobManager getJobManager() {
        return this.jobManager;
    }

    @Override
    public @NonNull String getPlaceholderAPIIdentifier() {
        return "excellentjobs";
    }
}