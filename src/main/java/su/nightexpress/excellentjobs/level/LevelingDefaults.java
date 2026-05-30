package su.nightexpress.excellentjobs.level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.RewardProvider;
import su.nightexpress.excellentjobs.level.reward.model.RewardBase;
import su.nightexpress.excellentjobs.level.reward.model.RewardPool;
import su.nightexpress.excellentjobs.level.reward.model.RewardModel;
import su.nightexpress.excellentjobs.level.reward.model.RewardValue;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class LevelingDefaults {

    public static final int MAX_LEVEL = 100;

    private LevelingDefaults() {
    }

    public static Map<String, RewardPool> getDefaultRewardPools() {
        Map<String, RewardPool> map = new HashMap<>();

        map.put("basic", createBasicRewardPool());

        return map;
    }

    private static RewardPool createBasicRewardPool() {
        RewardModel moneyReward = createBasicMoneyReward();

        Map<Integer, RewardProvider> rewards = new HashMap<>();
        for (int level = LevelingConstants.START_LEVEL + 1; level < MAX_LEVEL + 1; level++) {
            if (level % 5 == 0) {
                rewards.put(level, moneyReward);
            }
        }

        return new RewardPool(0, rewards);
    }

    private static RewardModel createBasicMoneyReward() {
        String name = "$%amount%";
        List<String> commands = List.of(
            "money give " + CommonPlaceholders.PLAYER_NAME + " %amount_raw%"
        );

        RewardBase base = new RewardBase(name, List.of(), commands, Map.of());

        return new RewardModel(base, Map.of(
            "amount", new RewardValue(500, 50)
        ));
    }
}
