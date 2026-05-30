package su.nightexpress.excellentjobs.grind.modifier.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.modifier.impl.BrewingGrindModifier;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class BrewingModifierCodec implements ConfigCodec<BrewingGrindModifier> {

    @Override
    public BrewingGrindModifier read(FileConfig config, String path) throws CodecReadException {
        double amountBonus = config.getOrSet(path + ".PerPotionBonus", ConfigCodecs.DOUBLE, 25D);
        double autoBrewPenalty = config.getOrSet(path + ".AutomatedBrewBonus", ConfigCodecs.DOUBLE, -80D);
        double manualBrewBonus = config.getOrSet(path + ".ManualBrewBonus", ConfigCodecs.DOUBLE, 50D);

        return new BrewingGrindModifier(amountBonus, autoBrewPenalty, manualBrewBonus);
    }

    @Override
    public void write(FileConfig config, String path, BrewingGrindModifier value) {
        config.set(path + ".PerPotionBonus", value.getAmountBonus());
        config.set(path + ".AutomatedBrewBonus", value.getAutoBrewPenalty());
        config.set(path + ".ManualBrewBonus", value.getManualBrewBonus());
    }
}
