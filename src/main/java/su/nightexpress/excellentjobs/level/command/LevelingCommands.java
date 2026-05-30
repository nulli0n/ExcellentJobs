package su.nightexpress.excellentjobs.level.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.level.LevelingManager;
import su.nightexpress.excellentjobs.level.core.LevelingLang;
import su.nightexpress.excellentjobs.level.core.LevelingPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.CommandProvider;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Lists;

@NullMarked
public class LevelingCommands implements CommandProvider {

    private static final String ARG_JOB    = "job";
    private static final String ARG_PLAYER = "player";
    private static final String ARG_AMOUNT = "amount";

    private static final String FLAG_SILENT = "s";

    private final LevelingManager manager;

    public LevelingCommands(LevelingManager manager) {
        this.manager = manager;
    }

    @Override
    public void provideCommands(HubNodeBuilder root) {
        root
            .branch(Commands.literal("levels")
                .playerOnly()
                .description(LevelingLang.COMMAND_LEVELS_DESC.text())
                .permission(LevelingPerms.COMMAND_LEVELS)
                .withArguments(
                    Arguments.argument(ARG_JOB, Job.class)
                        .localized(Lang.COMMAND_ARGUMENT_NAME_JOB.text())
                )
                .executes(this::viewLevels)
            )
            .branch(Commands.hub("level")
                .description(LevelingLang.COMMAND_LEVEL_DESC.text())
                .permission(LevelingPerms.COMMAND_LEVEL)
                .branch(Commands.literal("give")
                    .withArguments(
                        Arguments.playerName(ARG_PLAYER),
                        Arguments.argument(ARG_JOB, Job.class)
                            .localized(Lang.COMMAND_ARGUMENT_NAME_JOB.text()),
                        Arguments.integer(ARG_AMOUNT, 1)
                            .localized(CoreLang.COMMAND_ARGUMENT_NAME_AMOUNT.text())
                            .suggestions((reader, context) -> Lists.newList("1", "2", "3", "4", "5"))
                    )
                    .withFlags(FLAG_SILENT)
                    .executes(this::giveLevel)
                )
                .branch(Commands.literal("take")
                    .withArguments(
                        Arguments.playerName(ARG_PLAYER),
                        Arguments.argument(ARG_JOB, Job.class)
                            .localized(Lang.COMMAND_ARGUMENT_NAME_JOB.text()),
                        Arguments.integer(ARG_AMOUNT, 1)
                            .localized(CoreLang.COMMAND_ARGUMENT_NAME_AMOUNT.text())
                            .suggestions((reader, context) -> Lists.newList("1", "2", "3", "4", "5"))

                    )
                    .withFlags(FLAG_SILENT)
                    .executes(this::takeLevel)
                )
            )
            .branch(Commands.hub("xp")
                .description(LevelingLang.COMMAND_XP_DESC.text())
                .permission(LevelingPerms.COMMAND_XP)
                .branch(Commands.literal("give")
                    .withArguments(
                        Arguments.playerName(ARG_PLAYER),
                        Arguments.argument(ARG_JOB, Job.class)
                            .localized(Lang.COMMAND_ARGUMENT_NAME_JOB.text()),
                        Arguments.integer(ARG_AMOUNT, 1)
                            .localized(CoreLang.COMMAND_ARGUMENT_NAME_AMOUNT.text())
                            .suggestions((reader, context) -> Lists.newList("10", "20", "30", "40", "50"))

                    )
                    .withFlags(FLAG_SILENT)
                    .executes(this::giveXP)
                )
                .branch(Commands.literal("take")
                    .withArguments(
                        Arguments.playerName(ARG_PLAYER),
                        Arguments.argument(ARG_JOB, Job.class)
                            .localized(Lang.COMMAND_ARGUMENT_NAME_JOB.text()),
                        Arguments.integer(ARG_AMOUNT, 1)
                            .localized(CoreLang.COMMAND_ARGUMENT_NAME_AMOUNT.text())
                            .suggestions((reader, context) -> Lists.newList("10", "20", "30", "40", "50"))

                    )
                    .withFlags(FLAG_SILENT)
                    .executes(this::takeXP)
                )
            );
    }

    private boolean viewLevels(CommandContext context, ParsedArguments arguments) {
        Job job = arguments.get(ARG_JOB, Job.class);
        Player player = context.getPlayerOrThrow();

        return this.manager.showLevels(player, job);
    }

    private boolean giveLevel(CommandContext context, ParsedArguments arguments) {
        int amount = arguments.getInt(ARG_AMOUNT);
        Job job = arguments.get(ARG_JOB, Job.class);
        String playerName = arguments.getString(ARG_PLAYER, context.getSender().getName());
        boolean silent = context.hasFlag(FLAG_SILENT);

        this.manager.giveLevel(context.getSender(), playerName, job, amount, silent);
        return true;
    }

    private boolean takeLevel(CommandContext context, ParsedArguments arguments) {
        int amount = arguments.getInt(ARG_AMOUNT);
        Job job = arguments.get(ARG_JOB, Job.class);
        String playerName = arguments.getString(ARG_PLAYER, context.getSender().getName());
        boolean silent = context.hasFlag(FLAG_SILENT);

        this.manager.takeLevel(context.getSender(), playerName, job, amount, silent);
        return true;
    }

    private boolean giveXP(CommandContext context, ParsedArguments arguments) {
        double amount = arguments.getDouble(ARG_AMOUNT);
        Job job = arguments.get(ARG_JOB, Job.class);
        String playerName = arguments.getString(ARG_PLAYER, context.getSender().getName());
        boolean silent = context.hasFlag(FLAG_SILENT);

        this.manager.giveXP(context.getSender(), playerName, job, amount, silent);
        return true;
    }

    private boolean takeXP(CommandContext context, ParsedArguments arguments) {
        double amount = arguments.getDouble(ARG_AMOUNT);
        Job job = arguments.get(ARG_JOB, Job.class);
        String playerName = arguments.getString(ARG_PLAYER, context.getSender().getName());
        boolean silent = context.hasFlag(FLAG_SILENT);

        this.manager.takeXP(context.getSender(), playerName, job, amount, silent);
        return true;
    }
}
