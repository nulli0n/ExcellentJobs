package su.nightexpress.excellentjobs.api.stats;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public interface TopTracker {

    void update();

    @Nullable
    TopEntry getTop(Job job, int position);

    @Nullable
    TopEntry getTop(Job job, UUID playerId);

    String getId();

    String getName();

    NightItem getIcon();

    List<String> getDescription();

    String getPlaceholderName();

}
