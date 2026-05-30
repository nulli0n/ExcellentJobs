package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.nightcore.util.ItemUtil;

@NullMarked
public class StripBlockGrindListener extends GrindListener<BlockState> {

    public StripBlockGrindListener(JobsPlugin plugin,
                                   GrindManager manager,
                                   @Nullable GrindProtection protection,
                                   GrindType<BlockState> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockStrip(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!this.checkProtection(p -> p.isGrindAllowed(player) && !p.isArtificalBlock(block))) return;

        Material originType = block.getType();
        BlockState originState = block.getState();

        if (!Tag.LOGS.isTagged(originType) || isStripped(originType)) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemStack = event.getItem();
        if (itemStack == null || !ItemUtil.isAxe(itemStack)) return;

        this.plugin.runTask(player, () -> {
            if (!isStripped(block.getType())) return;

            this.giveXP(player, originState, GrindContext.create());
        });
    }

    private static boolean isStripped(Material material) {
        return material.name().startsWith("STRIPPED_");
    }
}
