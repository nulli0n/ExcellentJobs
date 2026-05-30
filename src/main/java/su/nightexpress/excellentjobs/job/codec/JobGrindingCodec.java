package su.nightexpress.excellentjobs.job.codec;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.JobGrinding;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class JobGrindingCodec implements ConfigCodec<JobGrinding> {

    @Override
    public JobGrinding read(FileConfig config, String path) throws CodecReadException {
        NightBarColor barColor = config.getOrSet(path + ".Bar-Color", ConfigCodecs.forEnum(NightBarColor.class),
            NightBarColor.GREEN);
        Set<String> objectiveIds = config.getOrSet(path + ".Objective-Ids", ConfigCodecs.STRING_SET, Set.of());

        return new JobGrinding(barColor, objectiveIds);
    }

    @Override
    public void write(FileConfig config, String path, JobGrinding grinding) {
        config.set(path + ".Bar-Color", grinding.getBarColor().name());
        config.set(path + ".Objective-Ids", grinding.getObjectiveIds());
    }
}
