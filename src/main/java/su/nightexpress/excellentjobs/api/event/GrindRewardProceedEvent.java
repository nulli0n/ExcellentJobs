package su.nightexpress.excellentjobs.api.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.api.grind.bar.BarElement;
import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.bridge.currency.Currency;

@NullMarked
public class GrindRewardProceedEvent extends GrindObjectiveEvent {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final GrindReward      reward;
    private final Currency         currency;
    private final List<BarElement> barElements;

    public GrindRewardProceedEvent(Player player,
                                   Job job,
                                   JobData jobData,
                                   GrindObjective objective,
                                   GrindReward reward,
                                   Currency currency) {
        super(player, job, jobData, objective);
        this.reward = reward;
        this.currency = currency;
        this.barElements = new ArrayList<>();
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public GrindReward getReward() {
        return this.reward;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public List<BarElement> getBarElements() {
        return this.barElements;
    }
}
