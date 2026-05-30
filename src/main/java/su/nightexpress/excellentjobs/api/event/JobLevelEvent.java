package su.nightexpress.excellentjobs.api.event;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public abstract class JobLevelEvent extends JobDataEvent {

    protected final int oldLevel;

    protected JobLevelEvent(Player player, Job job, JobData jobData, int oldLevel) {
        super(player, job, jobData);
        this.oldLevel = oldLevel;
    }

    public int getOldLevel() {
        return this.oldLevel;
    }

    public int getNewLevel() {
        return this.jobData.getLevel();
    }
}
