package su.nightexpress.excellentjobs.api.zone;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public interface ZoneRequirement {

    boolean test(Player player, Job job);
}
