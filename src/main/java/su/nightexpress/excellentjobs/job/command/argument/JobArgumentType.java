package su.nightexpress.excellentjobs.job.command.argument;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

@NullMarked
public class JobArgumentType implements ArgumentType<Job>, SuggestionsProvider {

    private final JobManager manager;

    public JobArgumentType(JobManager manager) {
        this.manager = manager;
    }

    @Override
    public Job parse(CommandContextBuilder contextBuilder, String string) throws CommandSyntaxException {
        Job job = this.manager.getJobById(string);
        if (job == null) throw CommandSyntaxException.custom(Lang.ERROR_COMMAND_INVALID_JOB_ARGUMENT);

        return job;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return this.manager.getJobIds();
    }
}
