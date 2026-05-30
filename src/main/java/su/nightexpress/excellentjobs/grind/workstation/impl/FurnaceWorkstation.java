package su.nightexpress.excellentjobs.grind.workstation.impl;

import org.bukkit.block.Furnace;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.workstation.AbstractWorkstation;
import su.nightexpress.excellentjobs.grind.workstation.WorkstationType;

@NullMarked
public class FurnaceWorkstation extends AbstractWorkstation<Furnace> {

    public FurnaceWorkstation(WorkstationType type, Furnace backend) {
        super(type, backend);
    }

    @Override
    public boolean isCrafting() {
        return this.backend.getCookTime() > 0;
    }
}
