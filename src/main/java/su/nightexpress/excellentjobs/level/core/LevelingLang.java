package su.nightexpress.excellentjobs.level.core;

import org.bukkit.Sound;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.job.JobConstants;
import su.nightexpress.excellentjobs.level.LevelingConstants;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class LevelingLang implements LangContainer {

    private LevelingLang() {
    }

    public static final TextLocale COMMAND_LEVELS_DESC = LangEntry.builder("Leveling.Command.Levels.Desc")
        .text("Browse job levels.");

    public static final TextLocale COMMAND_LEVEL_DESC = LangEntry.builder("Leveling.Command.Level.Desc")
        .text("Level management.");

    public static final TextLocale COMMAND_LEVEL_GIVE_DESC = LangEntry.builder("Leveling.Command.Level.Give.Desc")
        .text("Add levels to player's job.");

    public static final TextLocale COMMAND_LEVEL_TAKE_DESC = LangEntry.builder("Leveling.Command.Level.Take.Desc")
        .text("Take levels from player's job.");

    public static final TextLocale COMMAND_XP_DESC = LangEntry.builder("Leveling.Command.XP.Desc")
        .text("XP management.");

    public static final TextLocale COMMAND_XP_GIVE_DESC = LangEntry.builder("Leveling.Command.XP.Give.Desc")
        .text("Add XP to player's job.");

    public static final TextLocale COMMAND_XP_TAKE_DESC = LangEntry.builder("Leveling.Command.XP.Take.Desc")
        .text("Take XP from player's job.");

    public static final MessageLocale XP_GIVE_NOTIFY = LangEntry.builder("Leveling.GiveXP.Notify").chatMessage(
        TagWrappers.GRAY.wrap(TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " XP") +
            " has been added to your " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale XP_GIVE_FEEDBACK = LangEntry.builder("Leveling.GiveXP.Feedback").chatMessage(
        TagWrappers.GRAY.wrap("You have added " + TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " XP") +
            " to " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "'s " +
            TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale XP_TAKE_NOTIFY = LangEntry.builder("Leveling.TakeXP.Notify").chatMessage(
        TagWrappers.GRAY.wrap(TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " XP") +
            " has been taken from your " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale XP_TAKE_FEEDBACK = LangEntry.builder("Leveling.TakeXP.Feedback").chatMessage(
        TagWrappers.GRAY.wrap("You have removed " + TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " XP") +
            " from " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "'s " +
            TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale LEVEL_GIVE_NOTIFY = LangEntry.builder("Leveling.GiveLevel.Notify").chatMessage(
        TagWrappers.GRAY.wrap(TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " level(s)") +
            " has been added to your " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale LEVEL_GIVE_FEEDBACK = LangEntry.builder("Leveling.GiveLevel.Feedback")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have added " +
                TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " level(s)") +
                " to " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "'s " +
                TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
        );

    public static final MessageLocale LEVEL_TAKE_NOTIFY = LangEntry.builder("Leveling.TakeLevel.Notify").chatMessage(
        TagWrappers.GRAY.wrap(TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " level(s)") +
            " has been taken from your " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
    );

    public static final MessageLocale LEVEL_TAKE_FEEDBACK = LangEntry.builder("Leveling.TakeLevel.Feedback")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have removed " +
                TagWrappers.GOLD.wrap(CommonPlaceholders.GENERIC_AMOUNT + " level(s)") +
                " from " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "'s " +
                TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job.")
        );

    public static final MessageLocale LEVEL_UP = LangEntry.builder("Leveling.Level.Up").titleMessage(
        TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Level Up!"),
        TagWrappers.GRAY.wrap("Your " +
            TagWrappers.GREEN.wrap(JobsPlaceholders.JOB_NAME) + " job is now level " +
            TagWrappers.GREEN.wrap(JobsPlaceholders.JOB_DATA_LEVEL) + "!"
        ),
        Sound.ENTITY_PLAYER_LEVELUP
    );

    public static final MessageLocale LEVEL_DOWN = LangEntry.builder("Leveling.Level.Downgrade").titleMessage(
        TagWrappers.RED.and(TagWrappers.BOLD).wrap("Level Downgrade"),
        TagWrappers.GRAY.wrap("Your " +
            TagWrappers.RED.wrap(JobsPlaceholders.JOB_NAME) + " job level downgraded to " +
            TagWrappers.RED.wrap(JobsPlaceholders.JOB_DATA_LEVEL) + "."
        ),
        Sound.ENTITY_IRON_GOLEM_DEATH
    );

    public static final MessageLocale LEVEL_REWARDS_NOTIFY = LangEntry.builder("Leveling.Rewards.Notify").message(
        MessageData.CHAT_NO_PREFIX,
        " ",
        TagWrappers.YELLOW.and(TagWrappers.BOLD).and(TagWrappers.UNDERLINED).wrap("Level Rewards"),
        " ",
        TagWrappers.GRAY.wrap("You have " + TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_AMOUNT) +
            " unclaimed rewards in " + TagWrappers.YELLOW.wrap(JobsPlaceholders.JOB_NAME) + " job."
        ),
        TagWrappers.GRAY.wrap("Click " +
            TagWrappers.RUN_COMMAND.with("/" + JobConstants.COMMAND_JOBS + " " + LevelingConstants.COMMAND_LEVELS +
                " " + JobsPlaceholders.JOB_ID)
                .wrap(TagWrappers.YELLOW.wrap("[Here]")) + " to claim now!"
        ),
        " "
    );

    public static final MessageLocale LEVEL_REWARDS_AUTO_CLAIMED = LangEntry.builder("Leveling.Rewards.AutoClaimed")
        .message(
            MessageData.CHAT_NO_PREFIX,
            " ",
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("Level Rewards:"),
            TagWrappers.DARK_GRAY.wrap("Hover over reward name for details."),
            " ",
            CommonPlaceholders.GENERIC_ENTRY,
            " "
        );

    public static final TextLocale LEVEL_REWARDS_ENTRY = LangEntry.builder("Leveling.Rewards.Entry").text(
        TagWrappers.YELLOW.wrap("✔ " +
            TagWrappers.SHOW_TEXT.with(TagWrappers.GRAY.wrap(JobsPlaceholders.REWARD_DESCRIPTION))
                .wrap(TagWrappers.GRAY.wrap(JobsPlaceholders.REWARD_NAME))
        )
    );
}
