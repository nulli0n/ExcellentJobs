package su.nightexpress.excellentjobs.contract.model;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.job.model.Job;

public record ContractJob(@NonNull Contract contract, @NonNull Job job) {

}
