package su.nightexpress.excellentjobs.contract.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.event.JobJoinEvent;
import su.nightexpress.excellentjobs.api.event.JobLeaveEvent;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.nightcore.manager.AbstractListener;

public class ContractListener extends AbstractListener<JobsPlugin> {

    private final ContractManager manager;

    public ContractListener(@NonNull JobsPlugin plugin, @NonNull ContractManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJobMenuDefineLayout(JobMenuDefineLayoutEvent event) {
        this.manager.handleJobMenuDefineLayout(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.manager.handlePlayerJoin(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.manager.handlePlayerQuit(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJobJoin(JobJoinEvent event) {
        this.manager.handleJobJoin(event);
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJobQuit(JobLeaveEvent event) {
        this.manager.handleJobQuit(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onGrindRewardPre(GrindRewardEvent event) {
        this.manager.handleGrindRewardPre(event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onGrindRewardPost(GrindRewardProceedEvent event) {
        this.manager.handleGrindRewardPost(event);
    }
}
