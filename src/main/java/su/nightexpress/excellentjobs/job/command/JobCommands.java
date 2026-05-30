package su.nightexpress.excellentjobs.job.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.job.JobConstants;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.core.JobLang;
import su.nightexpress.excellentjobs.job.core.JobPerms;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.CommandProvider;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;

@NullMarked
public class JobCommands implements CommandProvider {

    private static final String ARG_JOB = "job";

    private final JobManager manager;

    public JobCommands(JobManager manager) {
        this.manager = manager;
    }

    @Override
    public void provideCommands(HubNodeBuilder root) {
        root
            .branch(Commands.literal(JobConstants.COMMAND_MENU)
                .playerOnly()
                .description(JobLang.COMMAND_MENU_DESC.text())
                .permission(JobPerms.COMMAND_MENU)
                .executes(this::handleMenu)
            )
            .branch(Commands.literal("join")
                .playerOnly()
                .description(JobLang.COMMAND_JOIN_DESC.text())
                .permission(JobPerms.COMMAND_JOIN)
                .withArguments(Arguments.argument(ARG_JOB, Job.class)
                    .localized(Lang.COMMAND_ARGUMENT_NAME_JOB)
                )
                .executes(this::handleJoin)
            )
            .branch(Commands.literal("leave")
                .playerOnly()
                .description(JobLang.COMMAND_LEAVE_DESC.text())
                .permission(JobPerms.COMMAND_LEAVE)
                .withArguments(Arguments.argument(ARG_JOB, Job.class)
                    .localized(Lang.COMMAND_ARGUMENT_NAME_JOB)
                )
                .executes(this::handleLeave)
            );

        root.executes(this::handleMenu);
    }

    private boolean handleJoin(CommandContext context, ParsedArguments arguments) {
        Job job = arguments.get(ARG_JOB, Job.class);
        Player player = context.getPlayerOrThrow();

        return this.manager.joinJob(player, job);
    }

    private boolean handleLeave(CommandContext context, ParsedArguments arguments) {
        Job job = arguments.get(ARG_JOB, Job.class);
        Player player = context.getPlayerOrThrow();

        return this.manager.leaveJob(player, job);
    }

    private boolean handleMenu(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();

        this.manager.showJobsMenu(player);
        return true;
    }
}
