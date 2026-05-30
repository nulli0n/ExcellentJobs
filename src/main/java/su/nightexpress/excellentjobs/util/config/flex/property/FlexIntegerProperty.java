package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexIntegerProperty extends FlexProperty<Integer> {

    public FlexIntegerProperty(@NonNull String name, int defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.INT, FlexCodecs.INTEGER, aliases);
    }

}
