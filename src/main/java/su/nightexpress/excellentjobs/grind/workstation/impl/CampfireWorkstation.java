package su.nightexpress.excellentjobs.grind.workstation.impl;

import org.bukkit.block.Campfire;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.workstation.AbstractWorkstation;
import su.nightexpress.excellentjobs.grind.workstation.WorkstationType;

@NullMarked
public class CampfireWorkstation extends AbstractWorkstation<Campfire> {

    public CampfireWorkstation(Campfire backend) {
        super(WorkstationType.CAMPFIRE, backend);
    }

    @Override
    public boolean isCrafting() {
        for (int slot = 0; slot < this.backend.getSize(); slot++) {
            if (this.backend.getCookTime(slot) > 0) {
                return true;
            }
        }
        return false;
    }
}
