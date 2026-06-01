package su.nightexpress.excellentjobs.level.core;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentjobs.config.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public class LevelingPerms {

    private LevelingPerms() {
    }

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("leveling");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission REWARD_AUTO_CLAIM = ROOT.create("reward.autoclaim");

    public static final Permission COMMAND_LEVELS = COMMAND.create("levels");

    public static final Permission COMMAND_XP      = COMMAND.create("xp");
    public static final Permission COMMAND_XP_GIVE = COMMAND.create("xp.give");
    public static final Permission COMMAND_XP_TAKE = COMMAND.create("xp.take");

    public static final Permission COMMAND_LEVEL      = COMMAND.create("level");
    public static final Permission COMMAND_LEVEL_GIVE = COMMAND.create("level.give");
    public static final Permission COMMAND_LEVEL_TAKE = COMMAND.create("level.take");
}
