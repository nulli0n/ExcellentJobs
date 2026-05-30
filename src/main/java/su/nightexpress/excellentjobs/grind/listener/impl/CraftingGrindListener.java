package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class CraftingGrindListener extends GrindListener<ItemStack> {

    public CraftingGrindListener(JobsPlugin plugin,
                                 GrindManager manager,
                                 @Nullable GrindProtection protection,
                                 GrindType<ItemStack> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindCrafting(CraftItemEvent event) {
        if (event.getClick() == ClickType.MIDDLE) return;

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().isAir()) return;

        Player player = (Player) event.getWhoClicked();
        if (this.protection != null && !this.protection.isGrindAllowed(player)) return;

        ItemStack craftedItem = new ItemStack(itemStack);
        int unitSize = craftedItem.getAmount();

        if (event.isShiftClick()) {
            int has = Players.countItem(player, craftedItem);
            this.plugin.runTask(() -> {
                int now = Players.countItem(player, craftedItem);
                int crafted = now - has;

                this.giveXP(player, craftedItem, GrindContext.create(null, crafted));
            });
            return;
        }

        ItemStack cursor = event.getCursor();
        if (cursor.getType().isAir()) {
            this.giveXP(player, craftedItem, GrindContext.create(null, unitSize));
            return;
        }

        if (!craftedItem.isSimilar(cursor) || cursor.getAmount() + unitSize > cursor.getMaxStackSize()) {
            return;
        }

        this.giveXP(player, craftedItem, GrindContext.create(null, unitSize));
    }
}
