package su.nightexpress.excellentjobs.zone.selection;

import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

public class Selection {

    private BlockPos first;
    private BlockPos second;

    public void clear() {
        this.setFirst(null);
        this.setSecond(null);
    }

    public boolean isIncompleted() {
        return !this.isCompleted();
    }

    public boolean isCompleted() {
        return this.first != null && this.second != null;
    }

    public @Nullable BlockPos getFirst() {
        return first;
    }

    public void setFirst(@Nullable BlockPos first) {
        this.first = first;
    }

    public @Nullable BlockPos getSecond() {
        return second;
    }

    public void setSecond(@Nullable BlockPos second) {
        this.second = second;
    }

    public @Nullable Cuboid toCuboid() {
        return this.isIncompleted() ? null : new Cuboid(this.first, this.second);
    }
}
