package su.nightexpress.excellentjobs.zone.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobInfo;
import su.nightexpress.excellentjobs.zone.core.ZonePerms;
import su.nightexpress.excellentjobs.zone.model.BlockList;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.excellentjobs.zone.model.ZoneBehavior;
import su.nightexpress.excellentjobs.zone.requirement.RequirementMode;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class ActiveZone {

    private final JobsPlugin                plugin;
    private final JobManager                jobManager;
    private final World                     world;
    private final Zone                      zone;
    private final Map<BlockPos, RenewBlock> renewBlocks;

    public ActiveZone(JobsPlugin plugin, JobManager jobManager, World world, Zone zone) {
        this.plugin = plugin;
        this.jobManager = jobManager;
        this.world = world;
        this.zone = zone;
        this.renewBlocks = new HashMap<>();
    }

    public boolean isAvailable(Player player) {
        if (player.hasPermission(ZonePerms.BYPASS_ZONE_REQUIREMENTS)) return true;
        if (!this.zone.hasPermission(player)) return false;

        ZoneBehavior behavior = this.zone.getBehavior();
        RequirementMode mode = behavior.getRequirementMode();
        Set<ZoneRequirement> requirements = behavior.getRequirements();
        if (requirements.isEmpty()) return true;

        Stream<Job> stream = this.jobManager.getActiveJobs(player)
            .stream().map(JobInfo::job);

        return switch (mode) {
            case ANY_JOB -> stream.anyMatch(job -> requirements.stream().allMatch(req -> req.test(player, job)));
            case ALL_JOBS -> stream.allMatch(job -> requirements.stream().allMatch(req -> req.test(player, job)));
        };
    }

    public boolean handleBlockBreak(BlockBreakEvent event, Block block) {
        if (block.getWorld() != this.world) return false;

        BlockList blockList = this.zone.getBehavior().getBlockList(block.getType());
        if (blockList == null) return false;

        if (!blockList.isDropItems()) {
            event.setExpToDrop(0);
            event.setDropItems(false);
        }

        BlockData blockData = block.getBlockData();
        BlockPos pos = BlockPos.from(block.getLocation());
        long resetDate = TimeUtil.createFutureTimestamp(blockList.getResetTime());

        this.renewBlocks.put(pos, new RenewBlock(blockData, resetDate));
        this.plugin.runTask(() -> this.world.setBlockData(block.getLocation(), blockList.getFallbackMaterial()
            .createBlockData()));

        return true;
    }

    public void regenerateBlocks() {
        this.regenerateBlocks(false);
    }

    public void regenerateBlocks(boolean force) {
        this.renewBlocks.entrySet().removeIf(entry -> {
            BlockPos pos = entry.getKey();
            RenewBlock renewBlock = entry.getValue();

            if (!force) {
                if (!renewBlock.isReady()) return false;
                if (!pos.isChunkLoaded(this.world)) return false;
            }

            BlockData blockData = renewBlock.getBlockData();
            Location location = pos.toLocation(this.world);
            this.world.setBlockData(location, blockData);
            UniParticle.of(Particle.BLOCK, blockData).play(LocationUtil.setCenter3D(location), 0.35, 0.05, 60);

            return true;
        });
    }

    public Zone getZone() {
        return zone;
    }
}
