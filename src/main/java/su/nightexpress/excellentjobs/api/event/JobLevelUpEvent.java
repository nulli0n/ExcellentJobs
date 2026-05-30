package su.nightexpress.excellentjobs.api.event;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.Reward;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public class JobLevelUpEvent extends JobLevelEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<Reward> rewards;

    public JobLevelUpEvent(Player player, Job job, JobData data, int oldLevel, List<Reward> rewards) {
        super(player, job, data, oldLevel);
        this.rewards = rewards;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public List<Reward> getRewards() {
        return this.rewards;
    }
}
