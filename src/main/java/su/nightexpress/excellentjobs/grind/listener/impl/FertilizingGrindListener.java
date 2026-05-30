package su.nightexpress.excellentjobs.grind.listener.impl;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class FertilizingGrindListener extends GrindListener<BlockState> {

    public FertilizingGrindListener(JobsPlugin plugin,
                                    GrindManager manager,
                                    @Nullable GrindProtection protection,
                                    GrindType<BlockState> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (this.protection != null && !this.protection.isGrindAllowed(player)) return;

        Set<BlockState> blocks = new HashSet<>();
        blocks.add(event.getBlock().getState());
        blocks.addAll(event.getBlocks());

        blocks.forEach(blockState -> {
            this.giveXP(player, blockState, GrindContext.create());
        });
    }
}
