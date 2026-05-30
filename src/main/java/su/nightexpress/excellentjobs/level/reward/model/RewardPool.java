package su.nightexpress.excellentjobs.level.reward.model;

import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.leveling.Reward;
import su.nightexpress.excellentjobs.api.leveling.RewardProvider;
import su.nightexpress.excellentjobs.level.reward.codec.RewardPoolCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;

@NullMarked
public class RewardPool {

    public static final ConfigCodec<RewardPool> CODEC = new RewardPoolCodec();

    private final int                          priority;
    private final Map<Integer, RewardProvider> rewardByLevelMap;

    public RewardPool(int priority, Map<Integer, RewardProvider> rewardByLevel) {
        this.priority = priority;
        this.rewardByLevelMap = rewardByLevel;
    }

    public boolean hasReward(int level) {
        return this.rewardByLevelMap.containsKey(level);
    }

    public @Nullable Reward getReward(int level) {
        RewardProvider provider = this.rewardByLevelMap.get(level);
        return provider == null ? null : provider.get(level);
    }

    public int getPriority() {
        return priority;
    }

    public Map<Integer, RewardProvider> getRewardByLevelMap() {
        return rewardByLevelMap;
    }
}
