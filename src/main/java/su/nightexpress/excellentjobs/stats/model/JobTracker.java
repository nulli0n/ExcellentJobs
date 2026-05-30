package su.nightexpress.excellentjobs.stats.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.stats.TopTracker;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public record JobTracker(Job job, TopTracker tracker) {

}
