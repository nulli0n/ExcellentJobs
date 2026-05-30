package su.nightexpress.excellentjobs.job.dialog;

import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobInfo;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public class JobDialogKeys {

    private JobDialogKeys() {
    }

    public static final DialogKey<Job>     JOB_JOIN = new DialogKey<>("job_join_confirm");
    public static final DialogKey<JobInfo> JOB_QUIT = new DialogKey<>("job_quit_confirm");
}
