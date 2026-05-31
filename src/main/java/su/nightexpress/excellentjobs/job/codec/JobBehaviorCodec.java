package su.nightexpress.excellentjobs.job.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.JobBehavior;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class JobBehaviorCodec implements ConfigCodec<JobBehavior> {

    @Override
    public JobBehavior read(FileConfig config, String path) throws CodecReadException {
        boolean permissionRequired = config.getOrSet(path + ".Permission-Required", ConfigCodecs.BOOLEAN, false);

        List<String> joinCommands = config.getOrSet(path + ".Join-Commands", ConfigCodecs.STRING_LIST, List.of());
        List<String> leaveCommands = config.getOrSet(path + ".Leave-Commands", ConfigCodecs.STRING_LIST, List.of());

        return new JobBehavior.Builder()
            .setPermissionRequired(permissionRequired)
            .setJoinCommands(joinCommands)
            .setLeaveCommands(leaveCommands)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, JobBehavior behavior) {
        config.set(path + ".Permission-Required", behavior.isPermissionRequired());
        config.set(path + ".Join-Commands", behavior.getJoinCommands());
        config.set(path + ".Leave-Commands", behavior.getLeaveCommands());
    }
}
