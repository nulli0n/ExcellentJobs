package su.nightexpress.excellentjobs.level.placeholder;

import java.util.function.Function;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.level.LevelingManager;
import su.nightexpress.nightcore.bridge.placeholder.PlaceholderProvider;
import su.nightexpress.nightcore.bridge.placeholder.PlaceholderRegistry;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class LevelingPlaceholderProvider implements PlaceholderProvider {

    private final JobManager      jobManager;
    private final LevelingManager manager;

    public LevelingPlaceholderProvider(JobManager jobManager, LevelingManager manager) {
        this.jobManager = jobManager;
        this.manager = manager;
    }

    @Override
    public void addPlaceholders(PlaceholderRegistry registry) {
        registry.registerMapped("level", Job.class, (player, job) -> {
            return this.parseData(player, job, data -> NumberUtil.format(data.getLevel()));
        });

        registry.registerMapped("xp", Job.class, (player, job) -> {
            return this.parseData(player, job, data -> NumberUtil.format(data.getXP()));
        });

        registry.registerMapped("xp_required", Job.class, (player, job) -> {
            return this.parseData(player, job, data -> NumberUtil.format(this.manager.getRequiredXP(job, data
                .getLevel())));
        });
    }

    private String parseData(Player player, Job job, Function<JobData, String> function) {
        JobData data = this.jobManager.getActiveData(player, job);
        if (data == null) return Lang.OTHER_N_A.text();

        return function.apply(data);
    }
}
