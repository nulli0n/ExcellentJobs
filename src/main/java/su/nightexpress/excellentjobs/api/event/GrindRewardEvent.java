package su.nightexpress.excellentjobs.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.bridge.currency.Currency;

@NullMarked
public class GrindRewardEvent extends GrindObjectiveEvent implements Cancellable {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private @Nullable GrindModifier modifier;
    private GrindReward             reward;
    private Currency                currency;

    private boolean cancelled;

    public GrindRewardEvent(Player player,
                            Job job,
                            JobData data,
                            GrindObjective objective,
                            @Nullable GrindModifier modifier,
                            GrindReward reward,
                            Currency currency) {
        super(player, job, data, objective);
        this.reward = reward;
        this.modifier = modifier;
        this.currency = currency;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public GrindReward getReward() {
        return this.reward;
    }

    public void setReward(GrindReward reward) {
        this.reward = reward;
    }

    public @Nullable GrindModifier getModifier() {
        return this.modifier;
    }

    public void setModifier(@Nullable GrindModifier modifier) {
        this.modifier = modifier;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
