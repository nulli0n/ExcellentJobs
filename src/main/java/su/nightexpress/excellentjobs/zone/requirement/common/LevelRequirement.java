package su.nightexpress.excellentjobs.zone.requirement.common;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class LevelRequirement implements ZoneRequirement {

    private final JobManager jobManager;

    private int min;
    private int max;

    public LevelRequirement(JobManager jobManager, int min, int max) {
        this.jobManager = jobManager;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(Player player, Job job) {
        JobData data = this.jobManager.getActiveData(player, job);
        return data != null && this.isGoodLevel(data.getLevel());
    }

    private boolean isGoodLevel(int level) {
        if (this.min >= 0 && this.min > level) return false;

        return this.max <= 0 || level < this.max;
    }

    public static class Codec implements ConfigCodec<LevelRequirement> {

        private final JobManager jobManager;

        public Codec(JobManager jobManager) {
            this.jobManager = jobManager;
        }

        @Override
        public LevelRequirement read(FileConfig config, String path) throws CodecReadException {
            int min = config.getOrSet(path + ".min", ConfigCodecs.INT, 0);
            int max = config.getOrSet(path + ".max", ConfigCodecs.INT, -1);

            return new LevelRequirement(this.jobManager, min, max);
        }

        @Override
        public void write(FileConfig config, String path, LevelRequirement value) {
            config.set(path + ".min", value.min);
            config.set(path + ".max", value.max);
        }
    }
}
