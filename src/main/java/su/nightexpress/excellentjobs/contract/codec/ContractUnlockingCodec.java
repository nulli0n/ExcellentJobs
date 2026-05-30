package su.nightexpress.excellentjobs.contract.codec;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.condition.ConditionRegistry;
import su.nightexpress.excellentjobs.contract.condition.UnlockCondition;
import su.nightexpress.excellentjobs.contract.definition.ContractUnlocking;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class ContractUnlockingCodec implements ConfigCodec<ContractUnlocking> {

    @Override
    public ContractUnlocking read(FileConfig config, String path) throws CodecReadException {
        boolean permissionRequired = config.getOrSet(path + ".Permission-Required", ConfigCodecs.BOOLEAN, false);

        boolean costEnabled = config.getOrSet(path + ".Cost.Enabled", ConfigCodecs.BOOLEAN, false);
        Map<String, Double> cost = config.getOrSet(path + ".Cost.Currencies",
            ConfigCodecs.forMapWithLowerKeys(ConfigCodecs.DOUBLE),
            Map.of());

        String conditionsPath = path + ".Conditions";
        boolean conditionsEnabled = config.getOrSet(conditionsPath + ".Enabled", ConfigCodecs.BOOLEAN, false);
        Map<String, UnlockCondition> conditionsMap = new HashMap<>();

        config.getSection(conditionsPath + ".List").forEach(conditionId -> {
            Class<? extends UnlockCondition> condType = ConditionRegistry.access().getTypeByAlias(conditionId);
            if (condType == null) return;

            UnlockCondition condition = config.get(conditionsPath + ".List." + conditionId, condType);
            if (condition == null) return;

            conditionsMap.put(conditionId, condition);
        });

        return new ContractUnlocking.Builder()
            .setPermissionRequired(permissionRequired)
            .setCostEnabled(costEnabled)
            .setCost(cost)
            .setConditionsEnabled(conditionsEnabled)
            .setConditionsMap(conditionsMap)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, ContractUnlocking unlocking) {
        config.set(path + ".Permission-Required", unlocking.isPermissionRequired());

        config.set(path + ".Cost.Enabled", unlocking.isCostEnabled());
        config.remove(path + ".Cost.Currencies");
        unlocking.getCost().forEach((key, amount) -> config.set(path + ".Cost.Currencies." + key, amount));

        config.set(path + ".Conditions.Enabled", unlocking.isConditionsEnabled());
        config.remove(path + ".Conditions.List");
        unlocking.getConditionsMap().forEach((id, condition) -> {
            config.set(path + ".Conditions.List." + id, condition);
        });
    }
}
