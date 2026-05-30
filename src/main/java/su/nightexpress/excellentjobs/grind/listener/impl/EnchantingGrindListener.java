package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.context.impl.EnchantingGrindContext;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class EnchantingGrindListener extends GrindListener<Enchantment> {

    public EnchantingGrindListener(JobsPlugin plugin,
                                   GrindManager manager,
                                   @Nullable GrindProtection protection,
                                   GrindType<Enchantment> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindEnchanting(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if (this.protection != null && !this.protection.isGrindAllowed(player)) return;

        event.getEnchantsToAdd().forEach((enchantment, level) -> {
            this.giveXP(player, enchantment, new EnchantingGrindContext(null, 1, level));
        });
    }
}
