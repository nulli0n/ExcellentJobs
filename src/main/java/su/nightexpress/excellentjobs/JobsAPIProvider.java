package su.nightexpress.excellentjobs;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.excellentjobs.data.Database;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.level.LevelingManager;
import su.nightexpress.excellentjobs.stats.StatsManager;
import su.nightexpress.excellentjobs.zone.ZoneManager;

@NullMarked
public class JobsAPIProvider {

    private final JobsPlugin plugin;

    public JobsAPIProvider(JobsPlugin plugin) {
        this.plugin = plugin;
    }

    public JobsPlugin getPlugin() {
        return plugin;
    }

    public Database getDataHandler() {
        return this.plugin.dataHandler;
    }

    public JobManager getJobManager() {
        return this.plugin.jobManager;
    }

    public @Nullable GrindManager getGrindManager() {
        return this.plugin.grindManager;
    }

    public @Nullable LevelingManager getLevelingManager() {
        return this.plugin.levelingManager;
    }

    public @Nullable ContractManager getContractManager() {
        return this.plugin.contractManager;
    }

    public @Nullable StatsManager getStatsManager() {
        return this.plugin.statsManager;
    }

    public @Nullable ZoneManager getZoneManager() {
        return this.plugin.zoneManager;
    }
}
