package su.nightexpress.excellentjobs.grind;

import java.util.Set;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class GrindSettings extends AbstractConfig {

    private final ConfigProperty<String> preferredCurrency = this.addProperty(
        ConfigCodecs.STRING,
        "General.Preferred-Currency",
        CurrencyId.VAULT,
        "Sets preferred currency used by the '%s' objective property."
            .formatted(GrindObjectiveProperty.INCOME.name())
    );

    private final ConfigProperty<Set<String>> disabledTypes = this.addProperty(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "General.Disabled-Types",
        Set.of("type_name_1", "type_name_2"),
        "List of grind types that should be disabled globally."
    );

    private final ConfigProperty<Boolean> protectionEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "General.Protection-Enabled",
        true,
        "Whether Protection module is enabled.",
        "Protection module will ensure players can't abuse grind systems."
    );

    private final ConfigProperty<Boolean> grindBarEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "General.Grind-Bar-Enabled",
        true,
        ""
    );

    private final ConfigProperty<Boolean> grindBarElementEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "BarElement.Enabled",
        true,
        ""
    );

    private final ConfigProperty<String> grindBarElementFormat = this.addProperty(
        ConfigCodecs.STRING,
        "BarElement.Format",
        TagWrappers.GREEN.wrap("+" + CommonPlaceholders.GENERIC_AMOUNT)
    );

    public String getPreferredCurrency() {
        return this.preferredCurrency.get();
    }

    public boolean isTypeDisabled(String id) {
        return this.disabledTypes.get().contains(LowerCase.INTERNAL.apply(id));
    }

    public boolean isProtectionEnabled() {
        return this.protectionEnabled.get();
    }

    public boolean isGrindBarEnabled() {
        return this.grindBarEnabled.get();
    }

    public boolean isBarElementEnabled() {
        return this.grindBarElementEnabled.get();
    }

    public String getBarElementFormat() {
        return this.grindBarElementFormat.get();
    }
}
