package su.nightexpress.excellentjobs.zone.core;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.zone.command.ZoneCommands;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class ZoneLang implements LangContainer {

    private ZoneLang() {
    }

    public static final TextLocale COMMAND_ARGUMENT_NAME_ZONE = LangEntry.builder(
        "Command.Argument.Name.Zone").text("zone");

    public static final TextLocale COMMAND_ZONE_DESC = LangEntry.builder("Command.Zone.Desc")
        .text("Zone commands.");

    public static final TextLocale COMMAND_ZONE_CREATE_DESC = LangEntry.builder("Command.Zone.Create.Desc")
        .text("Create a new zone from selection.");

    public static final TextLocale COMMAND_ZONE_WAND_DESC = LangEntry.builder("Command.Zone.Wand.Desc")
        .text("Get zone selection tool.");

    public static final MessageLocale ZONE_NOT_AVAILABLE = LangEntry.builder("Zone.Info.NotAvailable").actionBarMessage(
        TagWrappers.RED.wrap("You can't work in this zone currently!")
    );

    public static final MessageLocale ZONE_NO_PVP = LangEntry.builder("Zone.Info.NoPvP").actionBarMessage(
        TagWrappers.RED.wrap("PvP is disabled in this zone!")
    );

    public static final MessageLocale ZONE_DEFINE_BAD_NAME = LangEntry.builder("Zone.Creation.BadName").chatMessage(
        TagWrappers.GRAY.wrap("The name you specified can not be used for zone ID.")
    );

    public static final MessageLocale ZONE_DEFINE_EXISTS = LangEntry.builder("Zone.Error.AlreadyExists").chatMessage(
        TagWrappers.GRAY.wrap("There is already a zone created with the specified ID " +
            TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_NAME) + ".")
    );

    public static final MessageLocale ZONE_DEFINE_INCOMPLETE_SELECTION = LangEntry.builder(
        "Zone.Error.IncompleteSelection").chatMessage(
            TagWrappers.GRAY.wrap("You must select 2 region corners to define a zone region!")
        );

    public static final MessageLocale ZONE_DEFINE_CREATED = LangEntry.builder("Zone.Creation.Success").chatMessage(
        TagWrappers.GRAY.wrap("You have successfully created " +
            TagWrappers.GREEN.wrap(JobsPlaceholders.ZONE_NAME) + " job zone!")
    );

    public static final MessageLocale ZONE_DEFINE_OVERRIDE = LangEntry.builder("Zone.Creation.Override").chatMessage(
        TagWrappers.GRAY.wrap("You have successfully updated region of " +
            TagWrappers.GREEN.wrap(JobsPlaceholders.ZONE_NAME) + " zone!")
    );

    public static final MessageLocale ZONE_CREATE_INFO = LangEntry.builder("Zone.Creation.Info").chatMessage(
        TagWrappers.GRAY.wrap("Select two corners and use " + TagWrappers.GREEN.wrap("/" + ZoneCommands.DEF_ROOT_NAME +
            " " +
            ZoneCommands.DEF_CREATE_NAME) + " to create a new zone.")
    );

    public static final MessageLocale ZONE_SELECTION_INFO = LangEntry.builder("Zone.Selection.Info").chatMessage(
        TagWrappers.GRAY.wrap("Selected " + TagWrappers.YELLOW.wrap("#" + CommonPlaceholders.GENERIC_VALUE) +
            " zone position.")
    );

    public static final MessageLocale ERROR_COMMAND_INVALID_ZONE_ARGUMENT = LangEntry.builder(
        "Command.Syntax.InvalidZoneArgument")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + " is not a valid zone!"));
}
