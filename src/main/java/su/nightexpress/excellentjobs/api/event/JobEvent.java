package su.nightexpress.excellentjobs.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.Job;

@NullMarked
public abstract class JobEvent extends Event {

    protected final Player player;
    protected final Job    job;

    protected JobEvent(boolean async, Player player, Job job) {
        super(async);
        this.player = player;
        this.job = job;
    }


    public final Player getPlayer() {
        return this.player;
    }


    public final Job getJob() {
        return this.job;
    }
}
