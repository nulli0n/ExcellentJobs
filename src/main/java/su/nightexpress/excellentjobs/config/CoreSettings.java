package su.nightexpress.excellentjobs.config;

import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class CoreSettings extends AbstractConfig {

    private final ConfigProperty<Boolean> grindEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Features.Grind",
        true,
        "Controls whether Grind module is enabled.",
        "It allows players to get rewards for doing certain objectives in their jobs."
    );

    private final ConfigProperty<Boolean> levelingEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Features.Leveling",
        true,
        "Controls whether Leveling module is enabled."
    );

    private final ConfigProperty<Boolean> contractsEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Features.Contracts",
        true,
        "Controls whether Contracts module is enabled."
    );

    private final ConfigProperty<Boolean> statsEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Features.Stats",
        true,
        "Controls whether Stats module is enabled."
    );

    private final ConfigProperty<Boolean> zonesEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Features.Zones",
        true,
        "Controls whether Zones module is enabled."
    );

    public boolean isGrindEnabled() {
        return this.grindEnabled.get();
    }

    public boolean isLevelingEnabled() {
        return this.levelingEnabled.get();
    }

    public boolean isContractsEnabled() {
        return this.contractsEnabled.get();
    }

    public boolean isStatsEnabled() {
        return this.statsEnabled.get();
    }

    public boolean isZonesEnabled() {
        return this.zonesEnabled.get();
    }
}
