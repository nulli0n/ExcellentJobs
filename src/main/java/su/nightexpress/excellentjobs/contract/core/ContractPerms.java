package su.nightexpress.excellentjobs.contract.core;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.config.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public class ContractPerms {

    private ContractPerms() {
    }

    public static final PermissionNamespace ROOT     = Perms.ROOT.namespace("contracts");
    public static final PermissionNamespace CONTRACT = ROOT.namespace("contract");

    public static String getJobContractPermission(String contractId, String jobId) {
        return CONTRACT.getPrefix() + "." + contractId + "." + jobId;
    }
}
