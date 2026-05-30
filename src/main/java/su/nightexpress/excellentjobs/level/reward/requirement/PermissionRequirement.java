package su.nightexpress.excellentjobs.level.reward.requirement;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class PermissionRequirement implements RewardRequirement {

    public static final PermissionRequirementCodec CODEC = new PermissionRequirementCodec();

    private final String premission;

    public PermissionRequirement(String premission) {
        this.premission = premission;
    }

    @Override
    public boolean test(Player player, Job job) {
        return player.hasPermission(this.premission);
    }

    public static class PermissionRequirementCodec implements ConfigCodec<PermissionRequirement> {

        @Override
        public PermissionRequirement read(FileConfig config, String path) throws CodecReadException {
            String permission = config.getOrSet(path, ConfigCodecs.STRING, "null");
            return new PermissionRequirement(permission);
        }

        @Override
        public void write(FileConfig config, String path, PermissionRequirement value) {
            config.set(path, value.premission);
        }
    }
}
