package su.nightexpress.excellentjobs.zone.requirement.common;

import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Placeholders;

@NullMarked
public class AllowedJobRequirement implements ZoneRequirement {

    public static final Codec CODEC = new Codec();

    private final Set<String> jobIds;

    public AllowedJobRequirement(Set<String> jobIds) {
        this.jobIds = jobIds;
    }

    @Override
    public boolean test(Player player, Job job) {
        return this.jobIds.contains(Placeholders.WILDCARD) || this.jobIds.contains(job.getId());
    }

    public static class Codec implements ConfigCodec<AllowedJobRequirement> {

        @Override
        public AllowedJobRequirement read(FileConfig config, String path) throws CodecReadException {
            Set<String> jobIds = config.getOrSet(path + ".allowed-job-ids", ConfigCodecs.STRING_SET_LOWER_CASE, Set.of(
                Placeholders.WILDCARD));

            return new AllowedJobRequirement(jobIds);
        }

        @Override
        public void write(FileConfig config, String path, AllowedJobRequirement value) {
            config.set(path + ".allowed-job-ids", value.jobIds);
        }
    }
}
