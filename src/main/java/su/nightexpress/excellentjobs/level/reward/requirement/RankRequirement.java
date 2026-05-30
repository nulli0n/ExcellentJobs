package su.nightexpress.excellentjobs.level.reward.requirement;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class RankRequirement implements RewardRequirement {

    public static final RankRequirementCodec CODEC = new RankRequirementCodec();

    private final Set<String> ranks;

    public RankRequirement(Set<String> ranks) {
        this.ranks = ranks;
    }

    @Override
    public boolean test(Player player, Job job) {
        Set<String> playerRanks = Players.getInheritanceGroups(player);

        return this.ranks.stream().anyMatch(playerRanks::contains);
    }

    public static class RankRequirementCodec implements ConfigCodec<RankRequirement> {

        private static final String DELIMITER = ",";

        @Override
        public RankRequirement read(FileConfig config, String path) throws CodecReadException {
            String rawRanks = config.get(path, ConfigCodecs.STRING);
            if (rawRanks == null) return new RankRequirement(Set.of());

            Set<String> ranks = Stream.of(rawRanks.split(DELIMITER)).collect(Collectors.toSet());

            return new RankRequirement(ranks);
        }

        @Override
        public void write(FileConfig config, String path, RankRequirement value) {
            config.set(path, String.join(DELIMITER, value.ranks));
        }
    }
}
