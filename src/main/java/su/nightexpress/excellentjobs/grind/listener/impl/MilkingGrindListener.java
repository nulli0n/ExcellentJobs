package su.nightexpress.excellentjobs.grind.listener.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.util.RayTraceResult;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.excellentjobs.grind.listener.GrindListener;

@NullMarked
public class MilkingGrindListener extends GrindListener<Entity> {

    // TODO Add a 5 minute cooldown

    public MilkingGrindListener(JobsPlugin plugin,
                                GrindManager manager,
                                @Nullable GrindProtection protection,
                                GrindType<Entity> type) {
        super(plugin, manager, protection, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMilk(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if (event.getItemStack() == null) return;
        if (event.getItemStack().getType() != Material.MILK_BUCKET) return;

        Location location = player.getLocation();
        if (location == null) return;

        Location eyes = player.getEyeLocation();
        RayTraceResult result = player.getWorld().rayTraceEntities(eyes, location.getDirection(), 5D,
            entity -> !(entity instanceof Player));
        if (result == null) return;

        Entity entity = result.getHitEntity();
        if (entity == null) return;

        if (!this.checkProtection(p -> p.isGrindAllowed(player) && !p.isArtificalMob(entity))) return;

        this.giveXP(player, entity, GrindContext.create());
    }
}
