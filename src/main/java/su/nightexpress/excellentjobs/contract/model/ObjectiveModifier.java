package su.nightexpress.excellentjobs.contract.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.util.config.flex.FlexConfigObject;
import su.nightexpress.excellentjobs.util.config.flex.codec.FlexConfigCodec;
import su.nightexpress.excellentjobs.util.config.flex.property.FlexDoubleProperty;

@NullMarked
public class ObjectiveModifier extends FlexConfigObject {

    public static final FlexConfigCodec<ObjectiveModifier> CODEC = FlexConfigCodec.create(ObjectiveModifier::new);

    private static final FlexDoubleProperty BASE_MULTIPLIER   = new FlexDoubleProperty("Base", 1D, "baseMultiplier");
    private static final FlexDoubleProperty LEVEL_BONUS       = new FlexDoubleProperty("JobLevelBonus", 0D, "levelBonus");
    private static final FlexDoubleProperty TIMEFRAME_PENALTY = new FlexDoubleProperty("TimeframePenalty", 1D, "timePenalty");

    public ObjectiveModifier() {
        this.register(BASE_MULTIPLIER);
        this.register(LEVEL_BONUS);
        this.register(TIMEFRAME_PENALTY);
    }

    public static ObjectiveModifier create(double base, double levelBonus, double timeframePenalty) {
        ObjectiveModifier modifier = new ObjectiveModifier();
        modifier.setProperty(BASE_MULTIPLIER, base);
        modifier.setProperty(LEVEL_BONUS, levelBonus);
        modifier.setProperty(TIMEFRAME_PENALTY, timeframePenalty);

        return modifier;
    }

    public double getBaseMultiplier() {
        return this.get(BASE_MULTIPLIER);
    }

    public double getLevelBonus() {
        return this.get(LEVEL_BONUS);
    }

    public double getTimeframePenalty() {
        return this.get(TIMEFRAME_PENALTY);
    }
}
