package su.nightexpress.excellentjobs.job.model;

import java.util.ArrayList;
import java.util.List;

import su.nightexpress.excellentjobs.job.codec.JobBehaviorCodec;

public class JobBehavior {

    public static final JobBehaviorCodec CODEC = new JobBehaviorCodec();

    private boolean      permissionRequired;
    private List<String> joinCommands;
    private List<String> leaveCommands;

    JobBehavior(Builder builder) {
        this.permissionRequired = builder.permissionRequired;
        this.joinCommands = builder.joinCommands;
        this.leaveCommands = builder.leaveCommands;
    }

    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    public List<String> getJoinCommands() {
        return joinCommands;
    }

    public List<String> getLeaveCommands() {
        return leaveCommands;
    }

    public static class Builder {

        private boolean      permissionRequired;
        private List<String> joinCommands;
        private List<String> leaveCommands;

        public Builder() {
            this.permissionRequired = false;
            this.joinCommands = new ArrayList<>();
            this.leaveCommands = new ArrayList<>();
        }

        public JobBehavior build() {
            return new JobBehavior(this);
        }

        public Builder setPermissionRequired(boolean permissionRequired) {
            this.permissionRequired = permissionRequired;
            return this;
        }

        public Builder setJoinCommands(List<String> joinCommands) {
            this.joinCommands = joinCommands;
            return this;
        }

        public Builder setLeaveCommands(List<String> leaveCommands) {
            this.leaveCommands = leaveCommands;
            return this;
        }
    }
}
