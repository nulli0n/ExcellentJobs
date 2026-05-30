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
import su.nightexpress.nightcore.util.Numbers;

@NullMarked
public class SourceTableLegacyCodec implements ConfigCodec<SourceTable> {

    @Override
    public @Nullable SourceTable read(FileConfig config, String path) throws CodecReadException {
        Map<String, SourceReward> entires = new HashMap<>();

        config.getSection(path).forEach(name -> {
            String data = config.getString(path + "." + name);
            if (data == null) return;

            SourceReward legacyReward = deserializeLegacy(data);
            config.set(path + "." + name, legacyReward);
            entires.put(name, legacyReward);
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

    public static SourceReward deserializeLegacy(String from) {
        String[] sections = from.split(" ");

        String xpRaw = sections[0];
        String moneyRaw = sections.length >= 2 ? sections[1] : "";
        double chance = sections.length >= 3 ? Numbers.getDoubleAbs(sections[2]) : 100D;

        double[] xpAmount = parseLegacyAmount(xpRaw);
        double[] moneyAmount = parseLegacyAmount(moneyRaw);

        SourceReward reward = new SourceReward();
        reward.setProperty(SourceReward.XP, xpAmount);
        reward.setProperty(SourceReward.INCOME, moneyAmount);
        reward.setProperty(SourceReward.PROBABILITY, new double[]{chance, chance});

        return reward;
    }

    private static double[] parseLegacyAmount(String string) {
        String[] split = string.split(";");
        int length = split.length;

        double min = Numbers.getDoubleAbs(split[0]);
        double max = length >= 2 ? Numbers.getDoubleAbs(split[1]) : min;

        return new double[]{min, max};
    }
}
