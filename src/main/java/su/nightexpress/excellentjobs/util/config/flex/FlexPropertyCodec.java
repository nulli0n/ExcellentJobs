package su.nightexpress.excellentjobs.util.config.flex;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface FlexPropertyCodec<T> {

    public static <T> @NonNull FlexPropertyCodec<T> of(@NonNull Function<String, @Nullable T> deserialize,
                                                       @NonNull Function<T, String> serialize) {
        return new FlexPropertyCodec<>() {

            @Override
            public @Nullable T deserialize(@NonNull String string) {
                return deserialize.apply(string);
            }

            @Override
            public @NonNull String serialize(@NonNull T value) {
                return serialize.apply(value);
            }
        };
    }

    @Nullable
    T deserialize(@NonNull String string);

    @NonNull
    String serialize(@NonNull T value);
}
