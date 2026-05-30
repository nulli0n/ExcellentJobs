package su.nightexpress.excellentjobs.job.core;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.db.data.DataSettings;
import su.nightexpress.nightcore.util.RankTable;

@NullMarked
public class JobSettings extends AbstractConfig implements DataSettings {

    private final ConfigProperty<String> dataTableName = this.addProperty(ConfigCodecs.STRING,
        "Data.Table-Name",
        "job_data"
    );

    private final ConfigProperty<Integer> dataCacheDuration = this.addProperty(ConfigCodecs.INT,
        "Data.Cache-Duration",
        3600
    );

    private final ConfigProperty<Integer> dataSaveInterval = this.addProperty(ConfigCodecs.INT,
        "Data.Save-Interval", 5
    );

    private final ConfigProperty<RankTable> jobAmountLimits = this.addProperty(
        ConfigCodecs.RANK_TABLE,
        "Job.Amount-Limit",
        RankTable.ranked(3)
            .permissionPrefix("jobs.amount.")
            .addRankValue("pro", 4)
            .addRankValue("elite", 5)
            .build(),
        ""
    );

    private final ConfigProperty<Boolean> jobLeaveCooldownEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Job.Leave-Cooldown.Enabled",
        true,
        "Controls whether players required to work for certain time in job before they can leave."
    );

    private final ConfigProperty<Long> jobLeaveCooldownValue = this.addProperty(
        ConfigCodecs.LONG,
        "Job.Leave-Cooldown.Value",
        3600L,
        "Sets cooldown value."
    );

    private final ConfigProperty<Integer> jobsMenuPages = this.addProperty(
        ConfigCodecs.INT,
        "Job.Menu-Pages",
        1,
        "Sets amount of pages for the Jobs GUI."
    );

    private final ConfigProperty<Integer> jobEmployeeCountUpdateInterval = this.addProperty(
        ConfigCodecs.INT,
        "Job.Employee-Count-Update-Interval",
        900,
        "Controls how often (in seconds) employees counts are updated."
    );

    public String getTableName() {
        return this.dataTableName.get();
    }

    @Override
    public int getCacheTimeDuration() {
        return this.dataCacheDuration.get();
    }

    @Override
    public @NonNull TimeUnit getCacheTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public int getSaveInterval() {
        return this.dataSaveInterval.get();
    }

    public RankTable getJobAmountLimits() {
        return this.jobAmountLimits.get();
    }

    public boolean isLeaveCooldownEnabled() {
        return this.jobLeaveCooldownEnabled.get();
    }

    public long getLeaveCooldownValue() {
        return this.jobLeaveCooldownValue.get();
    }

    public int getJobsMenuPages() {
        return this.jobsMenuPages.get();
    }

    public int getJobEmployeeCountUpdateInterval() {
        return this.jobEmployeeCountUpdateInterval.get();
    }
}
