package su.nightexpress.excellentjobs.zone.codec;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.excellentjobs.zone.model.BlockList;
import su.nightexpress.excellentjobs.zone.model.ZoneBehavior;
import su.nightexpress.excellentjobs.zone.requirement.RequirementMode;
import su.nightexpress.excellentjobs.zone.requirement.RequirementRegistry;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class ZoneBehaviorCodec implements ConfigCodec<ZoneBehavior> {

    @Override
    public ZoneBehavior read(FileConfig config, String path) throws CodecReadException {
        boolean pvpAllowed = config.getOrSet(path + ".PvP-Allowed", ConfigCodecs.BOOLEAN, false);

        Set<Material> disabledInteractions = config.getOrSet(path + ".Disabled-Block-Interactions",
            ConfigCodecs.MATERIAL_SET, Set.of());

        RequirementMode requirementMode = config.getOrSet(path + ".Requirements.Mode",
            ConfigCodecs.forEnum(RequirementMode.class), RequirementMode.ANY_JOB);

        Map<String, ZoneRequirement> requirements = new HashMap<>();
        config.getSection(path + ".Requirements.List").forEach(sId -> {
            Class<? extends ZoneRequirement> reqClass = RequirementRegistry.access().getRequirementType(sId);
            if (reqClass == null) {
                // TODO Log "Unknown requirement type: " + sId;
                return;
            }

            ZoneRequirement requirement = config.get(path + ".Requirements.List." + sId, reqClass);
            requirements.put(sId, requirement);
        });

        Map<String, BlockList> blockListMap = new HashMap<>();
        config.getSection(path + ".Grind.Renewable-Blocks").forEach(sId -> {
            BlockList blockList = config.get(path + ".Grind.Renewable-Blocks." + sId, BlockList.CODEC);
            if (blockList == null) return;

            blockListMap.put(sId, blockList);
        });

        Map<GrindObjectiveProperty, Double> grindMultipliers = new EnumMap<>(GrindObjectiveProperty.class);
        for (GrindObjectiveProperty property : GrindObjectiveProperty.values()) {
            double multiplier = config.getOrSet(path + ".Grind.Multipliers." + property.name(), ConfigCodecs.DOUBLE,
                1D);
            grindMultipliers.put(property, multiplier);
        }

        return new ZoneBehavior(pvpAllowed, disabledInteractions, requirementMode, requirements, blockListMap, grindMultipliers);
    }

    @Override
    public void write(FileConfig config, String path, ZoneBehavior value) {
        config.set(path + ".PvP-Allowed", value.isPvpAllowed());
        config.set(path + ".Disabled-Block-Interactions", ConfigCodecs.MATERIAL_SET, value.getDisabledInteractions());

        config.set(path + ".Requirements.Mode", value.getRequirementMode());
        config.remove(path + ".Requirements.List");
        value.getRequirementMap().forEach((id, requirement) -> config.set(path + ".Requirements.List." + id,
            requirement));

        config.remove(path + ".Grind.Renewable-Blocks");
        value.getBlockListMap().forEach((id, blockList) -> config.set(path + ".Grind.Renewable-Blocks." + id,
            blockList));

        config.remove(path + ".Grind.Multipliers");
        value.getGrindMultipliers().forEach((property, multiplier) -> {
            config.set(path + ".Grind.Multipliers." + property.name(), multiplier);
        });
    }


}
