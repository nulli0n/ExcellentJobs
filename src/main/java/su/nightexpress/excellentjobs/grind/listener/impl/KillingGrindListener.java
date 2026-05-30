package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.context.impl.KillingGrindContext;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class KillingGrindListener extends GrindListener<Entity> {

    public KillingGrindListener(JobsPlugin plugin,
                                GrindManager manager,
                                @Nullable GrindProtection protection,
                                GrindType<Entity> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();
        if (player == null) return;
        if (!this.checkProtection(p -> p.isGrindAllowed(player))) return;

        boolean isSpawner = this.protection != null && this.protection.isArtificalMob(entity);

        ItemStack tool = player.getInventory().getItemInMainHand();
        KillingGrindContext context = new KillingGrindContext(tool, 1, isSpawner);

        this.giveXP(player, entity, context);
    }
}
