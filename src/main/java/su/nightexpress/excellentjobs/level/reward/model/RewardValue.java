package su.nightexpress.excellentjobs.level.reward.model;

import su.nightexpress.excellentjobs.util.config.flex.FlexConfigObject;
import su.nightexpress.excellentjobs.util.config.flex.codec.FlexConfigCodec;
import su.nightexpress.excellentjobs.util.config.flex.property.FlexDoubleProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;

public class RewardValue extends FlexConfigObject {

    public static final ConfigCodec<RewardValue> CODEC = FlexConfigCodec.create(RewardValue::new);

    public static final FlexDoubleProperty BASE = new FlexDoubleProperty("Base", 0, "start");
    public static final FlexDoubleProperty STEP = new FlexDoubleProperty("Step", 0, "lvlStep");

    public RewardValue() {
        this.register(BASE);
        this.register(STEP);
    }

    public RewardValue(double base, double step) {
        this();
        this.setProperty(BASE, base);
        this.setProperty(STEP, step);
    }

    public double getBase() {
        return this.get(BASE);
    }

    public double getStep() {
        return this.get(STEP);
    }
}
