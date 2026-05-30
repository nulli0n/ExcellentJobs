package su.nightexpress.excellentjobs.job.dialog.impl;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.core.JobLang;
import su.nightexpress.excellentjobs.job.model.JobInfo;
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
public class JobQuitConfirmDialog extends Dialog<JobInfo> {

    private static final TextLocale TITLE = JobLang.builder("Dialog.Quit.Title")
        .text(title("Job", "Quit"));

    private static final DialogElementLocale BODY = JobLang.builder("Dialog.Quit.Body")
        .dialogElement(
            400,
            "You're about to quit the " + TagWrappers.YELLOW.wrap(JobsPlaceholders.JOB_NAME) + " job.",
            "",
            TagWrappers.SOFT_RED.wrap("Please review the terms and conditions before confirming:"),
            "",
            TagWrappers.RED.wrap("- All leveling progress will be reset;"),
            TagWrappers.RED.wrap("- Your stats for this job will be excluded from leaderboards;"),
            TagWrappers.RED.wrap("- You will no longer be able to work in certain job zones;")
        );

    private final JobManager manager;

    public JobQuitConfirmDialog(JobManager manager) {
        this.manager = manager;
    }

    @Override
    public WrappedDialog create(Player player, JobInfo info) {
        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(info.data().placeholders())
            .with(info.job().placeholders())
            .build();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY.replace(placeholderContext)))
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                this.manager.leaveJob(player, info.job());
                viewer.callback();
            });
        });
    }
}
