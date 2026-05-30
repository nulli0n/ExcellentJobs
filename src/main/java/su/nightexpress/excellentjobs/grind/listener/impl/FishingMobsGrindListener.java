package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
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
public class FishingMobsGrindListener extends GrindListener<Entity> {

    public FishingMobsGrindListener(JobsPlugin plugin,
                                    GrindManager manager,
                                    @Nullable GrindProtection protection,
                                    GrindType<Entity> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrindFishing(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        EquipmentSlot hand = event.getHand();
        if (hand == null) return;

        Player player = event.getPlayer();
        if (this.protection != null && !this.protection.isGrindAllowed(player)) return;

        ItemStack tool = player.getInventory().getItem(hand);

        Entity entity = event.getCaught();
        if (entity == null) return;

        this.giveXP(player, entity, GrindContext.create(tool));
    }
}
