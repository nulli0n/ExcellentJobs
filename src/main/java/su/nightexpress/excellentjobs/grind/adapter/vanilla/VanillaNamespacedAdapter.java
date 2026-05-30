package su.nightexpress.excellentjobs.grind.adapter.vanilla;

import java.util.function.Function;

import org.bukkit.Keyed;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindAdapter;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bridge.RegistryType;

@NullMarked
public class VanillaNamespacedAdapter<K extends Keyed, B> implements GrindAdapter<B> {

    private final String          name;
    private final RegistryType<K> registry;
    private final Function<B, K>  keyFunction;

    public VanillaNamespacedAdapter(String name, RegistryType<K> registry, Function<B, K> keyFunction) {
        this.name = name;
        this.registry = registry;
        this.keyFunction = keyFunction;
    }

    @Override
    public boolean isCaseSensetive() {
        return false;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public boolean canHandle(String serializedName) {
        return BukkitThing.getByString(this.registry, serializedName) != null;
    }

    @Override
    public boolean canHandle(@NonNull B bukkit) {
        return true;
    }

    @Override
    public @Nullable String serializeFromBukkit(@NonNull B entity) {
        K key = this.keyFunction.apply(entity);

        return BukkitThing.getAsString(key);
    }

    @Override
    public String serializeFromName(String internalName) {
        K key = BukkitThing.getByString(this.registry, internalName);
        if (key == null) return internalName;

        return BukkitThing.getAsString(key);
    }
}
