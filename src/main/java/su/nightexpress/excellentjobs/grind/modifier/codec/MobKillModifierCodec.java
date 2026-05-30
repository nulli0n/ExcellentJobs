package su.nightexpress.excellentjobs.grind.modifier.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.modifier.impl.MobKillGrindModifier;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class MobKillModifierCodec implements ConfigCodec<MobKillGrindModifier> {

    @Override
    public MobKillGrindModifier read(FileConfig config, String path) throws CodecReadException {
        double spawnerMobBonus = config.getOrSet(path + ".SpawnerMobBonus", ConfigCodecs.DOUBLE, -90D);

        return new MobKillGrindModifier(spawnerMobBonus);
    }

    @Override
    public void write(FileConfig config, String path, MobKillGrindModifier value) {
        config.set(path + ".SpawnerMobBonus", value.getSpawnerMobBonus());
    }
}
