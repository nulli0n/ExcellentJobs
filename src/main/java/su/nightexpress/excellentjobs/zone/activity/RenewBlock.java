package su.nightexpress.excellentjobs.zone.activity;

import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class RenewBlock {

    private final BlockData blockData;
    private final long      resetTime;

    public RenewBlock(BlockData blockData, long resetTime) {
        this.blockData = blockData;
        this.resetTime = resetTime;
    }

    public boolean isReady() {
        return TimeUtil.isPassed(this.resetTime);
    }

    public BlockData getBlockData() {
        return this.blockData;
    }

    public long getResetTime() {
        return this.resetTime;
    }
}
