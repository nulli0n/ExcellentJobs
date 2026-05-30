package su.nightexpress.excellentjobs.zone.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.zone.ZoneManager;
import su.nightexpress.excellentjobs.zone.core.ZoneLang;
import su.nightexpress.excellentjobs.zone.core.ZonePerms;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.CommandProvider;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.command.HubCommand;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class ZoneCommands implements CommandProvider {

    private static final String ARG_ZONE = "zone";
    private static final String ARG_NAME = "name";

    public static final String DEF_ROOT_NAME   = "jobzone";
    public static final String DEF_WAND_NAME   = "wand";
    public static final String DEF_CREATE_NAME = "define";

    private final ZoneManager manager;

    private HubCommand command;

    public ZoneCommands(JobsPlugin plugin, ZoneManager manager) {
        this.manager = manager;

        this.command = NightCommand.hub(plugin, DEF_ROOT_NAME, builder -> builder
            .description(ZoneLang.COMMAND_ZONE_DESC.text())
            .permission(ZonePerms.COMMAND_ZONE)
            .branch(Commands.literal(DEF_WAND_NAME)
                .playerOnly()
                .description(ZoneLang.COMMAND_ZONE_WAND_DESC.text())
                .permission(ZonePerms.COMMAND_ZONE_WAND)
                .withArguments(Arguments.argument(ARG_ZONE, Zone.class)
                    .localized(ZoneLang.COMMAND_ARGUMENT_NAME_ZONE.text())
                    .optional()
                )
                .executes(this::giveWand)
            )
            .branch(Commands.literal(DEF_CREATE_NAME)
                .playerOnly()
                .description(ZoneLang.COMMAND_ZONE_CREATE_DESC.text())
                .permission(ZonePerms.COMMAND_ZONE_DEFINE)
                .withArguments(Arguments.string(ARG_NAME)
                    .localized(CoreLang.COMMAND_ARGUMENT_NAME_NAME.text())
                )
                .executes(this::createZone)
            )
        );
    }


    @Override
    public void provideCommands(HubNodeBuilder root) {
        this.command.register();
    }

    public void unload() {
        if (this.command != null) {
            this.command.unregister();
        }
    }

    private boolean giveWand(CommandContext context, ParsedArguments arguments) {
        Zone zone = arguments.contains(ARG_ZONE) ? arguments.getOrNull(ARG_ZONE, Zone.class) : null;
        Player player = context.getPlayerOrThrow();
        manager.startSelection(player, zone);
        return true;
    }

    private boolean createZone(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        manager.defineZone(player, arguments.getString(ARG_NAME));
        return true;
    }
}
