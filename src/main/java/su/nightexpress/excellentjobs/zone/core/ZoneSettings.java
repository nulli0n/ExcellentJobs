package su.nightexpress.excellentjobs.zone.core;

import org.bukkit.Material;

import su.nightexpress.excellentjobs.zone.ZoneDefaults;
import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class ZoneSettings extends AbstractConfig {

    private final ConfigProperty<Boolean> strictMode = this.addProperty(ConfigCodecs.BOOLEAN,
        "Zones.Strict_Mode",
        false,
        "When enabled, players will only get payments & Job XP when working inside Job Zones."
    );

    private final ConfigProperty<Boolean> controlEntrance = this.addProperty(ConfigCodecs.BOOLEAN,
        "Zones.Control_Entrance",
        true,
        "When enabled, prevents players from entering zones that are not available for them.",
        "Examples: When player don't have permission to specific zone; when current server time is not in zone hours, etc.",
        "You can disable this setting if you're experiencing performance issues related to the 'PlayerMoveEvent' from this plugin.",
        "Even if disabled, it still won't allow players to have zone bonuses and the whole ability to work there unless all conditions are met."
    );

    private final ConfigProperty<NightItem> wandItem = this.addProperty(ConfigCodecs.NIGHT_ITEM,
        "Zones.WandItem",
        ZoneDefaults.getDefaultZoneWand(),
        "Item used to define zone's cuboid."
    );

    private final ConfigProperty<Integer> regenerationTickInterval = this.addProperty(
        ConfigCodecs.INT,
        "Zones.RegenerationTask.Interval",
        5,
        "Sets how often (in seconds) plugin will attempt to regnerate blocks in job zones."
    );

    private final ConfigProperty<Material> highlightBlockCorner = this.addProperty(
        ConfigCodecs.MATERIAL,
        "Zones.Highlighting.CornerBlock",
        Material.WHITE_STAINED_GLASS,
        "Block type used for a fake block display entity for zone selection's corners.",
        "[Default is " + BukkitThing.getValue(Material.WHITE_STAINED_GLASS) + "]"
    );

    private final ConfigProperty<Material> highlightBlockWire = this.addProperty(
        ConfigCodecs.MATERIAL,
        "Zones.Highlighting.WireBlock",
        Material.CHAIN,
        "Block type used for a fake block display entity for zone selection's corners connections.",
        "[Default is " + BukkitThing.getValue(Material.CHAIN) + "]"
    );

    public boolean isStrictMode() {
        return this.strictMode.get();
    }

    public boolean isControlEntrance() {
        return this.controlEntrance.get();
    }

    public NightItem getWandItem() {
        return this.wandItem.get().copy();
    }

    public int getRegenerationTickInterval() {
        return this.regenerationTickInterval.get();
    }

    public Material getHighlightCorner() {
        return this.highlightBlockCorner.get();
    }

    public Material getHighlightWire() {
        return this.highlightBlockWire.get();
    }
}
