package su.nightexpress.excellentjobs.level.reward.codec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.RewardProvider;
import su.nightexpress.excellentjobs.level.reward.model.RewardPool;
import su.nightexpress.excellentjobs.level.reward.model.RewardModel;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.ArrayUtil;

@NullMarked
public class RewardPoolCodec implements ConfigCodec<RewardPool> {

    @Override
    public RewardPool read(FileConfig config, String path) throws CodecReadException {
        int priority = config.getOrSet(path + ".Priority", ConfigCodecs.INT, 0);

        Map<Integer, RewardProvider> rewards = new HashMap<>();

        String levelsPath = path + ".Levels";

        config.getSection(levelsPath).forEach(rawLevels -> {
            int[] levels = ArrayUtil.parseIntArray(rawLevels);
            if (levels.length == 0) return;

            RewardModel reward = config.get(levelsPath + "." + rawLevels, RewardModel.class);
            if (reward == null) return;

            for (int level : levels) {
                rewards.put(level, reward);
            }
        });

        return new RewardPool(priority, rewards);
    }

    @Override
    public void write(FileConfig config, String path, RewardPool pool) {
        config.set(path + ".Priority", pool.getPriority());

        String levelsPath = path + ".Levels";
        config.remove(levelsPath);

        Map<RewardProvider, Set<Integer>> rewards = new HashMap<>();

        pool.getRewardByLevelMap().forEach((level, reward) -> {
            rewards.computeIfAbsent(reward, k -> new HashSet<>()).add(level);
        });

        rewards.forEach((reward, levels) -> {
            String key = ArrayUtil.arrayToString(levels.stream().mapToInt(i -> i).toArray());
            config.writeByCodec(levelsPath + "." + key, reward);
        });
    }
}
