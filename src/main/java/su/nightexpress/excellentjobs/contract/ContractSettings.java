package su.nightexpress.excellentjobs.contract;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.db.data.DataSettings;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class ContractSettings extends AbstractConfig implements DataSettings {

    private final ConfigProperty<Integer> dataCacheDuration = this.addProperty(ConfigCodecs.INT,
        "Data.Cache.Duration",
        3600
    );

    private final ConfigProperty<Integer> dataSaveInterval = this.addProperty(ConfigCodecs.INT,
        "Data.Save-Interval",
        5
    );

    private final ConfigProperty<Boolean> activationConfirmation = this.addProperty(ConfigCodecs.BOOLEAN,
        "Contract.Activation.Confirmation",
        true
    );

    private final ConfigProperty<Boolean> grindBarElementEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "GrindBar.Element.Enabled",
        true,
        ""
    );

    private final ConfigProperty<String> grindBarElementFormat = this.addProperty(
        ConfigCodecs.STRING,
        "GrindBar.Element.Format",
        TagWrappers.PURPLE.wrap("+" + CommonPlaceholders.GENERIC_AMOUNT + " Ct. Points"),
        ""
    );

    @Override
    public int getCacheTimeDuration() {
        return this.dataCacheDuration.get();
    }

    @Override
    public TimeUnit getCacheTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public int getSaveInterval() {
        return this.dataSaveInterval.get();
    }

    public boolean isActivationConfirmation() {
        return this.activationConfirmation.get();
    }

    public boolean isBarElementEnabled() {
        return this.grindBarElementEnabled.get();
    }

    public String getBarElementFormat() {
        return this.grindBarElementFormat.get();
    }
}
