package su.nightexpress.excellentjobs.api.grind;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GrindContext {

    protected final ItemStack tool;
    protected final int       amount;

    public GrindContext(@Nullable ItemStack tool, int amount) {
        this.tool = tool;
        this.amount = amount;
    }

    public static @NonNull GrindContext create() {
        return create(null);
    }

    public static @NonNull GrindContext create(@Nullable ItemStack tool) {
        return create(tool, 1);
    }

    public static @NonNull GrindContext create(@Nullable ItemStack tool, int amount) {
        return new GrindContext(tool, amount);
    }

    public @Nullable ItemStack getTool() {
        return this.tool;
    }

    public int getAmount() {
        return this.amount;
    }
}
