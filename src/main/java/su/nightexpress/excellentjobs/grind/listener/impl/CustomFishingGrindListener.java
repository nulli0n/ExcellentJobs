package su.nightexpress.excellentjobs.grind.listener.impl;

import net.momirealms.customfishing.api.event.FishingResultEvent;
import net.momirealms.customfishing.api.mechanic.loot.Loot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;
import su.nightexpress.excellentjobs.grind.table.impl.BasicItemGrindTable;
import su.nightexpress.excellentjobs.grind.type.impl.CustomFishingGrindType;

public class CustomFishingGrindListener extends GrindListener<BasicItemGrindTable, CustomFishingGrindType> {

    public CustomFishingGrindListener(@NotNull JobsPlugin plugin, @NotNull GrindManager grindManager, @NotNull CustomFishingGrindType grindType) {
        super(plugin, grindManager, grindType);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindFishing(FishingResultEvent event) {
        if (event.getResult() != FishingResultEvent.Result.SUCCESS) return;

        EquipmentSlot hand = EquipmentSlot.HAND;

        Player player = event.getPlayer();
        if (!this.grindManager.canGrinding(player)) return;

        ItemStack tool = player.getInventory().getItem(hand);

        Loot loot = event.getLoot();

        this.giveXP(player, tool, (job, table) -> table.getItemXP(loot.id()));
    }
}
