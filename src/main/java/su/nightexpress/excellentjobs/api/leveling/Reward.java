package su.nightexpress.excellentjobs.api.leveling;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;

@NullMarked
public interface Reward extends PlaceholderResolvable {

    String getName();

    List<String> getDescription();

    Set<RewardRequirement> getRequirements();

    List<String> getCommands();

    boolean isAvailable(Player player, Job job);

    void give(Player player);
}
