package su.nightexpress.excellentjobs.util.config.flex;

import java.util.Set;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.util.Lists;

public class FlexProperty<T> {

    private final String               name;
    private final Set<String>          aliases;
    private final T                    defaultValue;
    private final ConfigCodec<T>       standardType;
    private final FlexPropertyCodec<T> minimizedType;

    public FlexProperty(@NonNull String name,
                        @NonNull T defaultValue,
                        @NonNull ConfigCodec<T> standardType,
                        @NonNull FlexPropertyCodec<T> minimizedType,
                        @NonNull String... aliases) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.standardType = standardType;
        this.minimizedType = minimizedType;
        this.aliases = Lists.newSet(aliases);
        this.aliases.add(name);
    }

    public boolean matches(@NonNull String key) {
        return this.aliases.stream().anyMatch(alias -> alias.equalsIgnoreCase(key));
    }

    public @NonNull String getName() {
        return this.name;
    }

    public @NonNull Set<String> getAliases() {
        return this.aliases;
    }

    public @NonNull T getDefaultValue() {
        return this.defaultValue;
    }

    public @NonNull ConfigCodec<T> getStandardType() {
        return this.standardType;
    }

    public @NonNull FlexPropertyCodec<T> getMinimizedType() {
        return this.minimizedType;
    }
}
