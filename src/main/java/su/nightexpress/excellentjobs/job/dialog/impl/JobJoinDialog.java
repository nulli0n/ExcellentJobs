package su.nightexpress.excellentjobs.job.dialog.impl;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.core.JobLang;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class JobJoinDialog extends Dialog<Job> {

    private static final TextLocale TITLE = JobLang.builder("Dialog.Join.Title")
        .text(title("Job", "Joining"));

    private static final DialogElementLocale BODY = JobLang.builder("Dialog.Join.Body")
        .dialogElement(
            400,
            "You're about to join the " + TagWrappers.YELLOW.wrap(JobsPlaceholders.JOB_NAME) + " job.",
            "",
            TagWrappers.ORANGE.wrap("Please review the job details before confirming:"),
            "",
            JobsPlaceholders.JOB_DESCRIPTION,
            "",
            TagWrappers.GREEN.wrap("After confirming, you'll be prompted to select a job contract.")
        );

    private final JobManager manager;

    public JobJoinDialog(JobManager manager) {
        this.manager = manager;
    }

    @Override
    public WrappedDialog create(Player player, Job job) {
        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(job.placeholders())
            .build();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY.replace(placeholderContext)))
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                viewer.callback();
                this.manager.joinJob(player, job);
            });
        });
    }
}
