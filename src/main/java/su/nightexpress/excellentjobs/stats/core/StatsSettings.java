package su.nightexpress.excellentjobs.stats.core;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.db.data.DataSettings;

public class StatsSettings extends AbstractConfig implements DataSettings {

    private final ConfigProperty<String> dataTableName = this.addProperty(ConfigCodecs.STRING,
        "Data.Table-Name",
        "job_stats"
    );

    private final ConfigProperty<Integer> dataCacheDuration = this.addProperty(ConfigCodecs.INT,
        "Data.Cache-Duration",
        3600
    );

    private final ConfigProperty<Integer> dataSaveInterval = this.addProperty(ConfigCodecs.INT,
        "Data.Save-Interval",
        5
    );

    private final ConfigProperty<Integer> topTrackerUpdateInterval = this.addProperty(ConfigCodecs.INT,
        "Stats.Top-Trackers.Update-Interval",
        900
    );

    public String getDataTableName() {
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

    public int getTopTrackerUpdateInterval() {
        return this.topTrackerUpdateInterval.get();
    }
}
