package su.nightexpress.excellentjobs.grind.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.nightcore.manager.AbstractListener;

@NullMarked
public class GrindProtectionListener extends AbstractListener<JobsPlugin> {

    private final GrindProtectionManager manager;

    public GrindProtectionListener(JobsPlugin plugin, GrindProtectionManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onGrindReward(GrindRewardEvent event) {
        this.manager.handleDailyLimits(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindProtectionEntitySpawn(CreatureSpawnEvent event) {
        this.manager.handleCreatureSpawn(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGlitchEntityTransform(EntityTransformEvent event) {
        this.manager.handleEntityTransform(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGlitchBlockFertilize(BlockFertilizeEvent event) {
        this.manager.handleBlockFertilize(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGlitchBlockGeneration(BlockFormEvent event) {
        this.manager.handleBlockForm(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        this.manager.handleBlockBreak(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDrop(BlockDropItemEvent event) {
        this.manager.handleBlockItemDrop(event);
    }
}
