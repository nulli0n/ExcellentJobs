package su.nightexpress.excellentjobs.zone.requirement;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class RequirementRegistry {

    private static @Nullable RequirementRegistry instance;

    public static void init(RequirementRegistry registry) {
        if (instance != null) throw new IllegalStateException("Registry is already initialized!");

        RequirementRegistry.instance = registry;
    }

    public static RequirementRegistry access() {
        if (instance == null) {
            throw new IllegalStateException("Registry is not initialized!");
        }
        return instance;
    }

    public static void shutdown() {
        instance = null;
    }


    private final Map<String, Class<? extends ZoneRequirement>> requirements;

    public RequirementRegistry() {
        this.requirements = new HashMap<>();
    }

    public <T extends ZoneRequirement> void register(String name, Class<T> requirement) {
        this.requirements.put(LowerCase.INTERNAL.apply(name), requirement);
    }

    public @Nullable Class<? extends ZoneRequirement> getRequirementType(String name) {
        return this.requirements.get(LowerCase.INTERNAL.apply(name));
    }
}
