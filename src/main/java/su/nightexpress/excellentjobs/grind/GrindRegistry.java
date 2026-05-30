package su.nightexpress.excellentjobs.grind;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindAdapter;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.nightcore.util.registry.StringRegistry;

@NullMarked
public class GrindRegistry {

    private final StringRegistry<GrindAdapter<?>> adapterRegistry;
    private final StringRegistry<GrindType<?>>    typeRegistry;
    private final StringRegistry<GrindObjective>  objectiveRegistry;

    public GrindRegistry() {
        this.adapterRegistry = new StringRegistry<>();
        this.typeRegistry = new StringRegistry<>();
        this.objectiveRegistry = new StringRegistry<>();
    }

    public void clear() {
        this.adapterRegistry.clear();
        this.typeRegistry.clear();
        this.objectiveRegistry.clear();
    }

    public int countObjectives() {
        return this.objectiveRegistry.size();
    }

    public <A extends GrindAdapter<?>> void registerAdapter(A adapter) {
        this.adapterRegistry.add(adapter.getId(), adapter);
    }

    public <T> void registerType(GrindType<T> grindType) {
        this.typeRegistry.add(grindType.getId(), grindType);
    }

    public void registerObjective(String id, GrindObjective objective) {
        this.objectiveRegistry.add(id, objective);
    }

    public @Nullable GrindType<?> getType(String id) {
        return this.typeRegistry.getByKey(id);
    }

    public @Nullable GrindObjective getObjective(String id) {
        return this.objectiveRegistry.getByKey(id);
    }
}
