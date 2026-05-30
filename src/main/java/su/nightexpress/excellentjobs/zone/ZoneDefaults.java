package su.nightexpress.excellentjobs.zone;

import org.bukkit.Material;

import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class ZoneDefaults {

    private ZoneDefaults() {
    }

    public static NightItem getDefaultZoneWand() {
        return NightItem.fromType(Material.STICK)
            .setEnchantGlint(true)
            .setDisplayName(TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Zone Wand"))
            .setLore(Lists.newList(
                TagWrappers.DARK_GRAY.wrap("(Drop to exit selection mode)"),
                "",
                TagWrappers.GOLD.wrap("[▶] ") + TagWrappers.GRAY.wrap("Left-Click to " + TagWrappers.GOLD.wrap(
                    "set 1st") + " point."),
                TagWrappers.GOLD.wrap("[▶] ") + TagWrappers.GRAY.wrap("Right-Click to " + TagWrappers.GOLD.wrap(
                    "set 2nd") + " point.")
            ));
    }
}
