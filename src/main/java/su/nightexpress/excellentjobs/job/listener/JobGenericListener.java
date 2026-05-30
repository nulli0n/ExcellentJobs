package su.nightexpress.excellentjobs.job.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.nightcore.manager.AbstractListener;

@NullMarked
public class JobGenericListener extends AbstractListener<JobsPlugin> {

    private final JobManager manager;

    public JobGenericListener(JobsPlugin plugin, JobManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        this.manager.handleJoin(event);
    }
}
