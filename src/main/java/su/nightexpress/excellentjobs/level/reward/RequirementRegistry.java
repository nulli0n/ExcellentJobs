package su.nightexpress.excellentjobs.level.reward;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.nightcore.util.LowerCase;

public class RequirementRegistry {

    private static RequirementRegistry instance;

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


    private final Map<String, Class<? extends RewardRequirement>> requirements;

    public RequirementRegistry() {
        this.requirements = new HashMap<>();
    }

    public <T extends RewardRequirement> void register(String name, Class<T> requirement) {
        this.requirements.put(LowerCase.INTERNAL.apply(name), requirement);
    }

    public @Nullable Class<? extends RewardRequirement> getRequirementType(String name) {
        return this.requirements.get(LowerCase.INTERNAL.apply(name));
    }
}
