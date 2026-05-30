package su.nightexpress.excellentjobs.grind.workstation;

import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Campfire;
import org.bukkit.block.Furnace;
import org.bukkit.block.Smoker;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.grind.workstation.impl.BrewingWorkstation;
import su.nightexpress.excellentjobs.grind.workstation.impl.CampfireWorkstation;
import su.nightexpress.excellentjobs.grind.workstation.impl.FurnaceWorkstation;

@NullMarked
public interface Workstation {

    static @Nullable Workstation getByBlock(Block block) {
        return getByState(block.getState());
    }

    static @Nullable Workstation getByInventory(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof BlockState state)) return null;

        return getByState(state);
    }

    private static @Nullable Workstation getByState(BlockState state) {
        return switch (state) {
            case BrewingStand brewingStand -> new BrewingWorkstation(brewingStand);
            case BlastFurnace blastFurnace -> new FurnaceWorkstation(WorkstationType.BLAST_FURNACE, blastFurnace);
            case Smoker smoker -> new FurnaceWorkstation(WorkstationType.SMOKER, smoker);
            case Furnace furnace -> new FurnaceWorkstation(WorkstationType.FURNACE, furnace);
            case Campfire campfire -> new CampfireWorkstation(campfire);
            default -> null;
        };
    }

    WorkstationType getType();

    TileState getBackend();

    void update();

    boolean isCrafting();
}
