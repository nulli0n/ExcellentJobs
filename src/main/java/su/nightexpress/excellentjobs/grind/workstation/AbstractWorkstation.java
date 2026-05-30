package su.nightexpress.excellentjobs.grind.workstation;

import org.bukkit.block.TileState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractWorkstation<B extends TileState> implements Workstation {

    protected final WorkstationType type;
    protected final B               backend;

    protected AbstractWorkstation(WorkstationType type, B backend) {
        this.type = type;
        this.backend = backend;
    }

    @Override
    public void update() {
        this.backend.update(true, false);
    }

    @Override
    public WorkstationType getType() {
        return this.type;
    }

    @Override
    public @NonNull B getBackend() {
        return this.backend;
    }
}
