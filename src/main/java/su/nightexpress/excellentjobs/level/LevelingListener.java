package su.nightexpress.excellentjobs.level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.event.JobLeaveEvent;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.api.event.StatsLoadTrackersEvent;
import su.nightexpress.nightcore.manager.AbstractListener;

@NullMarked
public class LevelingListener extends AbstractListener<JobsPlugin> {

    private final LevelingManager manager;

    public LevelingListener(JobsPlugin plugin, LevelingManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJobMenuDefineLayout(JobMenuDefineLayoutEvent event) {
        this.manager.handleJobMenuDefineLayout(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onGrindRewardProceed(GrindRewardProceedEvent event) {
        this.manager.handleGrindRewardEvent(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJobLeave(JobLeaveEvent event) {
        this.manager.handleJobLeave(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onStatsLoad(StatsLoadTrackersEvent event) {
        this.manager.handleStatsTrackerLoad(event);
    }
}
