package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexIntArrayProperty extends FlexProperty<int[]> {

    public FlexIntArrayProperty(@NonNull String name, int @NonNull [] defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.INT_ARRAY, FlexCodecs.INT_ARRAY, aliases);
    }
}
