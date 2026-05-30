package su.nightexpress.excellentjobs.contract.definition;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public record Timeframe(LocalTime startTime, LocalTime endTime) {

    public static final Codec CODEC = new Codec();

    public static final Timeframe WHOLE_DAY = new Timeframe(LocalTime.MIN, LocalTime.MAX);
    private static final String DELIMITER = "-";

    public boolean isInRange(LocalTime time) {
        return time.isAfter(this.startTime) && time.isBefore(this.endTime);
    }

    public static class Codec implements ConfigCodec<Timeframe> {

        @Override
        public Timeframe read(FileConfig config, String path) throws CodecReadException {
            String string = config.getString(path);
            if (string == null) return WHOLE_DAY;

            String[] pair = string.split(DELIMITER);
            if (pair.length != 2) return WHOLE_DAY;

            try {
                LocalTime start = LocalTime.parse(pair[0]);
                LocalTime end = LocalTime.parse(pair[1]);

                return new Timeframe(start, end);
            }
            catch (DateTimeParseException exception) {
                return WHOLE_DAY;
            }
        }

        @Override
        public void write(FileConfig config, String path, Timeframe value) {
            config.set(path, value.startTime + DELIMITER + value.endTime);
        }
    }
}
