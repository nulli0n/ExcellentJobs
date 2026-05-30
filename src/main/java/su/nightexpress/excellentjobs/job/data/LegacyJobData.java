package su.nightexpress.excellentjobs.job.data;

import java.util.HashSet;
import java.util.Set;

public class LegacyJobData {

    private final Set<Integer> claimedLevelRewards;

    private final boolean inactive;
    private final int     level;
    private final int     xp;
    private final long    cooldown;

    public LegacyJobData(boolean inactive,
                         int level,
                         int xp,
                         long cooldown,
                         Set<Integer> claimedLevelRewards) {
        this.inactive = inactive;
        this.cooldown = cooldown;
        this.claimedLevelRewards = new HashSet<>(claimedLevelRewards);
        this.level = level;
        this.xp = xp;
    }

    public boolean isInactive() {
        return this.inactive;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public int getLevel() {
        return this.level;
    }

    public int getXP() {
        return this.xp;
    }

    public Set<Integer> getClaimedLevelRewards() {
        return this.claimedLevelRewards;
    }
}
