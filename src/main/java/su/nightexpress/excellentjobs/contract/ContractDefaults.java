package su.nightexpress.excellentjobs.contract;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.contract.condition.impl.ActiveContractsCondition;
import su.nightexpress.excellentjobs.contract.core.ContractPerms;
import su.nightexpress.excellentjobs.contract.definition.ContractBehavior;
import su.nightexpress.excellentjobs.contract.definition.ContractDefinition;
import su.nightexpress.excellentjobs.contract.definition.ContractPromotion;
import su.nightexpress.excellentjobs.contract.definition.ContractUnlocking;
import su.nightexpress.excellentjobs.contract.definition.Timeframe;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.contract.model.ObjectiveModifier;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class ContractDefaults {

    private final JobManager      jobManager;
    private final ContractManager contractManager;

    public ContractDefaults(JobManager jobManager, ContractManager contractManager) {
        this.jobManager = jobManager;
        this.contractManager = contractManager;
    }

    public List<Contract> createDefaultContracts() {
        List<Contract> contracts = new ArrayList<>();

        contracts.add(createBasic());
        contracts.add(createAdvanced());

        return contracts;
    }

    private List<String> createDescription(String... lines) {
        return Stream.of(lines).map(TagWrappers.GRAY::wrap).toList();
    }

    private Contract createBasic() {
        ContractDefinition definition = new ContractDefinition.Builder()
            .setName(TagWrappers.WHITE.and(TagWrappers.BOLD).wrap("Basic"))
            .setDescription(createDescription(
                "The most basic contract with no special privileges.",
                "Available to everyone.",
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEMS.apply("item/clock_12")) + " " +
                    TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("Working Hours:"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "Mon-Fri: " +
                    TagWrappers.WHITE.wrap("10 AM") + " - " + TagWrappers.WHITE.wrap("10 PM"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "Sat-Sun: " +
                    TagWrappers.WHITE.wrap("12 PM") + " - " + TagWrappers.WHITE.wrap("8 PM"),
                "",
                "Work done outside of working hours " +
                    TagWrappers.SOFT_RED.and(TagWrappers.BOLD).wrap("IS NOT PAID") + "!",
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.ARROW)) + " " +
                    TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Objective Bonuses:"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "XP: " + TagWrappers.GREEN.wrap("+1%") + " per job level",
                TagWrappers.DARK_GRAY.wrap(" » ") + "Earnings: " + TagWrappers.GREEN.wrap("+1%") + " per job level",
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.EXPERIENCE_BOTTLE)) + " " +
                    TagWrappers.BLUE.and(TagWrappers.BOLD).wrap("Promotion:"),
                "Earn " + TagWrappers.BLUE.wrap("2,500 Contract Points"),
                "within " + TagWrappers.WHITE.wrap("14 days") + " to receive a promotion",
                "and additional rewards.",
                "",
                "In case of failure, a promotion retry",
                "will be available in no less than " + TagWrappers.SOFT_RED.wrap("3 days") + "."
            ))
            .setPriority(0)
            .setIcon(NightItem.fromType(Material.COAL))
            .build();

        ContractUnlocking unlocking = new ContractUnlocking.Builder()
            .setConditionsEnabled(false)
            .setCostEnabled(false)
            .setPermissionRequired(false)
            .build();

        ContractBehavior behavior = new ContractBehavior.Builder()
            .setLeaveCooldown(TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS))
            .setObjectiveModifiers(Map.of(
                GrindObjectiveProperty.XP, ObjectiveModifier.create(1D, 0.01, 1D),
                GrindObjectiveProperty.INCOME, ObjectiveModifier.create(1D, 0.01, 1D),
                GrindObjectiveProperty.CONTRACT_POINTS, ObjectiveModifier.create(1D, 0, 1D),
                GrindObjectiveProperty.PROBABILITY, ObjectiveModifier.create(1D, 0, 1D)
            ))
            .setTimeframes(Map.of(
                DayOfWeek.MONDAY, new Timeframe(LocalTime.of(10, 0), LocalTime.of(22, 0)),
                DayOfWeek.TUESDAY, new Timeframe(LocalTime.of(10, 0), LocalTime.of(22, 0)),
                DayOfWeek.WEDNESDAY, new Timeframe(LocalTime.of(10, 0), LocalTime.of(22, 0)),
                DayOfWeek.THURSDAY, new Timeframe(LocalTime.of(10, 0), LocalTime.of(22, 0)),
                DayOfWeek.FRIDAY, new Timeframe(LocalTime.of(10, 0), LocalTime.of(22, 0)),
                DayOfWeek.SATURDAY, new Timeframe(LocalTime.of(12, 0), LocalTime.of(20, 0)),
                DayOfWeek.SUNDAY, new Timeframe(LocalTime.of(12, 0), LocalTime.of(20, 0))
            ))
            .build();

        ContractPromotion promotion = new ContractPromotion.Builder()
            .enabled(true)
            .cooldownEnabled(true)
            .cooldownMillis(TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS))
            .durationEnabled(true)
            .durationMillis(TimeUnit.MILLISECONDS.convert(14, TimeUnit.DAYS))
            .pointsGoal(2500)
            .rewardCommands(Lists.newList(
                "money give " + CommonPlaceholders.PLAYER_NAME + " 15000",
                "lp user " + CommonPlaceholders.PLAYER_NAME + " permission set " + ContractPerms
                    .getJobContractPermission("advanced", JobsPlaceholders.JOB_ID) + " true"
            ))
            .rewardMessages(Lists.newList(
                TagWrappers.GRAY.wrap("You have unlocked " + TagWrappers.GREEN.wrap("Advanced") +
                    " contract for your " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job!")
            ))
            .build();

        return new Contract("basic", definition, behavior, unlocking, promotion);
    }

    private Contract createAdvanced() {
        ContractDefinition definition = new ContractDefinition.Builder()
            .setName(TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("Advanced"))
            .setDescription(createDescription(
                "An advanced contract for those",
                "who have mastered the basic one.",
                "",
                "Additional working hours for earning and",
                "increased objective bonuses.",
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.MAP)) + " " +
                    TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Terms & Conditions:"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "Promotion Required: " + TagWrappers.WHITE.wrap("Basic"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "Activation Cost: " + TagWrappers.WHITE.wrap("$5,000"),
                TagWrappers.DARK_GRAY.wrap(" » ") + " Usage Limits: " + TagWrappers.WHITE.wrap("0 - 3"),
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEMS.apply("item/clock_12")) + " " +
                    TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("Working Hours:"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "Mon-Fri: " +
                    TagWrappers.WHITE.wrap("8 AM") + " - " + TagWrappers.WHITE.wrap("10 PM"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "Sat-Sun: " +
                    TagWrappers.WHITE.wrap("12 PM") + " - " + TagWrappers.WHITE.wrap("12 AM"),
                "",
                "Work done outside of working hours " +
                    TagWrappers.GOLD.wrap("is paid at 20%"),
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.ARROW)) + " " +
                    TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Objective Bonuses:"),
                TagWrappers.DARK_GRAY.wrap(" » ") + "XP: " + TagWrappers.GREEN.wrap("15%") + " + " +
                    TagWrappers.GREEN.wrap("1%") + " per job level",
                TagWrappers.DARK_GRAY.wrap(" » ") + "Earnings: " + TagWrappers.GREEN.wrap("15%") + " + " +
                    TagWrappers.GREEN.wrap("1%") + " per job level",
                TagWrappers.DARK_GRAY.wrap(" » ") + "Contract Points: " + TagWrappers.GREEN.wrap("+10%"),
                "",
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.EXPERIENCE_BOTTLE)) + " " +
                    TagWrappers.BLUE.and(TagWrappers.BOLD).wrap("Promotion:"),
                "Earn " + TagWrappers.BLUE.wrap("4,000 Contract Points"),
                "within " + TagWrappers.WHITE.wrap("21 days") + " to receive a promotion",
                "and additional rewards.",
                "",
                "In case of failure, a promotion retry",
                "will be available in no less than " + TagWrappers.SOFT_RED.wrap("5 days") + "."
            ))
            .setPriority(1)
            .setIcon(NightItem.fromType(Material.GOLD_INGOT))
            .build();

        ContractUnlocking unlocking = new ContractUnlocking.Builder()
            .setConditionsEnabled(true)
            .setConditionsMap(Map.of(
                ActiveContractsCondition.NAME,
                new ActiveContractsCondition(this.jobManager, this.contractManager, Map.of("advanced", new int[]{0, 3}))
            ))
            .setCostEnabled(true)
            .setCost(Map.of(
                CurrencyId.VAULT, 5000D
            ))
            .setPermissionRequired(true)
            .build();

        ContractBehavior behavior = new ContractBehavior.Builder()
            .setLeaveCooldown(TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS))
            .setObjectiveModifiers(Map.of(
                GrindObjectiveProperty.XP, ObjectiveModifier.create(1.15D, 0.01, 0.8D),
                GrindObjectiveProperty.INCOME, ObjectiveModifier.create(1.15D, 0.01, 0.8D),
                GrindObjectiveProperty.CONTRACT_POINTS, ObjectiveModifier.create(1.1D, 0, 0.8D),
                GrindObjectiveProperty.PROBABILITY, ObjectiveModifier.create(1D, 0, 0.8D)
            ))
            .setTimeframes(Map.of(
                DayOfWeek.MONDAY, new Timeframe(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                DayOfWeek.TUESDAY, new Timeframe(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                DayOfWeek.WEDNESDAY, new Timeframe(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                DayOfWeek.THURSDAY, new Timeframe(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                DayOfWeek.FRIDAY, new Timeframe(LocalTime.of(8, 0), LocalTime.of(22, 0)),
                DayOfWeek.SATURDAY, new Timeframe(LocalTime.of(12, 0), LocalTime.of(0, 0)),
                DayOfWeek.SUNDAY, new Timeframe(LocalTime.of(12, 0), LocalTime.of(0, 0))
            ))
            .build();

        ContractPromotion promotion = new ContractPromotion.Builder()
            .enabled(true)
            .cooldownEnabled(true)
            .cooldownMillis(TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS))
            .durationEnabled(true)
            .durationMillis(TimeUnit.MILLISECONDS.convert(21, TimeUnit.DAYS))
            .pointsGoal(4000)
            .rewardCommands(Lists.newList(
                "money give " + CommonPlaceholders.PLAYER_NAME + " 30000"
            ))
            .build();

        return new Contract("advanced", definition, behavior, unlocking, promotion);
    }
}
