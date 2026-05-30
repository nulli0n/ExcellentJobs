package su.nightexpress.excellentjobs.contract.definition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.codec.ContractUnlockingCodec;
import su.nightexpress.excellentjobs.contract.condition.UnlockCondition;

@NullMarked
public class ContractUnlocking {

    public static final ContractUnlockingCodec CODEC = new ContractUnlockingCodec();

    private final boolean                      permissionRequired;
    private final boolean                      costEnabled;
    private final Map<String, Double>          cost;
    private final boolean                      conditionsEnabled;
    private final Map<String, UnlockCondition> conditionsMap;

    ContractUnlocking(Builder builder) {
        this.permissionRequired = builder.permissionRequired;
        this.costEnabled = builder.costEnabled;
        this.cost = builder.cost;
        this.conditionsEnabled = builder.conditionsEnabled;
        this.conditionsMap = builder.conditionsMap;
    }

    public static ContractUnlocking defaults() {
        return new ContractUnlocking.Builder().build();
    }

    public boolean isPermissionRequired() {
        return this.permissionRequired;
    }

    public boolean isCostEnabled() {
        return this.costEnabled;
    }

    public Map<String, Double> getCost() {
        return this.cost;
    }

    public boolean isConditionsEnabled() {
        return this.conditionsEnabled;
    }

    public Map<String, UnlockCondition> getConditionsMap() {
        return this.conditionsMap;
    }

    public Set<UnlockCondition> getConditions() {
        return Set.copyOf(this.conditionsMap.values());
    }

    public static class Builder {

        private boolean                      permissionRequired;
        private boolean                      costEnabled;
        private Map<String, Double>          cost          = new HashMap<>();
        private boolean                      conditionsEnabled;
        private Map<String, UnlockCondition> conditionsMap = new HashMap<>();

        public Builder setPermissionRequired(boolean permissionRequired) {
            this.permissionRequired = permissionRequired;
            return this;
        }

        public Builder setCostEnabled(boolean costEnabled) {
            this.costEnabled = costEnabled;
            return this;
        }

        public Builder setCost(Map<String, Double> cost) {
            this.cost = cost;
            return this;
        }

        public Builder setConditionsEnabled(boolean conditionsEnabled) {
            this.conditionsEnabled = conditionsEnabled;
            return this;
        }

        public Builder setConditionsMap(Map<String, UnlockCondition> conditionsMap) {
            this.conditionsMap = conditionsMap;
            return this;
        }

        public ContractUnlocking build() {
            return new ContractUnlocking(this);
        }
    }
}
