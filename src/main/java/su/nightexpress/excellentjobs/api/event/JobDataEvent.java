package su.nightexpress.excellentjobs.api.event;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public abstract class JobDataEvent extends JobEvent {

    protected final JobData jobData;

    protected JobDataEvent(Player player, Job job, JobData jobData) {
        super(false, player, job);
        this.jobData = jobData;
    }

    public final JobData getJobData() {
        return this.jobData;
    }
}
