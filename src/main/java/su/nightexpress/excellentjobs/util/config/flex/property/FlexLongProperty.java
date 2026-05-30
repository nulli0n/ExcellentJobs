package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexLongProperty extends FlexProperty<Long> {

    public FlexLongProperty(@NonNull String name, long defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.LONG, FlexCodecs.LONG, aliases);
    }

}
