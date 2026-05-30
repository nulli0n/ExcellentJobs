package su.nightexpress.excellentjobs.level.reward.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.level.reward.RequirementRegistry;
import su.nightexpress.excellentjobs.level.reward.model.RewardBase;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardBaseCodec implements ConfigCodec<RewardBase> {

    @Override
    public RewardBase read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".Name", ConfigCodecs.STRING, "Reward");
        List<String> description = config.getOrSet(path + ".Description", ConfigCodecs.STRING_LIST, List.of());
        List<String> commands = config.getOrSet(path + ".Commands", ConfigCodecs.STRING_LIST, List.of());
        Map<String, RewardRequirement> requirements = new HashMap<>();

        config.getSection(path + ".Requirements").forEach(reqName -> {
            Class<? extends RewardRequirement> reqType = RequirementRegistry.access().getRequirementType(reqName);
            if (reqType == null) return; // TODO Log

            RewardRequirement requirement = config.get(path + ".Requirements." + reqName, reqType);
            if (requirement == null) return;

            requirements.put(reqName, requirement);
        });

        return new RewardBase(name, description, commands, requirements);
    }

    @Override
    public void write(FileConfig config, String path, RewardBase base) {
        config.set(path + ".Name", base.getName());
        config.set(path + ".Description", base.getDescription());
        config.set(path + ".Commands", base.getCommands());

        String reqPath = path + ".Requirements";
        config.remove(reqPath);
        base.getRequirementMap().forEach((reqName, requirement) -> {
            config.writeByCodec(reqPath + "." + reqName, requirement);
        });
    }
}
