package su.nightexpress.excellentjobs.grind.listener.impl;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.nightcore.util.Lists;

@NullMarked
public class BreakBlockGrindListener extends GrindListener<Block> {

    private static final Set<Material> TALL_BLOCKS = Lists.newSet(Material.BAMBOO, Material.SUGAR_CANE);

    public BreakBlockGrindListener(JobsPlugin plugin,
                                   GrindManager manager,
                                   @Nullable GrindProtection protection,
                                   GrindType<Block> type) {
        super(plugin, manager, protection, type);
    }

    private boolean isTallBlock(Material material) {
        return TALL_BLOCKS.contains(material);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGrindMining(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        this.giveBlockXP(player, tool, block);
    }

    private void giveBlockXP(Player player, @Nullable ItemStack tool, Block block) {
        if (!this.checkProtection(p -> p.isGrindAllowed(player) && !p.isArtificalBlock(block))) return;

        BlockData blockData = block.getBlockData();
        boolean isTall = this.isTallBlock(block.getType());

        // Do not give XP for ungrowth plants.
        if (!isTall && blockData instanceof Ageable age && age.getAge() < age.getMaximumAge()) {
            return;
        }

        this.giveXP(player, block, GrindContext.create(tool));

        if (isTall) {
            Block above = block.getRelative(BlockFace.UP);
            if (above.getType() == block.getType()) {
                this.giveBlockXP(player, tool, above);
            }
        }
    }
}
