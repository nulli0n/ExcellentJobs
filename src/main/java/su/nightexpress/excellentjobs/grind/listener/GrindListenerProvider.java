package su.nightexpress.excellentjobs.grind.listener;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.GrindManager;

@NullMarked
public interface GrindListenerProvider<T> {

    GrindListener<T> provide(JobsPlugin plugin,
                             GrindManager manager,
                             @Nullable GrindProtection protection,
                             GrindType<T> type);
}
