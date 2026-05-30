package su.nightexpress.excellentjobs.grind.visual;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.nightcore.manager.AbstractListener;

@NullMarked
public class GrindBarListener extends AbstractListener<JobsPlugin> {

    private final GrindBarManager barManager;

    public GrindBarListener(JobsPlugin plugin, GrindBarManager barManager) {
        super(plugin);
        this.barManager = barManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.barManager.handlePlayerQuit(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGrindReward(GrindRewardProceedEvent event) {
        this.barManager.handleGrindRewardEvent(event);
    }
}
