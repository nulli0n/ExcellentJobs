package su.nightexpress.excellentjobs.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public abstract class GrindObjectiveEvent extends Event {

    protected final Player         player;
    protected final Job            job;
    protected final JobData        jobData;
    protected final GrindObjective objective;

    protected GrindObjectiveEvent(Player player, Job job, JobData data, GrindObjective objective) {
        this.player = player;
        this.job = job;
        this.jobData = data;
        this.objective = objective;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Job getJob() {
        return this.job;
    }

    public JobData getJobData() {
        return this.jobData;
    }

    public GrindObjective getObjective() {
        return this.objective;
    }
}
