package su.nightexpress.excellentjobs.contract.codec;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.contract.definition.ContractBehavior;
import su.nightexpress.excellentjobs.contract.definition.Timeframe;
import su.nightexpress.excellentjobs.contract.model.ObjectiveModifier;
import su.nightexpress.excellentjobs.util.TimeParser;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Enums;

@NullMarked
public class ContractBehaviorCodec implements ConfigCodec<ContractBehavior> {

    @Override
    public ContractBehavior read(FileConfig config, String path) throws CodecReadException {
        String leaveCooldown = config.getOrSet(path + ".Leave-Cooldown", ConfigCodecs.STRING, "3d");
        long leaveCooldownMillis = TimeParser.parseToMillis(leaveCooldown);

        Map<DayOfWeek, Timeframe> timeframes = new EnumMap<>(DayOfWeek.class);
        Map<GrindObjectiveProperty, ObjectiveModifier> objectiveModifiers = new EnumMap<>(GrindObjectiveProperty.class);

        String daysPath = path + ".Timeframe.Days";
        config.getSection(daysPath).forEach(dayName -> {
            DayOfWeek day = Enums.get(dayName, DayOfWeek.class);
            if (day == null) return;

            timeframes.put(day, config.get(daysPath + "." + day.name(), Timeframe.class));
        });

        String modPath = path + ".Objective-Property-Modifiers";
        config.getSection(modPath).forEach(modName -> {
            GrindObjectiveProperty property = Enums.get(modName, GrindObjectiveProperty.class);
            if (property == null) return; // TODO Log warn

            ObjectiveModifier modifier = config.get(modPath + "." + modName, ObjectiveModifier.class);
            if (modifier == null) return;

            objectiveModifiers.put(property, modifier);
        });

        return new ContractBehavior.Builder()
            .setLeaveCooldown(leaveCooldownMillis)
            .setTimeframes(timeframes)
            .setObjectiveModifiers(objectiveModifiers)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, ContractBehavior value) {
        config.set(path + ".Leave-Cooldown", TimeParser.formatMillis(value.getLeaveCooldown()));

        String daysPath = path + ".Timeframe.Days";
        config.remove(daysPath);
        value.getTimeframes().forEach((day, timeframe) -> config.set(daysPath + "." + day.name(), timeframe));

        String modPath = path + ".Objective-Property-Modifiers";
        config.remove(modPath);
        value.getObjectiveModifiers().forEach((property, modifier) -> config.set(modPath + "." + property.name(),
            modifier));
    }
}
