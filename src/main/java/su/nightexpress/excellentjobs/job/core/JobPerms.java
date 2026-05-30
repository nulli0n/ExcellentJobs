package su.nightexpress.excellentjobs.job.core;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.config.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public class JobPerms {

    private JobPerms() {
    }

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("job");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace JOB     = ROOT.namespace("job");

    public static final Permission COMMAND_JOIN  = COMMAND.create("join");
    public static final Permission COMMAND_LEAVE = COMMAND.create("leave");
    public static final Permission COMMAND_MENU  = COMMAND.create("menu");

    public static final String getJobPermission(String jobId) {
        return JOB.getPrefix() + "." + jobId;
    }
}
