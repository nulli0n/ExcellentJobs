package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexDoubleProperty extends FlexProperty<Double> {

    public FlexDoubleProperty(@NonNull String name, double defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.DOUBLE, FlexCodecs.DOUBLE, aliases);
    }

}
