package su.nightexpress.excellentjobs.util.config.flex.codec;

import java.util.function.Supplier;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.util.config.flex.FlexConfigObject;
import su.nightexpress.excellentjobs.util.config.flex.FlexProperty;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class FlexConfigCodec<T extends FlexConfigObject> implements ConfigCodec<T> {

    private static final String DEFAULT_PROPERTY_DELIMITER = ";";
    private static final String DEFAULT_VALUE_DELIMITER    = " ";
    private static final String DEFAULT_VALUE_REGEX        = "\\s+";

    private final Supplier<T> constructor;

    public FlexConfigCodec(Supplier<T> constructor) {
        this.constructor = constructor;
    }

    public static <T extends FlexConfigObject> FlexConfigCodec<T> create(Supplier<T> constructor) {
        return new FlexConfigCodec<>(constructor);
    }

    @Override
    public T read(FileConfig config, String path) throws CodecReadException {
        T object = this.constructor.get();

        String miniString = config.getString(path);
        if (miniString == null) {
            object.setMinimized(false);
            this.parseStandard(object, config, path);
        }
        else {
            object.setMinimized(true);
            this.parseMinimized(object, miniString);
        }

        return object;
    }

    @Override
    public void write(FileConfig config, String path, @NonNull T value) {
        if (value.isMinimized()) {
            this.writeMinimized(value, config, path);
        }
        else {
            this.writeStandard(value, config, path);
        }
    }

    private void parseStandard(T object, FileConfig config, String path) {
        object.getProperties().forEach(property -> {
            String propertyPath = path + "." + property.getName();
            this.parseStandardProperty(object, config, propertyPath, property);
        });
    }

    private <E> void parseStandardProperty(T object,
                                           FileConfig config,
                                           String path,
                                           FlexProperty<E> property) {
        E value = config.getOrSet(path, property.getStandardType(), property.getDefaultValue());
        object.setProperty(property, value);
    }

    private void parseMinimized(T object, String miniString) {
        String[] pairs = miniString.split(DEFAULT_PROPERTY_DELIMITER);

        for (String pair : pairs) {
            this.parseMinimizedPropertyPair(object, pair);
        }
    }

    private void parseMinimizedPropertyPair(T object, String pair) {
        String[] parts = pair.trim().split(DEFAULT_VALUE_REGEX);
        if (parts.length != 2) return;

        String key = parts[0];
        String value = parts[1];

        FlexProperty<?> property = object.getProperty(key);
        if (property == null) return; // TODO Log warning

        this.parseMinimizedProperty(object, property, value);
    }

    private <E> void parseMinimizedProperty(T object, FlexProperty<E> property, String rawValue) {
        try {
            E parsed = property.getMinimizedType().deserialize(rawValue); // Number parsers can throw exceptions.
            if (parsed != null) {
                object.setProperty(property, parsed);
            }
        }
        catch (Exception exception) {
            object.setProperty(property, property.getDefaultValue());
        }
    }

    private void writeStandard(T object, FileConfig config, String path) {
        object.getProperties().forEach(property -> {
            String propertyPath = path + "." + property.getName();
            this.writeStandardProperty(object, config, propertyPath, property);
        });
    }

    private <E> void writeStandardProperty(T object,
                                           FileConfig config,
                                           String path,
                                           FlexProperty<E> property) {
        E value = object.get(property);
        property.getStandardType().write(config, path, value);
    }

    private void writeMinimized(T object, FileConfig config, String path) {
        StringBuilder builder = new StringBuilder();

        object.getProperties().forEach(property -> {
            String key = property.getName();
            String rawValue = this.serializeMinimizedProperty(object, property);

            builder
                .append(key).append(DEFAULT_VALUE_DELIMITER).append(rawValue)
                .append(DEFAULT_VALUE_DELIMITER)
                .append(DEFAULT_PROPERTY_DELIMITER)
                .append(DEFAULT_VALUE_DELIMITER);
        });

        config.set(path, builder.toString());
    }

    private <E> String serializeMinimizedProperty(T object, FlexProperty<E> property) {
        E value = object.get(property);
        return property.getMinimizedType().serialize(value);
    }
}
