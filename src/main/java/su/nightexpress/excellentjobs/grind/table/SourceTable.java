package su.nightexpress.excellentjobs.grind.table;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.grind.codec.SourceTableCodec;
import su.nightexpress.excellentjobs.grind.codec.SourceTableLegacyCodec;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class SourceTable {

    public static final SourceTableCodec       CODEC        = new SourceTableCodec();
    public static final SourceTableLegacyCodec LEGACY_CODEC = new SourceTableLegacyCodec();

    public static final SourceTable EMPTY = new SourceTable(Collections.emptyMap());

    private final Map<String, SourceReward> entires;

    public SourceTable() {
        this(new HashMap<>());
    }

    public SourceTable(Map<String, SourceReward> entires) {
        this.entires = entires;
    }

    public static Builder builder() {
        return new Builder();
    }

    public @Nullable SourceReward getEntry(String entry) {
        return this.entires.get(entry);
    }

    public @Nullable SourceReward getByNameOrDefault(String entry) {
        SourceReward reward = this.getEntry(entry);
        return reward == null ? this.getEntry(CommonPlaceholders.DEFAULT) : reward;
    }

    public Map<String, SourceReward> getEntryMap() {
        return this.entires;
    }

    public static class Builder {

        private final Map<String, SourceReward>           entires;
        private final Map<GrindObjectiveProperty, Double> scales;

        Builder() {
            this.entires = new LinkedHashMap<>();
            this.scales = new EnumMap<>(GrindObjectiveProperty.class);
        }

        public SourceTable build() {
            return new SourceTable(this.entires);
        }

        public Builder scale(GrindObjectiveProperty property, double multiplier) {
            this.scales.put(property, multiplier);
            return this;
        }

        public <K extends Keyed> EntryBuilder entry(K key) {
            String fullName = BukkitThing.getAsString(key);
            return new EntryBuilder(this, fullName);
        }

        public EntryBuilder defaultEntry() {
            return new EntryBuilder(this, CommonPlaceholders.DEFAULT);
        }

        public Builder addEntry(String fullName, SourceReward reward) {
            SourceReward multiplied = new SourceReward();
            for (GrindObjectiveProperty property : GrindObjectiveProperty.values()) {
                double multiplier = this.scales.getOrDefault(property, 1D);
                double[] values = reward.get(property);
                double[] scaled = {values[0] * multiplier, values[1] * multiplier};

                multiplied.set(property, scaled);
            }

            this.entires.put(fullName, multiplied);
            return this;
        }
    }

    public static class EntryBuilder {

        private final String  fullName;
        private final Builder builder;

        private double[] xp             = {0D, 0D};
        private double[] income         = {0D, 0D};
        private double[] contractPoints = {0D, 0D};
        private double[] probability    = {100D, 100D};

        public EntryBuilder(Builder builder, String fullName) {
            this.builder = builder;
            this.fullName = fullName;
        }

        public Builder add() {
            return this.builder.addEntry(this.fullName, this.build());
        }

        public SourceReward build() {
            SourceReward reward = new SourceReward();
            reward.setProperty(SourceReward.XP, this.xp);
            reward.setProperty(SourceReward.INCOME, this.income);
            reward.setProperty(SourceReward.CONTRACT_POINTS, this.contractPoints);
            reward.setProperty(SourceReward.PROBABILITY, this.probability);
            return reward;
        }

        public EntryBuilder xp(double value) {
            return this.xp(value, value);
        }

        public EntryBuilder xp(double min, double max) {
            this.xp = new double[]{min, max};
            return this;
        }

        public EntryBuilder income(double value) {
            return this.income(value, value);
        }

        public EntryBuilder income(double min, double max) {
            this.income = new double[]{min, max};
            return this;
        }

        public EntryBuilder contractPoints(double value) {
            return this.contractPoints(value, value);
        }

        public EntryBuilder contractPoints(double min, double max) {
            this.contractPoints = new double[]{min, max};
            return this;
        }

        public EntryBuilder probability(double value) {
            return this.probability(value, value);
        }

        public EntryBuilder probability(double min, double max) {
            this.probability = new double[]{min, max};
            return this;
        }
    }
}
