package su.nightexpress.excellentjobs.grind.listener;

import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindContext;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;
import su.nightexpress.nightcore.manager.AbstractListener;

@NullMarked
public abstract class GrindListener<T> extends AbstractListener<JobsPlugin> {

    protected final GrindManager              manager;
    protected final GrindType<T>              type;
    protected final @Nullable GrindProtection protection;

    protected GrindListener(JobsPlugin plugin,
                            GrindManager manager,
                            @Nullable GrindProtection protection,
                            GrindType<T> type) {
        super(plugin);
        this.manager = manager;
        this.type = type;
        this.protection = protection;
    }

    protected void giveXP(Player player, T source, GrindContext context) {
        this.manager.proceedObjectives(player, this.type, source, context);
    }

    protected boolean checkProtection(Predicate<GrindProtection> predicate) {
        return this.protection == null || predicate.test(this.protection);
    }
}
