package su.nightexpress.excellentjobs.grind.context.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindContext;

public class EnchantingGrindContext extends GrindContext {

    private final int enchantmentLevel;

    public EnchantingGrindContext(@Nullable ItemStack tool, int amount, int enchantmentLevel) {
        super(tool, amount);
        this.enchantmentLevel = enchantmentLevel;
    }

    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }
}
