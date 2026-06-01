package su.nightexpress.excellentjobs.level.reward;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.leveling.Reward;
import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class LevelReward implements Reward {

    private final String                 name;
    private final List<String>           description;
    private final List<String>           commands;
    private final Set<RewardRequirement> requirements;

    public LevelReward(String name,
                       List<String> description,
                       List<String> commands,
                       Set<RewardRequirement> requirements) {
        this.name = name;
        this.description = description;
        this.commands = commands;
        this.requirements = requirements;
    }

    @Override
    public @NonNull PlaceholderResolver placeholders() {
        return JobsPlaceholders.LEVEL_REWARD.resolver(this);
    }

    @Override
    public boolean isAvailable(Player player, Job job) {
        if (this.requirements.isEmpty()) return true;

        return this.requirements.stream().allMatch(requirement -> requirement.test(player, job));
    }

    @Override
    public void give(Player player) {
        Players.dispatchCommands(player, this.commands);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getDescription() {
        return this.description;
    }

    @Override
    public List<String> getCommands() {
        return this.commands;
    }

    @Override
    public Set<RewardRequirement> getRequirements() {
        return this.requirements;
    }
}
