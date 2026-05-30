package su.nightexpress.excellentjobs.contract.condition;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class ConditionRegistry {

    @Nullable
    private static ConditionRegistry instance;

    public static ConditionRegistry access() {
        if (instance == null) throw new IllegalStateException("Registry is not bound");
        return instance;
    }

    public static void bind(ConditionRegistry registry) {
        if (instance != null) throw new IllegalStateException("Registry is already bound");
        instance = registry;
    }

    public static void unbind() {
        instance = null;
    }

    private final Map<String, Class<? extends UnlockCondition>> typeAliases;

    public ConditionRegistry() {
        this.typeAliases = new HashMap<>();
    }

    public void clear() {
        this.typeAliases.clear();
    }

    public <T extends UnlockCondition> void register(String id, Class<T> type) {
        this.typeAliases.put(LowerCase.INTERNAL.apply(id), type);
    }

    public @Nullable Class<? extends UnlockCondition> getTypeByAlias(String id) {
        return this.typeAliases.get(LowerCase.INTERNAL.apply(id));
    }
}
