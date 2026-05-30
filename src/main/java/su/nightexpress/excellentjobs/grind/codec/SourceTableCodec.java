package su.nightexpress.excellentjobs.grind.codec;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.grind.table.SourceReward;
import su.nightexpress.excellentjobs.grind.table.SourceTable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class SourceTableCodec implements ConfigCodec<SourceTable> {

    @Override
    public @Nullable SourceTable read(FileConfig config, String path) throws CodecReadException {
        Map<String, SourceReward> entires = new HashMap<>();

        config.getSection(path).forEach(name -> {
            SourceReward entry = config.get(path + "." + name, SourceReward.class);
            entires.put(name, entry);
        });

        return new SourceTable(entires);
    }

    @Override
    public void write(FileConfig config, String path, SourceTable value) {
        config.remove(path);
        value.getEntryMap().forEach((name, entry) -> {
            config.set(path + "." + name, entry);
        });
    }
}
