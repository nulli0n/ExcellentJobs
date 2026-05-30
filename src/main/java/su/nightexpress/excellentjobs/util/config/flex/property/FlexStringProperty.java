package su.nightexpress.excellentjobs.util.config.flex.property;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.util.config.flex.FlexCodecs;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class FlexStringProperty extends FlexProperty<String> {

    public FlexStringProperty(@NonNull String name, String defaultValue, @NonNull String... aliases) {
        super(name, defaultValue, ConfigCodecs.STRING, FlexCodecs.STRING, aliases);
    }

}
