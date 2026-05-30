package su.nightexpress.excellentjobs.level.progression;

import su.nightexpress.excellentjobs.api.leveling.Progression;
import su.nightexpress.excellentjobs.level.progression.codec.GeometricProgressionCodec;

public class GeometricProgression implements Progression {

    public static final String                    NAME  = "geometric";
    public static final GeometricProgressionCodec CODEC = new GeometricProgressionCodec();

    public static final double DEFAULT_BASE       = 900D;
    public static final double DEFAULT_MULTIPLIER = 1.091001D;

    private final double base;
    private final double multiplier;

    public GeometricProgression(double base, double multiplier) {
        this.base = base;
        this.multiplier = multiplier;
    }

    @Override
    public double getRequiredXPForLevel(int level) {
        return this.base * (Math.pow(this.multiplier, level));
    }

    public double getBase() {
        return base;
    }


    public double getMultiplier() {
        return multiplier;
    }
}
