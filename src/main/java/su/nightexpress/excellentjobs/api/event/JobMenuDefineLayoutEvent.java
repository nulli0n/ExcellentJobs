package su.nightexpress.excellentjobs.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;

@NullMarked
public class JobMenuDefineLayoutEvent extends Event {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final AbstractObjectMenu<Job> jobMenu;

    public JobMenuDefineLayoutEvent(AbstractObjectMenu<Job> jobMenu) {
        this.jobMenu = jobMenu;
    }

    public AbstractObjectMenu<Job> getJobMenu() {
        return jobMenu;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
