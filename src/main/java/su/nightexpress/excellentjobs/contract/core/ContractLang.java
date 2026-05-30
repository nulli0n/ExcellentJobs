package su.nightexpress.excellentjobs.contract.core;

import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.contract.ContractPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class ContractLang implements LangContainer {

    private static final String PREFIX = "Contract.";

    private ContractLang() {
    }

    public static LangEntry.@NonNull Builder builder(@NonNull String path) {
        return LangEntry.builder(PREFIX + path);
    }

    public static final MessageLocale CONTRACT_SELECTION_JOB_NOTHING = builder("Selection.JobNothing").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.GRAY.wrap("There are no contracts available for the " +
            TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale CONTRACT_ACTIVATION_NOT_AVAILABLE = builder("Activation.NotAvailable")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You do not met requirements for the " + TagWrappers.RED.wrap(
                ContractPlaceholders.CONTRACT_NAME) + " contract.")
        );

    public static final MessageLocale CONTRACT_ACTIVATION_COOLDOWN = builder("Activation.Cooldown").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.SOFT_RED.wrap("You still have to work for another " +
            TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_TIME) + " in this contract before you can change it.")
    );

    public static final MessageLocale CONTRACT_ACTIVATION_SUCCESS = builder("Activation.Success")
        .titleMessage(
            TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("CONTRACT: ") + TagWrappers.WHITE.wrap(
                ContractPlaceholders.CONTRACT_NAME),
            TagWrappers.GRAY.wrap(TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) +
                " job contract successfully activated."),
            20, 60,
            Sound.ENTITY_PLAYER_LEVELUP
        );

    public static final MessageLocale JOB_LEAVE_ACTIVE_CONTRACT = builder("JobLeave.ActiveContract").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.SOFT_RED.wrap("You must cancel your current job contract first before you can quit a job.")
    );

    public static final MessageLocale CONTRACT_CANCEL_COOLDOWN = builder("Cancellation.Cooldown").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.SOFT_RED.wrap("You still have to work for another " +
            TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_TIME) + " in this contract before you can cancel it.")
    );


    public static final MessageLocale CONTRACT_CANCEL_SUCCESS = builder("Cancellation.Success")
        .titleMessage(
            TagWrappers.RED.and(TagWrappers.BOLD).wrap("CONTRACT: ") + TagWrappers.WHITE.wrap("Cancelled"),
            TagWrappers.GRAY.wrap(TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) +
                " job contract successfully cancelled."),
            20, 60,
            Sound.ENTITY_GENERIC_EXPLODE
        );

    public static final MessageLocale CONTRACT_PROMOTION_START_DISABLED = builder("Promotion.Start.Disabled")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("This contract does not allows promotions.")
        );

    public static final MessageLocale CONTRACT_PROMOTION_START_COOLDOWN = builder("Promotion.Start.Cooldown")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can start promotion again in " +
                TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_TIME))
        );

    public static final MessageLocale CONTRACT_PROMOTION_START_ALREADY = builder("Promotion.Start.Already")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You're already in promotion of this contract.")
        );

    public static final MessageLocale CONTRACT_PROMOTION_START_COMPLETED = builder("Promotion.Start.Completed")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You have already completed promotion of this contract.")
        );

    public static final MessageLocale CONTRACT_PROMOTION_START_SUCCESS = builder("Promotion.Start.Success")
        .titleMessage(
            TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Contract Promotion"),
            TagWrappers.GRAY.wrap("Started " + TagWrappers.WHITE.wrap(ContractPlaceholders.CONTRACT_NAME) +
                " contract promotion for " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job."),
            20, 80,
            Sound.ENTITY_PLAYER_LEVELUP
        );

    public static final MessageLocale CONTRACT_PROMOTION_START_DETAILS = builder("Promotion.Start.Details")
        .message(MessageData.CHAT_NO_PREFIX,
            "",
            TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("PROMOTION INFO:"),
            TagWrappers.DARK_GRAY.wrap(" » ") + TagWrappers.GRAY.wrap("Points Required: " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_AMOUNT)),
            TagWrappers.DARK_GRAY.wrap(" » ") + TagWrappers.GRAY.wrap("Timeleft: " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_TIME)),
            ""
        );

    public static final MessageLocale CONTRACT_PROMOTION_RESULT_FAIL = builder("Promotion.Result.Failure")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have failed " + TagWrappers.RED.wrap(ContractPlaceholders.CONTRACT_NAME) +
                " contract promotion for the " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
        );

    public static final MessageLocale CONTRACT_PROMOTION_RESULT_COMPLETED = builder("Promotion.Result.Completed")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have completed " + TagWrappers.GREEN.wrap(ContractPlaceholders.CONTRACT_NAME) +
                " contract promotion for the " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job!")
        );

    public static final MessageLocale ERROR_NO_ACTIVE_CONTRACT = builder("Error.NoActiveContract")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You do not have an active contract.")
        );


    public static final TextLocale OTHER_COST_DELIMITER = builder("Other.CostDelimiter").text(", ");
}
