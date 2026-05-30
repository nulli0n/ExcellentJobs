package su.nightexpress.excellentjobs.grind.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.DefaultGrindTypes;
import su.nightexpress.excellentjobs.grind.GrindRegistry;
import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.excellentjobs.grind.table.SourceTable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class GrindObjectiveCodec implements ConfigCodec<GrindObjective> {

    private static final String[] OLD_VALUES_NAMES = {"Mobs", "MobDrops", "BlockDrops", "Blocks", "Items", "Ingredients", "Enchantments", "Entities"};

    private final GrindRegistry registry;

    public GrindObjectiveCodec(GrindRegistry registry) {
        this.registry = registry;
    }

    @Override
    public GrindObjective read(FileConfig config, String path) throws CodecReadException {
        this.updateLegacyValues(config, path);

        String grindTypeId = config.getOrSet(path + ".Type", ConfigCodecs.STRING, "null");

        GrindType<?> grindType = this.registry.getType(grindTypeId);
        if (grindType == null) {
            throw new CodecReadException("Unknown work type '" + grindTypeId + "'");
        }

        Class<? extends GrindModifier> modifierType = grindType.getModifierType();
        GrindModifier modifier = null;
        if (modifierType != null) {
            if (config.contains(path + ".SourceTable.Modifiers")) {
                GrindModifier legacyMod = config.get(path + ".SourceTable.Modifiers", modifierType);
                config.set(path + ".Modifiers", legacyMod);
                config.remove(path + ".SourceTable");
            }
            modifier = config.get(path + ".Modifiers", modifierType);
        }

        SourceTable table = config.get(path + ".Sources", SourceTable.class);
        if (table == null) {
            throw new CodecReadException("Can not read Sources list");
        }

        return new GrindObjective(grindType, modifier, table);
    }

    @Override
    public void write(FileConfig config, String path, GrindObjective value) {
        config.set(path + ".Type", value.getTypeId());
        config.set(path + ".Modifiers", value.getModifier());
        config.set(path + ".Sources", value.getSourceTable());
    }

    private void updateLegacyValues(FileConfig config, String path) {
        String grindTypeId = config.getOrSet(path + ".Type", ConfigCodecs.STRING, "null");
        String newTypeId = null;

        if (grindTypeId.equalsIgnoreCase("fishing")) newTypeId = DefaultGrindTypes.FISH_ITEM.getId();
        if (grindTypeId.equalsIgnoreCase("gathering")) newTypeId = DefaultGrindTypes.BLOCK_LOOT.getId();

        if (newTypeId != null) {
            config.set(path + ".Type", newTypeId);
        }

        String tablePath = path + ".SourceTable";
        if (config.contains(tablePath)) {
            for (String oldKey : OLD_VALUES_NAMES) {
                if (config.contains(tablePath + "." + oldKey)) {

                    SourceTable table = config.get(tablePath + "." + oldKey, SourceTable.LEGACY_CODEC);
                    if (table != null) {
                        config.set(path + ".Sources", table);
                        config.remove(tablePath + "." + oldKey);
                    }

                    break;
                }
            }
        }
    }
}
