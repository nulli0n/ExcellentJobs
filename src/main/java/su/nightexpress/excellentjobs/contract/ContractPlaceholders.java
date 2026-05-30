package su.nightexpress.excellentjobs.contract;

import java.util.Objects;
import java.util.stream.Collectors;

import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.contract.core.ContractLang;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

public class ContractPlaceholders {

    private ContractPlaceholders() {
    }

    public static final String CONTRACT_NAME               = "%contract_name%";
    public static final String CONTRACT_DESCRIPTION        = "%contract_description%";
    public static final String CONTRACT_ACTIVATION_COST    = "%contract_activation_cost%";
    public static final String CONTRACT_LEAVE_COOLDOWN     = "%contract_leave_cooldown%";
    public static final String CONTRACT_PROMOTION_POINTS   = "%contract_promotion_points%";
    public static final String CONTRACT_PROMOTION_DURATION = "%contract_promotion_duration%";

    public static final TypedPlaceholder<Contract> CONTRACT = TypedPlaceholder.builder(Contract.class)
        .with(CONTRACT_NAME, contract -> contract.getDefinition().getName())
        .with(CONTRACT_DESCRIPTION, contract -> String.join(TagWrappers.BR, contract.getDefinition().getDescription()))
        .with(CONTRACT_ACTIVATION_COST, contract -> {
            return contract.getUnlocking().getCost().entrySet().stream().map(entry -> {
                String id = entry.getKey();
                double amount = entry.getValue();

                Currency currency = EconomyBridge.api().getCurrency(id);
                if (currency == null) return Lang.OTHER_N_A.text();

                return currency.format(amount);
            }).filter(Objects::nonNull).collect(Collectors.joining(ContractLang.OTHER_COST_DELIMITER.text()));
        })
        .with(CONTRACT_LEAVE_COOLDOWN, contract -> {
            return TimeFormats.formatAmount(contract.getBehavior().getLeaveCooldown(), TimeFormatType.LITERAL);
        })
        .with(CONTRACT_PROMOTION_POINTS, contract -> String.valueOf(contract.getPromotion().getPointsGoal()))
        .with(CONTRACT_PROMOTION_DURATION, contract -> TimeFormats.formatAmount(contract.getPromotion()
            .getDurationMillis(), TimeFormatType.LITERAL)
        )
        .build();
}
