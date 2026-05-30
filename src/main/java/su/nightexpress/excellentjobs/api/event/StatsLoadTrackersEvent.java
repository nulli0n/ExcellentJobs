package su.nightexpress.excellentjobs.api.event;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.stats.TopTracker;

@NullMarked
public class StatsLoadTrackersEvent extends Event {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<TopTracker> trackers;

    public StatsLoadTrackersEvent(List<TopTracker> trackers) {
        this.trackers = trackers;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public List<TopTracker> getTrackers() {
        return trackers;
    }
}
