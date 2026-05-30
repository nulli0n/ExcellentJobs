package su.nightexpress.excellentjobs.grind.adapter.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.api.EMFAPI;
import com.oheers.fish.fishing.items.Fish;

import su.nightexpress.excellentjobs.grind.adapter.AbstractGrindAdapter;

@NullMarked
public class EvenMoreFishAdapter extends AbstractGrindAdapter<Fish, ItemStack> {

    private static final EMFAPI API       = EvenMoreFish.getInstance().getApi();
    private static final String DELIMITER = ":";

    public EvenMoreFishAdapter(String name) {
        super(name, "evenmorefish");
    }

    @Override
    public boolean canHandle(ItemStack itemStack) {
        return API.isFish(itemStack);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public @Nullable Fish adaptFromName(String name) {
        String[] split = name.split(DELIMITER);
        if (split.length < 2) return null;

        return API.getFish(split[0], split[1]);
    }

    @Override
    public @Nullable Fish adaptFromBukkit(ItemStack itemStack) {
        return API.getFish(itemStack);
    }

    @Override
    public String getInternalName(Fish fish) {
        return fish.getRarity().getId() + DELIMITER + fish.getName();
    }
}
