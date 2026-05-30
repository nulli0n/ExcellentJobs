package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexBooleanProperty extends FlexProperty<Boolean> {

    public FlexBooleanProperty(@NonNull String name, boolean defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.BOOLEAN, FlexCodecs.BOOLEAN, aliases);
    }

}
