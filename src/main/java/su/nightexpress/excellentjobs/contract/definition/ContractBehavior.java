package su.nightexpress.excellentjobs.contract.definition;

import java.time.DayOfWeek;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.contract.codec.ContractBehaviorCodec;
import su.nightexpress.excellentjobs.contract.model.ObjectiveModifier;

@NullMarked
public class ContractBehavior {

    public static final ContractBehaviorCodec CODEC = new ContractBehaviorCodec();

    private final long                                           leaveCooldown;
    private final Map<DayOfWeek, Timeframe>                      timeframes;
    private final Map<GrindObjectiveProperty, ObjectiveModifier> objectiveModifiers;

    ContractBehavior(Builder builder) {
        this.leaveCooldown = builder.leaveCooldown;
        this.timeframes = builder.timeframes;
        this.objectiveModifiers = builder.objectiveModifiers;
    }

    public static ContractBehavior defaults() {
        return new Builder().build();
    }

    public long generateLeaveCooldownTimestamp() {
        if (this.leaveCooldown <= 0L) return this.leaveCooldown;

        return System.currentTimeMillis() + this.leaveCooldown;
    }

    public long getLeaveCooldown() {
        return this.leaveCooldown;
    }

    public @Nullable Timeframe getTimeframe(DayOfWeek day) {
        return this.timeframes.get(day);
    }

    public Map<DayOfWeek, Timeframe> getTimeframes() {
        return this.timeframes;
    }

    public @Nullable ObjectiveModifier getObjectiveModifier(GrindObjectiveProperty property) {
        return this.objectiveModifiers.get(property);
    }

    public Map<GrindObjectiveProperty, ObjectiveModifier> getObjectiveModifiers() {
        return this.objectiveModifiers;
    }

    public static class Builder {

        private long                                           leaveCooldown;
        private Map<DayOfWeek, Timeframe>                      timeframes;
        private Map<GrindObjectiveProperty, ObjectiveModifier> objectiveModifiers;

        public Builder() {
            this.leaveCooldown = 0L;
            this.timeframes = Map.of();
            this.objectiveModifiers = Map.of();
        }

        public Builder setLeaveCooldown(long leaveCooldown) {
            this.leaveCooldown = leaveCooldown;
            return this;
        }

        public Builder setTimeframes(Map<DayOfWeek, Timeframe> timeframes) {
            this.timeframes = timeframes;
            return this;
        }

        public Builder setObjectiveModifiers(Map<GrindObjectiveProperty, ObjectiveModifier> objectiveModifiers) {
            this.objectiveModifiers = objectiveModifiers;
            return this;
        }

        public ContractBehavior build() {
            return new ContractBehavior(this);
        }
    }
}
