package su.nightexpress.excellentjobs.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public class JobLevelDownEvent extends JobLevelEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public JobLevelDownEvent(Player player, Job job, JobData data, int oldLevel) {
        super(player, job, data, oldLevel);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }


    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
