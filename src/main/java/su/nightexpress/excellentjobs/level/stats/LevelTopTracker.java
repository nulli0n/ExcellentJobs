package su.nightexpress.excellentjobs.level.stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.stats.TopEntry;
import su.nightexpress.excellentjobs.api.stats.TopTracker;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.level.core.LevelingSettings;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class LevelTopTracker implements TopTracker {

    private final UserDataManager  userManager;
    private final JobManager       manager;
    private final LevelingSettings settings;

    private final Map<String, List<TopEntry>>      entriesByJob;
    private final Map<String, Map<UUID, TopEntry>> entriesById;

    public LevelTopTracker(UserDataManager userManager, JobManager manager, LevelingSettings settings) {
        this.userManager = userManager;
        this.manager = manager;
        this.settings = settings;

        this.entriesByJob = new HashMap<>();
        this.entriesById = new HashMap<>();
    }

    @Override
    public String getId() {
        return "level";
    }

    @Override
    public String getName() {
        return this.settings.getLeaderboardTrackerName();
    }

    @Override
    public List<String> getDescription() {
        return this.settings.getLeaderboardTrackerDescription();
    }

    @Override
    public NightItem getIcon() {
        return this.settings.getLeaderboardTrackerIcon();
    }

    @Override
    public String getPlaceholderName() {
        return "level";
    }

    @Override
    public @Nullable TopEntry getTop(Job job, int position) {
        if (position <= 0) return null;

        List<TopEntry> entries = this.entriesByJob.get(job.getId());
        if (entries == null || entries.size() < position) return null;

        return entries.get(position - 1);
    }

    @Override
    public @Nullable TopEntry getTop(Job job, UUID playerId) {
        return this.entriesById.getOrDefault(job.getId(), Map.of()).get(playerId);
    }

    @Override
    public void update() {
        this.entriesByJob.clear();

        Map<String, Map<UUID, Integer>> levels = this.manager.getJobLevels();

        levels.forEach((jobId, levelMap) -> {
            AtomicInteger counter = new AtomicInteger(levelMap.size());
            List<TopEntry> entries = new ArrayList<>();

            levelMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .forEach(entry -> {
                    UserData user = this.userManager.loadByIdAndCache(entry.getKey()).orElse(null);
                    if (user == null) return;

                    int position = counter.getAndDecrement();
                    TopEntry topEntry = new TopEntry(user, NumberUtil.format(entry.getValue()), position);
                    entries.add(0, topEntry);

                    this.entriesById.computeIfAbsent(jobId, k -> new HashMap<>()).put(user.getId(), topEntry);
                });

            this.entriesByJob.put(jobId, entries);
        });
    }
}
