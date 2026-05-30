package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.context.impl.CookingGrindContext;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.excellentjobs.grind.workstation.Workstation;
import su.nightexpress.excellentjobs.grind.workstation.WorkstationMode;

@NullMarked
public class CookingGrindListener extends GrindListener<ItemStack> {

    public CookingGrindListener(JobsPlugin plugin,
                                GrindManager manager,
                                @Nullable GrindProtection protection,
                                GrindType<ItemStack> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindCooking(BlockCookEvent event) {
        Block block = event.getBlock();
        Workstation workstation = Workstation.getByBlock(block);
        if (workstation == null) return;

        TileState tile = workstation.getBackend();

        Player player = this.manager.getWorkstationOwner(tile);
        if (player == null) return;

        if (this.protection != null && !this.protection.isGrindAllowed(player)) return;

        ItemStack ingredient = event.getSource();
        WorkstationMode mode = this.manager.getWorkstationMode(tile);

        CookingGrindContext context = new CookingGrindContext(1, mode == WorkstationMode.AUTO);

        this.giveXP(player, ingredient, context);
    }
}
