package su.nightexpress.excellentjobs.zone.model;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.zone.core.ZonePerms;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class Zone implements PlaceholderResolvable {

    private final String         id;
    private final ZoneDefinition definition;
    private final ZoneBehavior   behavior;

    public Zone(String id, ZoneDefinition definition, ZoneBehavior behavior) {
        this.id = id;
        this.definition = definition;
        this.behavior = behavior;
    }

    @Override
    public PlaceholderResolver placeholders() {
        return JobsPlaceholders.ZONE.resolver(this);
    }

    public boolean contains(Location location) {
        return this.definition.getCuboid().contains(location);
    }

    public boolean hasPermission(Player player) {
        if (!this.definition.isPermissionRequired()) return true;

        return player.hasPermission(this.getPermission());
    }

    public String getId() {
        return id;
    }

    public ZoneDefinition getDefinition() {
        return definition;
    }

    public ZoneBehavior getBehavior() {
        return behavior;
    }

    public String getPermission() {
        return ZonePerms.getZonePermission(this.id);
    }

    public String getWorldName() {
        return this.definition.getWorldName();
    }

    public Cuboid getCuboid() {
        return this.definition.getCuboid();
    }

    public String getName() {
        return this.definition.getName();
    }

    public List<String> getDescription() {
        return this.definition.getDescription();
    }

    public NightItem getIcon() {
        return this.definition.getIcon();
    }
}
