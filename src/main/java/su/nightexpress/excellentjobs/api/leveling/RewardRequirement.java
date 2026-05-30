package su.nightexpress.excellentjobs.api.leveling;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public interface RewardRequirement {

    boolean test(Player player, Job job);
}
