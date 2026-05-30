package su.nightexpress.excellentjobs.level.reward.codec;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.level.reward.model.RewardBase;
import su.nightexpress.excellentjobs.level.reward.model.RewardModel;
import su.nightexpress.excellentjobs.level.reward.model.RewardValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class RewardModelCodec implements ConfigCodec<RewardModel> {

    @Override
    public RewardModel read(FileConfig config, String path) throws CodecReadException {
        RewardBase base = config.get(path, RewardBase.class);
        if (base == null) throw new CodecReadException("Could not read RewardBase data");

        Map<String, RewardValue> values = new HashMap<>();
        config.getSection(path + ".Values").forEach(valName -> {
            RewardValue value = config.get(path + ".Values." + valName, RewardValue.class);
            if (value == null) return;

            values.put(LowerCase.INTERNAL.apply(valName), value);
        });

        return new RewardModel(base, values);
    }

    @Override
    public void write(FileConfig config, String path, RewardModel template) {
        config.set(path, template.getBase());

        String valuesPath = path + ".Values";
        config.remove(valuesPath);
        template.getValues().forEach((valName, value) -> {
            config.writeByCodec(valuesPath + "." + valName, value);
        });
    }
}
