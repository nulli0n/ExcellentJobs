package su.nightexpress.excellentjobs.grind.modifier.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.modifier.impl.EnchantingGrindModifier;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class EnchantingModifierCodec implements ConfigCodec<EnchantingGrindModifier> {

    @Override
    public EnchantingGrindModifier read(FileConfig config, String path) throws CodecReadException {
        double levelBonus = config.getOrSet(path + ".LevelBonus", ConfigCodecs.DOUBLE, 25D);

        return new EnchantingGrindModifier(levelBonus);
    }

    @Override
    public void write(FileConfig config, String path, EnchantingGrindModifier value) {
        config.set(path + ".LevelBonus", value.getLevelBonus());
    }
}
