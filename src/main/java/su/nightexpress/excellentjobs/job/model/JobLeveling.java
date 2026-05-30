package su.nightexpress.excellentjobs.job.model;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.codec.JobLevelingCodec;

@NullMarked
public class JobLeveling {

    public static final JobLevelingCodec CODEC = new JobLevelingCodec();

    private int         maxLevel;
    private Set<String> rewardPoolIds;

    public JobLeveling(int maxLevel, Set<String> rewardPoolIds) {
        this.maxLevel = maxLevel;
        this.rewardPoolIds = rewardPoolIds;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Set<String> getRewardPoolIds() {
        return rewardPoolIds;
    }
}
