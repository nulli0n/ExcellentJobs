package su.nightexpress.excellentjobs.zone.core;


import org.bukkit.permissions.Permission;

import su.nightexpress.excellentjobs.config.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public class ZonePerms {

    private ZonePerms() {
    }

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("zones");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");
    public static final PermissionNamespace ZONE    = ROOT.namespace("zone");

    public static final Permission COMMAND_ZONE        = COMMAND.create("zone");
    public static final Permission COMMAND_ZONE_DEFINE = COMMAND.create("zone.define");
    public static final Permission COMMAND_ZONE_DELETE = COMMAND.create("zone.delete");
    public static final Permission COMMAND_ZONE_WAND   = COMMAND.create("zone.wand");

    public static final Permission BYPASS_ZONE_REQUIREMENTS = BYPASS.create("zone.requirements");
    public static final Permission BYPASS_ZONE_PROTECTION   = BYPASS.create("zone.protection");

    public static String getZonePermission(String zoneId) {
        return ZONE.getPrefix() + "." + zoneId;
    }
}
