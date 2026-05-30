package su.nightexpress.excellentjobs.grind.modifier.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.modifier.impl.CookingGrindModifier;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CookingModifierCodec implements ConfigCodec<CookingGrindModifier> {

    @Override
    public CookingGrindModifier read(FileConfig config, String path) throws CodecReadException {
        double autoSmeltBonus = config.getOrSet(path + ".AutomatedSmeltBonus", ConfigCodecs.DOUBLE, -80D);
        double manualSmeltBonus = config.getOrSet(path + ".ManualSmeltBonus", ConfigCodecs.DOUBLE, 50D);

        return new CookingGrindModifier(autoSmeltBonus, manualSmeltBonus);
    }

    @Override
    public void write(FileConfig config, String path, CookingGrindModifier value) {
        config.set(path + ".AutomatedSmeltBonus", value.getAutoSmeltBonus());
        config.set(path + ".ManualSmeltBonus", value.getManualSmeltBonus());
    }
}
