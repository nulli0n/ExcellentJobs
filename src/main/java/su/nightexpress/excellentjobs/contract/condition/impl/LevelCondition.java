package su.nightexpress.excellentjobs.contract.condition.impl;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.condition.UnlockCondition;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Numbers;

@NullMarked
public class LevelCondition implements UnlockCondition {

    private final JobManager jobManager;
    private final int        min;
    private final int        max;

    public LevelCondition(JobManager jobManager, int min, int max) {
        this.jobManager = jobManager;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean check(Player player, Contract contract, Job job) {
        JobData data = this.jobManager.getActiveData(player, job);
        int jobLevel = data == null ? 0 : data.getLevel();

        return jobLevel >= min && (max <= 0 || jobLevel <= max);
    }

    public static class Codec implements ConfigCodec<LevelCondition> {

        private final JobManager jobManager;

        public Codec(JobManager jobManager) {
            this.jobManager = jobManager;
        }

        @Override
        public LevelCondition read(FileConfig config, String path) throws CodecReadException {
            String raw = config.getString(path);
            if (raw == null) return new LevelCondition(this.jobManager, 0, -1);

            String[] split = raw.trim().split("-");

            int min = Numbers.getIntegerAbs(split[0]);
            int max = split.length >= 2 ? Numbers.getIntegerAbs(split[1]) : -1;

            return new LevelCondition(this.jobManager, min, max);
        }

        @Override
        public void write(FileConfig config, String path, LevelCondition value) {
            config.set(path, "%s-%s".formatted(value.min, value.max));
        }
    }

}
