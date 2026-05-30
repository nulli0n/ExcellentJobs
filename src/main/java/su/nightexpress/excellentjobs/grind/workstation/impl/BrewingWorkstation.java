package su.nightexpress.excellentjobs.grind.workstation.impl;

import org.bukkit.block.BrewingStand;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.workstation.AbstractWorkstation;
import su.nightexpress.excellentjobs.grind.workstation.WorkstationType;

@NullMarked
public class BrewingWorkstation extends AbstractWorkstation<BrewingStand> {

    public BrewingWorkstation(BrewingStand backend) {
        super(WorkstationType.BREWING_STAND, backend);
    }

    @Override
    public boolean isCrafting() {
        return this.backend.getBrewingTime() > 0;
    }
}
