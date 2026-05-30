package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.SmithItemEvent;
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
public class SmithingGrindListener extends GrindListener<ItemStack> {

    public SmithingGrindListener(JobsPlugin plugin,
                                 GrindManager manager,
                                 @Nullable GrindProtection protection,
                                 GrindType<ItemStack> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSmithing(SmithItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!this.checkProtection(p -> p.isGrindAllowed(player))) return;

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().isAir()) return;

        int slot = event.getRawSlot();
        if (slot != 3) return;

        this.giveXP(player, itemStack, GrindContext.create());
    }
}
