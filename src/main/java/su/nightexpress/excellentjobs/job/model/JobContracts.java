package su.nightexpress.excellentjobs.job.model;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.codec.JobContractsCodec;

@NullMarked
public class JobContracts {

    public static final JobContractsCodec CODEC = new JobContractsCodec();

    private boolean     required;
    private Set<String> allowedIds;

    public JobContracts(boolean contractRequired, Set<String> contractIds) {
        this.required = contractRequired;
        this.allowedIds = contractIds;
    }

    public boolean isRequired() {
        return required;
    }

    public Set<String> getAllowedIds() {
        return allowedIds;
    }
}
