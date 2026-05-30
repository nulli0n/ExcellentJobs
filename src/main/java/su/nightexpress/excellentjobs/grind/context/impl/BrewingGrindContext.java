package su.nightexpress.excellentjobs.grind.context.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindContext;

public class BrewingGrindContext extends GrindContext {

    private final int     potionsAmount;
    private final boolean automated;

    public BrewingGrindContext(@Nullable ItemStack tool, int amount, int potionsAmount, boolean automated) {
        super(tool, amount);
        this.potionsAmount = potionsAmount;
        this.automated = automated;
    }

    public int getPotionsAmount() {
        return this.potionsAmount;
    }

    public boolean isAutomated() {
        return this.automated;
    }
}
