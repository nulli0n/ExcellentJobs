package su.nightexpress.excellentjobs.data;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.nightcore.db.AbstractDatabaseManager;

public class Database extends AbstractDatabaseManager<JobsPlugin> {

    public Database(@NonNull JobsPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onClose() {

    }

    @Override
    protected void onInitialize() {

    }

    @Override
    public void onPurge() {

    }

    @Override
    public void onSynchronize() {
        this.synchronizer.syncAll();
    }
}
