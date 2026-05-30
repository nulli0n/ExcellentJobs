package su.nightexpress.excellentjobs.job.codec;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.JobLeveling;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class JobLevelingCodec implements ConfigCodec<JobLeveling> {

    @Override
    public JobLeveling read(FileConfig config, String path) throws CodecReadException {
        int maxLevel = config.getOrSet(path + ".Max_Level", ConfigCodecs.INT, 100);
        Set<String> rewardPoolIds = config.getOrSet(path + ".Reward-Pool-Ids", ConfigCodecs.STRING_SET, Set.of());

        return new JobLeveling(maxLevel, rewardPoolIds);
    }

    @Override
    public void write(FileConfig config, String path, JobLeveling value) {
        config.set(path + ".Max-Level", value.getMaxLevel());
        config.set(path + ".Reward-Pool-Ids", value.getRewardPoolIds());
    }
}
