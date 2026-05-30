package su.nightexpress.excellentjobs.contract.definition;

import java.util.List;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.contract.codec.ContractPromotionCodec;

public class ContractPromotion {

    public static final ContractPromotionCodec CODEC = new ContractPromotionCodec();

    private final boolean      enabled;
    private final boolean      durationEnabled;
    private final long         durationMillis;
    private final boolean      cooldownEnabled;
    private final long         cooldownMillis;
    private final int          pointsGoal;
    private final List<String> rewardCommands;
    private final List<String> rewardMessages;

    private ContractPromotion(@NonNull Builder builder) {
        this.enabled = builder.enabled;
        this.durationEnabled = builder.durationEnabled;
        this.durationMillis = builder.durationMillis;
        this.cooldownEnabled = builder.cooldownEnabled;
        this.cooldownMillis = builder.cooldownMillis;
        this.pointsGoal = builder.pointsGoal;
        this.rewardCommands = builder.rewardCommands;
        this.rewardMessages = builder.rewardMessages;
    }

    public static ContractPromotion defaults() {
        return new Builder().build();
    }

    public boolean hasDurationLimit() {
        return this.durationEnabled && this.durationMillis > 0;
    }

    public boolean hasCooldown() {
        return this.cooldownEnabled && this.cooldownMillis > 0;
    }

    public long generateDurationTimestamp() {
        return System.currentTimeMillis() + this.durationMillis;
    }

    public long generateCooldownTimestamp() {
        return System.currentTimeMillis() + this.cooldownMillis;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isDurationEnabled() {
        return this.durationEnabled;
    }

    public long getDurationMillis() {
        return this.durationMillis;
    }

    public boolean isCooldownEnabled() {
        return this.cooldownEnabled;
    }

    public long getCooldownMillis() {
        return this.cooldownMillis;
    }

    public int getPointsGoal() {
        return this.pointsGoal;
    }

    public @NonNull List<String> getRewardCommands() {
        return this.rewardCommands;
    }

    public @NonNull List<String> getRewardMessages() {
        return this.rewardMessages;
    }

    public static class Builder {

        private boolean      enabled;
        private boolean      durationEnabled;
        private long         durationMillis = 0L;
        private boolean      cooldownEnabled;
        private long         cooldownMillis = 0L;
        private int          pointsGoal;
        private List<String> rewardCommands = List.of();
        private List<String> rewardMessages = List.of();

        public @NonNull Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public @NonNull Builder durationEnabled(boolean durationEnabled) {
            this.durationEnabled = durationEnabled;
            return this;
        }

        public @NonNull Builder durationMillis(long durationMillis) {
            this.durationMillis = durationMillis;
            return this;
        }

        public @NonNull Builder cooldownEnabled(boolean cooldownEnabled) {
            this.cooldownEnabled = cooldownEnabled;
            return this;
        }

        public @NonNull Builder cooldownMillis(long cooldownMillis) {
            this.cooldownMillis = cooldownMillis;
            return this;
        }

        public @NonNull Builder pointsGoal(int pointsGoal) {
            this.pointsGoal = pointsGoal;
            return this;
        }

        public @NonNull Builder rewardCommands(List<String> rewardCommands) {
            this.rewardCommands = rewardCommands;
            return this;
        }

        public @NonNull Builder rewardMessages(List<String> rewardMessages) {
            this.rewardMessages = rewardMessages;
            return this;
        }

        public @NonNull ContractPromotion build() {
            return new ContractPromotion(this);
        }
    }
}
