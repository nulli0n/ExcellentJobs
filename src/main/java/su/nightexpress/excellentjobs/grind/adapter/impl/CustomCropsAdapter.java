package su.nightexpress.excellentjobs.grind.adapter.impl;

import java.util.Optional;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.momirealms.customcrops.api.BukkitCustomCropsAPI;
import net.momirealms.customcrops.api.CustomCropsAPI;
import net.momirealms.customcrops.api.core.Registries;
import net.momirealms.customcrops.api.core.block.CustomCropsBlock;
import net.momirealms.customcrops.api.core.world.CustomCropsBlockState;
import net.momirealms.customcrops.api.core.world.CustomCropsWorld;
import net.momirealms.customcrops.api.core.world.Pos3;
import su.nightexpress.excellentjobs.grind.adapter.AbstractGrindAdapter;

@NullMarked
public class CustomCropsAdapter extends AbstractGrindAdapter<CustomCropsBlock, Block> {

    public CustomCropsAdapter(String name) {
        super(name, "customcrops");
    }

    private static CustomCropsAPI getAPI() {
        return BukkitCustomCropsAPI.get();
    }

    private static Optional<CustomCropsBlockState> getCropState(Block block) {
        World world = block.getWorld();
        Pos3 pos = getAPI().adapt(block.getLocation());

        CustomCropsWorld<?> cropsWorld = getAPI().getCustomCropsWorld(world);
        if (cropsWorld == null) return Optional.empty();

        return cropsWorld.getBlockState(pos);
    }

    @Override
    public boolean canHandle(Block block) {
        return getCropState(block).isPresent();
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public @Nullable CustomCropsBlock adaptFromName(String name) {
        return Registries.BLOCKS.get(name);
    }

    @Override
    public @Nullable CustomCropsBlock adaptFromBukkit(Block block) {
        Optional<CustomCropsBlock> optional = getCropState(block).map(CustomCropsBlockState::type);
        return optional.orElse(null);
    }

    @Override
    public String getInternalName(CustomCropsBlock customCropsBlock) {
        return customCropsBlock.type().asString();
    }
}
