package su.nightexpress.excellentjobs.grind.context.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindContext;

public class KillingGrindContext extends GrindContext {

    private final boolean isSpawnerMob;

    public KillingGrindContext(@Nullable ItemStack tool, int amount, boolean isSpawnerMob) {
        super(tool, amount);
        this.isSpawnerMob = isSpawnerMob;
    }

    public boolean isSpawnerMob() {
        return this.isSpawnerMob;
    }
}
