package su.nightexpress.excellentjobs.contract.condition;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
@FunctionalInterface
public interface UnlockCondition {

    boolean check(Player player, Contract contract, Job job);
}
