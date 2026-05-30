package su.nightexpress.excellentjobs.stats.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.stats.StatsManager;
import su.nightexpress.nightcore.manager.AbstractListener;

public class StatsListener extends AbstractListener<JobsPlugin> {

    private final StatsManager manager;

    public StatsListener(JobsPlugin plugin, StatsManager statsManager) {
        super(plugin);
        this.manager = statsManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJobMenuDefineLayout(JobMenuDefineLayoutEvent event) {
        this.manager.handleJobMenuDefineLayout(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindReward(GrindRewardProceedEvent event) {
        this.manager.handleGrindReward(event);
    }
}
