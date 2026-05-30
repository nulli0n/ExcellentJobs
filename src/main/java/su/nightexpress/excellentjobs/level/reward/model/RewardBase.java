package su.nightexpress.excellentjobs.level.reward.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.level.reward.codec.RewardBaseCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;

@NullMarked
public class RewardBase {

    public static final ConfigCodec<RewardBase> CODEC = new RewardBaseCodec();

    private final String                         name;
    private final List<String>                   description;
    private final List<String>                   commands;
    private final Map<String, RewardRequirement> requirementMap;

    public RewardBase(String name,
                      List<String> description,
                      List<String> commands,
                      Map<String, RewardRequirement> requirements) {
        this.name = name;
        this.description = description;
        this.commands = commands;
        this.requirementMap = requirements;
    }


    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<String> getCommands() {
        return commands;
    }

    public Map<String, RewardRequirement> getRequirementMap() {
        return requirementMap;
    }

    public Set<RewardRequirement> getRequirements() {
        return Set.copyOf(this.requirementMap.values());
    }
}
