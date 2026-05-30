package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class GrindstoneGrindListener extends GrindListener<ItemStack> {

    public GrindstoneGrindListener(JobsPlugin plugin,
                                   GrindManager manager,
                                   @Nullable GrindProtection protection,
                                   GrindType<ItemStack> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onXPInventoryHandler(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof GrindstoneInventory inventory)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!this.checkProtection(p -> p.isGrindAllowed(player))) return;

        if (event.getRawSlot() != 2 || event.getClick() == ClickType.MIDDLE) return;

        ItemStack result = inventory.getItem(2);
        if (result == null || result.getType().isAir()) return;

        ItemStack source = inventory.getItem(0);
        if (source == null || result.getType().isAir()) return;

        if (source.getEnchantments().size() == result.getEnchantments().size()) return;

        this.giveXP(player, result, GrindContext.create());
    }
}
