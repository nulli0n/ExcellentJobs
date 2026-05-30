package su.nightexpress.excellentjobs.zone.model;

import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.excellentjobs.zone.codec.ZoneBehaviorCodec;
import su.nightexpress.excellentjobs.zone.requirement.RequirementMode;

public class ZoneBehavior {

    public static final ZoneBehaviorCodec CODEC = new ZoneBehaviorCodec();

    private final boolean                             pvpAllowed;
    private final Set<Material>                       disabledInteractions;
    private final RequirementMode                     requirementMode;
    private final Map<String, ZoneRequirement>        requirementMap;
    private final Map<String, BlockList>              blockListMap;
    private final Map<GrindObjectiveProperty, Double> grindMultipliers;

    public ZoneBehavior(boolean pvpAllowed,
                        Set<Material> disabledInteractions,
                        RequirementMode requirementMode,
                        Map<String, ZoneRequirement> requirementMap,
                        Map<String, BlockList> blockListMap,
                        Map<GrindObjectiveProperty, Double> grindMultipliers) {
        this.pvpAllowed = pvpAllowed;
        this.disabledInteractions = disabledInteractions;
        this.requirementMode = requirementMode;
        this.requirementMap = requirementMap;
        this.blockListMap = blockListMap;
        this.grindMultipliers = grindMultipliers;
    }

    public static ZoneBehavior defaults() {
        return new ZoneBehavior(false, Set.of(), RequirementMode.ANY_JOB, Map.of(), Map.of(), Map.of());
    }

    public boolean isDisabledInteraction(Material material) {
        return this.disabledInteractions.contains(material);
    }

    public double getGrindMultiplier(GrindObjectiveProperty property) {
        return this.grindMultipliers.getOrDefault(property, 1D);
    }

    public boolean isPvpAllowed() {
        return pvpAllowed;
    }

    public Set<Material> getDisabledInteractions() {
        return disabledInteractions;
    }

    public Map<String, ZoneRequirement> getRequirementMap() {
        return requirementMap;
    }

    public RequirementMode getRequirementMode() {
        return requirementMode;
    }

    public Set<ZoneRequirement> getRequirements() {
        return Set.copyOf(this.requirementMap.values());
    }

    public Map<String, BlockList> getBlockListMap() {
        return blockListMap;
    }

    public Set<BlockList> getBlockLists() {
        return Set.copyOf(this.blockListMap.values());
    }

    public @Nullable BlockList getBlockList(String id) {
        return this.blockListMap.get(id.toLowerCase());
    }

    public @Nullable BlockList getBlockList(Material material) {
        return this.getBlockLists().stream().filter(blockList -> blockList.contains(material)).findFirst().orElse(null);
    }

    public Map<GrindObjectiveProperty, Double> getGrindMultipliers() {
        return grindMultipliers;
    }
}
