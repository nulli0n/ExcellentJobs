package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.AnvilView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class ForgingGrindListener extends GrindListener<ItemStack> {

    public ForgingGrindListener(JobsPlugin plugin,
                                GrindManager manager,
                                @Nullable GrindProtection protection,
                                GrindType<ItemStack> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnvilClick(InventoryClickEvent event) {
        if (!(event.getView() instanceof AnvilView anvil)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getRawSlot() != 2 || event.getClick() == ClickType.MIDDLE) return;
        if (!this.checkProtection(p -> p.isGrindAllowed(player))) return;

        if (anvil.getRepairCost() <= 0) return;

        ItemStack first = anvil.getItem(0);
        if (first == null || first.getType().isAir()) return;

        ItemStack result = anvil.getItem(2);
        if (result == null || result.getType().isAir()) return;

        if (first.getType() != result.getType() || result.isSimilar(first)) return;

        ItemStack resultCopy = new ItemStack(result);

        this.plugin.runTask(() -> {
            ItemStack updated = anvil.getItem(2);
            if (updated != null && !updated.getType().isAir()) return;

            this.giveXP(player, resultCopy, GrindContext.create());
        });
    }
}
