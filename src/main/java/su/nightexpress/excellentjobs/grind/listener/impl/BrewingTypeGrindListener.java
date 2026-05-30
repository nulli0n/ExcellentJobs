package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.context.impl.BrewingGrindContext;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.excellentjobs.grind.workstation.WorkstationMode;

@NullMarked
public class BrewingTypeGrindListener extends GrindListener<PotionType> {

    public BrewingTypeGrindListener(JobsPlugin plugin,
                                    GrindManager manager,
                                    @Nullable GrindProtection protection,
                                    GrindType<PotionType> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindBrewEnd(BrewEvent event) {
        BrewerInventory brewerInventory = event.getContents();
        BrewingStand stand = brewerInventory.getHolder();
        if (stand == null) return;

        ItemStack ingredient = brewerInventory.getIngredient();
        if (ingredient == null || ingredient.getType().isAir()) return;

        Player player = this.manager.getWorkstationOwner(stand);
        if (player == null) return;

        if (this.protection != null && !this.protection.isGrindAllowed(player)) return;

        int potionsAmount = event.getResults().size();
        WorkstationMode mode = this.manager.getWorkstationMode(stand);

        BrewingGrindContext context = new BrewingGrindContext(null, 1, potionsAmount, mode == WorkstationMode.AUTO);

        event.getResults().forEach(result -> {
            if (!(result.getItemMeta() instanceof PotionMeta potionMeta)) return;
            if (!potionMeta.hasBasePotionType()) return;

            PotionType type = potionMeta.getBasePotionType();
            if (type == null) return;

            this.giveXP(player, type, context);
        });
    }
}
