package su.nightexpress.excellentjobs.level.progression;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.Progression;
import su.nightexpress.excellentjobs.level.progression.codec.LinearProgressionCodec;

@NullMarked
public class LinearProgression implements Progression {

    public static final String                 NAME  = "linear";
    public static final LinearProgressionCodec CODEC = new LinearProgressionCodec();

    public static final double DEFAULT_BASE = 150D;
    public static final double DEFAULT_STEP = 100D;

    private final double base;
    private final double step;

    public LinearProgression(double base, double step) {
        this.base = base;
        this.step = step;
    }

    public static LinearProgression createDefault() {
        return new LinearProgression(DEFAULT_BASE, DEFAULT_STEP);
    }

    @Override
    public double getRequiredXPForLevel(int level) {
        return this.base + (level - 1) * this.step;
    }

    public double getBase() {
        return base;
    }

    public double getStep() {
        return step;
    }
}
