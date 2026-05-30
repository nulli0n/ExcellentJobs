package su.nightexpress.excellentjobs.util.config.flex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class FlexConfigObject {

    private final Map<FlexProperty<?>, Object> propertyValues;
    private final List<FlexProperty<?>>        properties;

    private boolean minimized;

    protected FlexConfigObject() {
        this.propertyValues = new HashMap<>();
        this.properties = new ArrayList<>();
    }

    protected <T> void register(FlexProperty<T> property) {
        this.properties.add(property);
        this.propertyValues.put(property, property.getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(FlexProperty<T> property) {
        return (T) this.propertyValues.getOrDefault(property, property.getDefaultValue());
    }

    public @Nullable FlexProperty<?> getProperty(String key) {
        for (FlexProperty<?> property : this.properties) {
            if (property.matches(key)) return property;
        }

        return null;
    }

    public <T> void setProperty(FlexProperty<T> property, T value) {
        this.propertyValues.put(property, value);
    }

    public boolean isMinimized() {
        return this.minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public List<FlexProperty<?>> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "FlexConfigObject [propertyValues=" + propertyValues + "]";
    }
}
