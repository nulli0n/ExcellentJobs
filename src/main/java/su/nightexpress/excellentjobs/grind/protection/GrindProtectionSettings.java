package su.nightexpress.excellentjobs.grind.protection;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;

public class GrindProtectionSettings extends AbstractConfig {

    private final ConfigProperty<Boolean> trackPlayerBlocks = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Protection.Track-Player-Blocks",
        true,
        "Controls whether player placed blocks will produce no rewards."
    );

    private final ConfigProperty<Set<String>> preventInWorlds = this.addProperty(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "Protection.Prevent-In-Worlds",
        Set.of("custom_world", "your_world_name"),
        "List of worlds, where job objectives will produce no rewards."
    );

    private final ConfigProperty<Boolean> preventInCreative = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Protection.Prevent-In-Creative",
        true,
        "Controls whether job objectives will produce no rewards for players in Creative mode."
    );

    private final ConfigProperty<Boolean> dailyLimitsEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Protection.Daily-Limits.Enabled",
        false,
        "Controls whether Daily Limits enabled."
    );

    private final ConfigProperty<Map<GrindObjectiveProperty, Double>> dailyLimitsValues = this.addProperty(
        ConfigCodecs.forMap(s -> Enums.get(s, GrindObjectiveProperty.class), Enum::name, ConfigCodecs.DOUBLE),
        "Protection.Daily-Limits.Values",
        Map.of(
            GrindObjectiveProperty.XP, 50000D,
            GrindObjectiveProperty.INCOME, 100_000D,
            GrindObjectiveProperty.CONTRACT_POINTS, 1000D
        ),
        "Daily limits.",
        "[>] Use '-1' for no limit.",
        "[>] Available values: [%s]".formatted(Enums.inline(GrindObjectiveProperty.class)),
        "[*] The '%s' value is useless here.".formatted(GrindObjectiveProperty.PROBABILITY.name())
    );

    private final ConfigProperty<EnumSet<CreatureSpawnEvent.SpawnReason>> artificalMobSpawns = this.addProperty(
        ConfigCodecs.forEnumSet(CreatureSpawnEvent.SpawnReason.class),
        "Protection.Artifical-Mob-Spawns",
        EnumSet.of(
            CreatureSpawnEvent.SpawnReason.EGG,
            CreatureSpawnEvent.SpawnReason.SPAWNER,
            CreatureSpawnEvent.SpawnReason.SPAWNER_EGG,
            CreatureSpawnEvent.SpawnReason.DISPENSE_EGG,
            CreatureSpawnEvent.SpawnReason.TRIAL_SPAWNER,
            CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN,
            CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM,
            CreatureSpawnEvent.SpawnReason.SLIME_SPLIT
        ),
        "List of mob spawn reasons considered artifical.",
        "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html"
    );

    private final ConfigProperty<Set<Material>> artificalBlockFormations = this.addProperty(
        ConfigCodecs.MATERIAL_SET,
        "Protection.Artifical-Block-Formations",
        EnumSet.of(
            Material.STONE,
            Material.COBBLESTONE,
            Material.OBSIDIAN
        ),
        "List of block formations (generators) considered artifical."
    );

    private final ConfigProperty<Set<String>> forbiddenFertilisers = this.addProperty(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "Protection.Forbidden-Fertilisers",
        Lists.newSet(Placeholders.WILDCARD),
        "Listed blocks will produce no rewards when fertilized by a player."
    );

    private final ConfigProperty<Set<EntityType>> forbiddenVehicles = this.addProperty(
        ConfigCodecs.ENTITY_TYPE_SET,
        "Protection.Forbidden-Vehicles",
        EnumSet.of(
            EntityType.MINECART
        ),
        "Controls whether job objectives will produce no rewards for players in certain vehicles."
    );

    public boolean getTrackBlockPlayers() {
        return this.trackPlayerBlocks.get();
    }

    public boolean getPreventInCreative() {
        return this.preventInCreative.get();
    }

    public Set<String> getPreventInWorlds() {
        return this.preventInWorlds.get();
    }

    public boolean getDailyLimitsEnabled() {
        return this.dailyLimitsEnabled.get();
    }

    public Map<GrindObjectiveProperty, Double> getDailyLimitsValues() {
        return this.dailyLimitsValues.get();
    }

    public Set<SpawnReason> getArtificalMobSpawns() {
        return this.artificalMobSpawns.get();
    }

    public Set<Material> getArtificalBlockFormations() {
        return this.artificalBlockFormations.get();
    }

    public Set<String> getForbiddenFertilisers() {
        return this.forbiddenFertilisers.get();
    }

    public Set<EntityType> getForbiddenVehicles() {
        return this.forbiddenVehicles.get();
    }
}
