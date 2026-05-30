package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class BreedingGrindListener extends GrindListener<Entity> {

    public BreedingGrindListener(JobsPlugin plugin,
                                 GrindManager manager,
                                 @Nullable GrindProtection protection,
                                 GrindType<Entity> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        LivingEntity breeder = event.getBreeder();
        if (!(breeder instanceof Player player)) return;

        LivingEntity entity = event.getEntity();
        if (!this.checkProtection(p -> p.isGrindAllowed(player) && !p.isArtificalMob(entity))) return;

        this.giveXP(player, entity, GrindContext.create());
    }
}
