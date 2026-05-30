package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexDoubleArrayProperty extends FlexProperty<double[]> {

    public FlexDoubleArrayProperty(@NonNull String name, double @NonNull [] defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.DOUBLE_ARRAY, FlexCodecs.DOUBLE_ARRAY, aliases);
    }
}
