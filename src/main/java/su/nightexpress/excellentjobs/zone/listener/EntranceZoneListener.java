package su.nightexpress.excellentjobs.zone.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.zone.ZoneManager;
import su.nightexpress.excellentjobs.zone.activity.ActiveZone;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.nightcore.manager.AbstractListener;

@NullMarked
public class EntranceZoneListener extends AbstractListener<JobsPlugin> {

    private final ZoneManager manager;

    public EntranceZoneListener(JobsPlugin plugin, ZoneManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneEntrance(PlayerMoveEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) return;

        Zone zone = this.manager.getZoneByLocation(to);
        if (zone == null) return;

        Player player = event.getPlayer();

        ActiveZone activeZone = this.manager.getActiveZone(zone);
        if (activeZone == null) return;

        if (!activeZone.isAvailable(player) && !this.manager.isInZone(player)) {
            event.setCancelled(true);
        }
    }
}
