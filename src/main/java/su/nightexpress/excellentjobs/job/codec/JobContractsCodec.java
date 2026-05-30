package su.nightexpress.excellentjobs.job.codec;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.JobContracts;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Placeholders;

@NullMarked
public class JobContractsCodec implements ConfigCodec<JobContracts> {

    @Override
    public JobContracts read(FileConfig config, String path) throws CodecReadException {
        boolean required = config.getOrSet("Contract.Required", ConfigCodecs.BOOLEAN, true);
        Set<String> allowedIds = config.getOrSet("Contract.Allowed-Ids", ConfigCodecs.STRING_SET,
            Set.of(Placeholders.WILDCARD)
        );

        return new JobContracts(required, allowedIds);
    }

    @Override
    public void write(FileConfig config, String path, JobContracts value) {
        config.set(path + ".Required", value.isRequired());
        config.set(path + ".Allowed-Ids", value.getAllowedIds());
    }
}
