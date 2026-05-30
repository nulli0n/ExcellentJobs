package su.nightexpress.excellentjobs.contract.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.definition.ContractPromotion;
import su.nightexpress.excellentjobs.util.TimeParser;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class ContractPromotionCodec implements ConfigCodec<ContractPromotion> {

    @Override
    public ContractPromotion read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".Enabled", ConfigCodecs.BOOLEAN, true);

        boolean durationEnabled = config.getOrSet(path + ".Duration.Enabled", ConfigCodecs.BOOLEAN, true);
        long durationMillis = 0L;

        if (durationEnabled) {
            String durationRaw = config.getOrSet(path + ".Duration.Value", ConfigCodecs.STRING, "30d");
            durationMillis = TimeParser.parseToMillis(durationRaw);
        }

        boolean cooldownEnabled = config.getOrSet(path + ".Cooldown.Enabled", ConfigCodecs.BOOLEAN, false);
        long cooldownMillis = 0L;

        if (cooldownEnabled) {
            String cooldownRaw = config.getOrSet(path + ".Cooldown.Value", ConfigCodecs.STRING, "30d");
            cooldownMillis = TimeParser.parseToMillis(cooldownRaw);
        }

        int pointsGoal = config.getOrSet(path + ".Goal.Contract-Points", ConfigCodecs.INT, 0);

        List<String> rewardCommands = config.getOrSet(path + ".Reward-Commands", ConfigCodecs.STRING_LIST, List.of());
        List<String> rewardMessages = config.getOrSet(path + ".Reward-Messages", ConfigCodecs.STRING_LIST, List.of());

        return new ContractPromotion.Builder()
            .enabled(enabled)
            .durationEnabled(durationEnabled)
            .durationMillis(durationMillis)
            .cooldownEnabled(cooldownEnabled)
            .cooldownMillis(cooldownMillis)
            .pointsGoal(pointsGoal)
            .rewardCommands(rewardCommands)
            .rewardMessages(rewardMessages)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, ContractPromotion promotion) {
        config.set(path + ".Enabled", promotion.isEnabled());
        config.set(path + ".Duration.Enabled", promotion.isDurationEnabled());
        if (promotion.isDurationEnabled()) {
            config.set(path + ".Duration.Value", TimeParser.formatMillis(promotion.getDurationMillis()));
        }
        config.set(path + ".Cooldown.Enabled", promotion.isCooldownEnabled());
        if (promotion.isCooldownEnabled()) {
            config.set(path + ".Cooldown.Value", TimeParser.formatMillis(promotion.getCooldownMillis()));
        }
        config.set(path + ".Goal.Contract-Points", promotion.getPointsGoal());
        config.set(path + ".Reward-Commands", promotion.getRewardCommands());
        config.set(path + ".Reward-Messages", promotion.getRewardMessages());
    }
}
