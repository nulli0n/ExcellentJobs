package su.nightexpress.excellentjobs.grind.table;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.util.config.flex.FlexConfigObject;
import su.nightexpress.excellentjobs.util.config.flex.codec.FlexConfigCodec;
import su.nightexpress.excellentjobs.util.config.flex.property.FlexDoubleArrayProperty;
import su.nightexpress.nightcore.util.Randomizer;

@NullMarked
public class SourceReward extends FlexConfigObject {

    public static final FlexConfigCodec<SourceReward> CODEC = FlexConfigCodec.create(SourceReward::new);

    private static final double[] DEFAULT_VALUE = {0D, 0D};

    public static final FlexDoubleArrayProperty XP              = new FlexDoubleArrayProperty("JobXP", DEFAULT_VALUE, "xp");
    public static final FlexDoubleArrayProperty INCOME          = new FlexDoubleArrayProperty("Income", DEFAULT_VALUE, "money");
    public static final FlexDoubleArrayProperty CONTRACT_POINTS = new FlexDoubleArrayProperty("ContractPoints", DEFAULT_VALUE, "contract");
    public static final FlexDoubleArrayProperty PROBABILITY     = new FlexDoubleArrayProperty("Probability", new double[]{100, 100}, "chance");

    public SourceReward() {
        this.setMinimized(true);
        this.register(XP);
        this.register(INCOME);
        this.register(CONTRACT_POINTS);
        this.register(PROBABILITY);
    }

    public void set(GrindObjectiveProperty property, double[] values) {
        switch (property) {
            case XP -> this.setProperty(SourceReward.XP, values);
            case INCOME -> this.setProperty(SourceReward.INCOME, values);
            case CONTRACT_POINTS -> this.setProperty(SourceReward.CONTRACT_POINTS, values);
            case PROBABILITY -> this.setProperty(SourceReward.PROBABILITY, values);
        }
    }

    public double[] get(GrindObjectiveProperty property) {
        return switch (property) {
            case XP -> this.get(SourceReward.XP);
            case INCOME -> this.get(SourceReward.INCOME);
            case CONTRACT_POINTS -> this.get(SourceReward.CONTRACT_POINTS);
            case PROBABILITY -> this.get(SourceReward.PROBABILITY);
        };
    }

    public double roll(GrindObjectiveProperty property) {
        double[] minMax = this.get(property);
        double min = minMax[0];
        double max = minMax[1];
        if (min == max) return min;

        return Randomizer.nextDouble(min, max);
    }
}
