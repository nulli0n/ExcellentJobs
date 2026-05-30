package su.nightexpress.excellentjobs.job.core;

import org.bukkit.Sound;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class JobLang implements LangContainer {

    private JobLang() {
    }

    public static final LangEntry.Builder builder(String path) {
        return LangEntry.builder("Job." + path);
    }

    public static final TextLocale COMMAND_JOIN_DESC = LangEntry.builder("Command.Join.Desc")
        .text("Join a job.");

    public static final TextLocale COMMAND_LEAVE_DESC = LangEntry.builder("Command.Leave.Desc")
        .text("Leave a job.");

    public static final TextLocale COMMAND_MENU_DESC = LangEntry.builder("Command.Menu.Desc")
        .text("Open Jobs GUI.");

    public static final MessageLocale JOB_JOIN_ALREADY = LangEntry.builder("Job.Join.AlreadyHired").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.GRAY.wrap("You're already hired for the " +
            TagWrappers.RED.wrap(JobsPlaceholders.JOB_NAME) + " job!")
    );

    public static final MessageLocale JOB_LEAVE_COOLDOWN = LangEntry.builder("Job.Leave.Cooldown").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.GRAY.wrap("You still have to work for another " +
            TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_TIME) + " before you can leave " +
            TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job!")
    );

    public static final MessageLocale JOB_JOIN_LIMIT_REACHED = LangEntry.builder("Job.Join.LimitReached").chatMessage(
        Sound.ENTITY_VILLAGER_NO,
        TagWrappers.GRAY.wrap("You can't get more than " +
            TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_AMOUNT) + " jobs!")
    );

    public static final MessageLocale JOB_JOIN_SUCCESS = LangEntry.builder("Job.Join.Success").chatMessage(
        Sound.ENTITY_PLAYER_LEVELUP,
        TagWrappers.GRAY.wrap("You have got the " +
            TagWrappers.GREEN.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale JOB_LEAVE_SUCCESS = LangEntry.builder("Job.Leave.Success").chatMessage(
        Sound.BLOCK_WOODEN_DOOR_CLOSE,
        TagWrappers.GRAY.wrap("You have quit your " +
            TagWrappers.GREEN.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );
}
