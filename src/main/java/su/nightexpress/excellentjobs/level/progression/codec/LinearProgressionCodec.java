package su.nightexpress.excellentjobs.level.progression.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.level.progression.LinearProgression;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class LinearProgressionCodec implements ConfigCodec<LinearProgression> {

    @Override
    public LinearProgression read(FileConfig config, String path) throws CodecReadException {
        double base = config.getOrSet(path + ".Base", ConfigCodecs.DOUBLE, LinearProgression.DEFAULT_BASE);
        double step = config.getOrSet(path + ".Step", ConfigCodecs.DOUBLE, LinearProgression.DEFAULT_STEP);

        return new LinearProgression(base, step);
    }

    @Override
    public void write(FileConfig config, String path, LinearProgression progression) {
        config.set(path + ".Base", progression.getBase());
        config.set(path + ".Step", progression.getStep());

    }
}
