package su.nightexpress.excellentjobs.config;

import org.bukkit.Sound;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.EnumLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class Lang implements LangContainer {

    private Lang() {
    }

    public static final EnumLocale<GrindObjectiveProperty> OBJECTIVE_PROPERTY = LangEntry
        .builder("GrindObjectiveProperty")
        .enumeration(GrindObjectiveProperty.class);

    public static final TextLocale COMMAND_ARGUMENT_NAME_JOB = LangEntry.builder("Command.Argument.Name.Job")
        .text("job");

    public static final TextLocale COMMAND_RESET_DESC = LangEntry.builder("Command.Reset.Desc")
        .text("Reset job progress.");

    public static final MessageLocale COMMAND_RESET_DONE = LangEntry.builder("Command.Reset.Done")
        .chatMessage(
            TagWrappers.GRAY.wrap("Successfully reset " + TagWrappers.YELLOW.wrap(JobsPlaceholders.JOB_NAME) +
                " progress for " + TagWrappers.YELLOW.wrap(CommonPlaceholders.PLAYER_NAME) + ".")
        );

    public static final MessageLocale JOB_ERROR_NO_PERMISSION = LangEntry.builder("Job.Error.NoPermission")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You do not have access to the " +
                TagWrappers.RED.wrap(JobsPlaceholders.JOB_NAME) + " job.")
        );

    public static final MessageLocale JOB_ERROR_NOT_EMPLOYED = LangEntry.builder("Job.Error.NotEmployed")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You are not employed for the " +
                TagWrappers.RED.wrap(JobsPlaceholders.JOB_NAME) + " job.")
        );

    public static final MessageLocale ERROR_COMMAND_INVALID_JOB_ARGUMENT = LangEntry.builder(
        "Command.Syntax.InvalidJobArgument")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + " is not a valid job!")
        );

    public static final TextLocale OTHER_N_A = LangEntry.builder("Other.NA").text("N/A");
}
