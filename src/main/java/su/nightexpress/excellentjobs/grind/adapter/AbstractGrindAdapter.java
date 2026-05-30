package su.nightexpress.excellentjobs.grind.adapter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindAdapter;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public abstract class AbstractGrindAdapter<R, B> implements GrindAdapter<B> {

    protected static final String KEY_DELIMITER = ":";

    protected final String name;
    protected final String namespace;

    protected AbstractGrindAdapter(String name, String key) {
        this.name = LowerCase.INTERNAL.apply(name);
        this.namespace = LowerCase.INTERNAL.apply(key);
    }

    @Override
    public boolean isCaseSensetive() {
        return false;
    }

    @Override
    public String getId() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    private String adaptToLowerCase(String name) {
        return this.isCaseSensetive() ? name : LowerCase.INTERNAL.apply(name);
    }

    @Override
    public boolean canHandle(String serializedName) {
        String[] split = serializedName.split(KEY_DELIMITER);
        if (split.length < 2) {
            String internalName = this.adaptToLowerCase(split[0]);
            return this.adaptFromName(internalName) != null;
        }

        String parsedKey = split[0];
        if (!this.namespace.equalsIgnoreCase(parsedKey)) return false;

        String internalName = this.adaptToLowerCase(split[1]);
        return this.adaptFromName(internalName) != null;
    }


    public abstract String getInternalName(@NonNull R type);

    public abstract @Nullable R adaptFromName(String internalName);

    public abstract @Nullable R adaptFromBukkit(@NonNull B entity);

    public @Nullable String serializeFromBukkit(@NonNull B bukkit) {
        R real = this.adaptFromBukkit(bukkit);
        if (real == null) return null;

        return this.serializeFromType(real);
    }

    public String serializeFromType(@NonNull R real) {
        String internalName = this.getInternalName(real);

        return this.namespace + KEY_DELIMITER + internalName;
    }

    public String serializeFromName(String internalName) {
        String adaptedName = this.adaptToLowerCase(internalName);

        R real = this.adaptFromName(adaptedName);
        return real == null ? adaptedName : this.serializeFromType(real);
    }
}
