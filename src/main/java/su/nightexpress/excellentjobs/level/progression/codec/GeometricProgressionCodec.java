package su.nightexpress.excellentjobs.level.progression.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.level.progression.GeometricProgression;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class GeometricProgressionCodec implements ConfigCodec<GeometricProgression> {

    @Override
    public GeometricProgression read(FileConfig config, String path) throws CodecReadException {
        double base = config.getOrSet(path + ".Base", ConfigCodecs.DOUBLE, GeometricProgression.DEFAULT_BASE);
        double multiplier = config.getOrSet(path + ".Multiplier", ConfigCodecs.DOUBLE,
            GeometricProgression.DEFAULT_MULTIPLIER);

        return new GeometricProgression(base, multiplier);
    }

    @Override
    public void write(FileConfig config, String path, GeometricProgression value) {
        config.set(path + ".Base", value.getBase());
        config.set(path + ".Multiplier", value.getMultiplier());
    }
}
