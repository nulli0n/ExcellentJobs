package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexLongArrayProperty extends FlexProperty<long[]> {

    public FlexLongArrayProperty(@NonNull String name, long @NonNull [] defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.LONG_ARRAY, FlexCodecs.LONG_ARRAY, aliases);
    }
}
