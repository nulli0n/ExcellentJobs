package su.nightexpress.excellentjobs.zone.selection;

import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class BlockInfo {

    private final BlockPos  blockPos;
    private final BlockData blockData;

    public BlockInfo(BlockPos blockPos, BlockData blockData) {
        this.blockPos = blockPos;
        this.blockData = blockData;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public BlockData getBlockData() {
        return this.blockData;
    }
}
