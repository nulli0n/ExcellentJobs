package su.nightexpress.excellentjobs.job.data;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.nightcore.db.state.StatefulData;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class JobData extends StatefulData implements PlaceholderResolvable {

    private final UUID   playerId;
    private final String jobId;

    private boolean                             active        = false;
    private long                                leaveCooldown = 0L;
    private Map<GrindObjectiveProperty, Double> limitProgress;
    private long                                limitTimestamp;

    private Set<Integer> claimedLevelRewards;
    private int          level = 0;
    private double       xp    = 0;

    public JobData(UUID playerId, String jobId) {
        this.playerId = playerId;
        this.jobId = jobId;
        this.claimedLevelRewards = new HashSet<>();
        this.limitProgress = new EnumMap<>(GrindObjectiveProperty.class);
    }

    JobData(Builder builder) {
        this.playerId = builder.playerId;
        this.jobId = builder.jobId;
        this.active = builder.active;
        this.leaveCooldown = builder.leaveCooldown;
        this.limitProgress = new EnumMap<>(builder.limitProgress);
        this.limitTimestamp = builder.limitTimestamp;

        this.xp = builder.xp;
        this.level = builder.level;
        this.claimedLevelRewards = new HashSet<>(builder.claimedLevelRewards);
    }

    @Override
    public PlaceholderResolver placeholders() {
        return JobsPlaceholders.JOB_DATA.resolver(this);
    }

    public boolean hasClaimedLevelReward(int level) {
        return this.claimedLevelRewards.contains(level);
    }

    public boolean hasLimitReached(GrindObjectiveProperty property) {
        return this.getLimitProgress(property) >= 1D;
    }

    public boolean isLeaveCooldownExpired() {
        return TimeUtil.isPassed(this.leaveCooldown);
    }

    public boolean isLimitTimeExceed() {
        return TimeUtil.isPassed(this.limitTimestamp);
    }

    public void resetAll() {
        this.resetLeveling();
        this.resetAdditional();
    }

    public void resetLeveling() {
        this.setLevel(0);
        this.setXP(0);
        this.markDirty();
    }

    public void resetAdditional() {
        this.setLeaveCooldown(0L);
        this.setClaimedLevelRewards(new HashSet<>());
        this.markDirty();
    }

    public void resetLimits() {
        this.limitProgress.clear();
        this.limitTimestamp = 0L;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getJobId() {
        return this.jobId;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getLeaveCooldown() {
        return this.leaveCooldown;
    }

    public void setLeaveCooldown(long cooldown) {
        this.leaveCooldown = cooldown;
    }

    public Map<GrindObjectiveProperty, Double> getLimitProgress() {
        return this.limitProgress;
    }

    public double getLimitProgress(GrindObjectiveProperty property) {
        return this.limitProgress.getOrDefault(property, 0D);
    }

    public void setLimitProgress(GrindObjectiveProperty property, double progress) {
        this.limitProgress.put(property, Math.clamp(progress, 0D, 1D));
    }

    public long getLimitTimestamp() {
        return this.limitTimestamp;
    }

    public void setLimitTimestamp(long limitTimestamp) {
        this.limitTimestamp = limitTimestamp;
    }

    public double getXP() {
        return this.xp;
    }

    public void setXP(double xp) {
        this.xp = xp;
    }


    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Set<Integer> getClaimedLevelRewards() {
        return this.claimedLevelRewards;
    }

    public void addClaimedLevelReward(int... levels) {
        this.claimedLevelRewards.addAll(IntStream.of(levels).boxed().toList());
    }

    public void setClaimedLevelRewards(Set<Integer> claimedLevelRewards) {
        this.claimedLevelRewards = claimedLevelRewards;
    }

    public static class Builder {

        private final UUID   playerId;
        private final String jobId;

        private boolean                             active;
        private long                                leaveCooldown;
        private Map<GrindObjectiveProperty, Double> limitProgress;
        private long                                limitTimestamp;

        private Set<Integer> claimedLevelRewards;
        private int          level;
        private double       xp;

        public Builder(UUID playerId, String jobId) {
            this.playerId = playerId;
            this.jobId = jobId;
            this.claimedLevelRewards = Set.of();
            this.limitProgress = new EnumMap<>(GrindObjectiveProperty.class);
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder setLeaveCooldown(long cooldown) {
            this.leaveCooldown = cooldown;
            return this;
        }

        public Builder setLimitProgress(Map<GrindObjectiveProperty, Double> limitProgress) {
            this.limitProgress.clear();
            this.limitProgress.putAll(limitProgress);
            return this;
        }

        public Builder setLimitTimestamp(long limitTimestamp) {
            this.limitTimestamp = limitTimestamp;
            return this;
        }

        public Builder setXP(double xp) {
            this.xp = xp;
            return this;
        }

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Builder setClaimedLevelRewards(Set<Integer> claimedLevelRewards) {
            this.claimedLevelRewards = claimedLevelRewards;
            return this;
        }

        public JobData build() {
            return new JobData(this);
        }
    }
}
