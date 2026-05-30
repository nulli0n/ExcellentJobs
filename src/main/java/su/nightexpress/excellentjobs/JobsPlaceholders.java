package su.nightexpress.excellentjobs;

import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.level.reward.LevelReward;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class JobsPlaceholders extends su.nightexpress.nightcore.util.Placeholders {

    public static final String URL_WIKI = "https://nightexpressdev.com/excellentjobs/";

    public static final String GENERIC_PROGRESS = "%progress%";
    public static final String GENERIC_TYPE     = "%type%";
    public static final String GENERIC_REWARD   = "%reward%";
    public static final String GENERIC_LEVEL    = "%level%";
    public static final String GENERIC_REQUIRED = "%required%";

    public static final String JOB_DATA_LEVEL = "%job_level%";
    public static final String JOB_DATA_XP    = "%job_xp%";

    public static final String JOB_ID          = "%job_id%";
    public static final String JOB_NAME        = "%job_name%";
    public static final String JOB_DESCRIPTION = "%job_description%";

    public static final String ZONE_ID          = "%zone_id%";
    public static final String ZONE_NAME        = "%zone_name%";
    public static final String ZONE_DESCRIPTION = "%zone_description%";

    public static final String REWARD_NAME        = "%reward_name%";
    public static final String REWARD_DESCRIPTION = "%reward_description%";

    public static final TypedPlaceholder<Job> JOB = TypedPlaceholder.builder(Job.class)
        .with(JOB_ID, Job::getId)
        .with(JOB_NAME, Job::getName)
        .with(JOB_DESCRIPTION, job -> String.join(TagWrappers.BR, job.getDescription()))
        .build();

    public static final TypedPlaceholder<JobData> JOB_DATA = TypedPlaceholder.builder(JobData.class)
        .with(JOB_DATA_LEVEL, data -> NumberUtil.format(data.getLevel()))
        .with(JOB_DATA_XP, data -> NumberUtil.format(data.getXP()))
        .build();

    public static final TypedPlaceholder<LevelReward> LEVEL_REWARD = TypedPlaceholder.builder(LevelReward.class)
        .with(REWARD_NAME, LevelReward::getName)
        .with(REWARD_DESCRIPTION, reward -> String.join(TagWrappers.BR, reward.getDescription()))
        .build();

    public static final TypedPlaceholder<Zone> ZONE = TypedPlaceholder.builder(Zone.class)
        .with(ZONE_ID, Zone::getId)
        .with(ZONE_NAME, Zone::getName)
        .with(ZONE_DESCRIPTION, zone -> String.join(TagWrappers.BR, zone.getDescription()))
        .build();
}
