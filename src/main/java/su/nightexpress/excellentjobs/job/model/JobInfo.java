package su.nightexpress.excellentjobs.job.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.data.JobData;

@NullMarked
public record JobInfo(Job job, JobData data) {

}
