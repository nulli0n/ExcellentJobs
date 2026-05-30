package su.nightexpress.excellentjobs.grind.protection;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindProtection;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.grind.core.GrindLang;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class GrindProtectionManager extends AbstractManager<JobsPlugin> implements GrindProtection {

    private static final String PLAYER_BLOCK_MARKER = "player_block_marker";

    private final GrindProtectionSettings settings;
    private final Path                    settingsFile;

    private final NamespacedKey mobSpawnerKey;

    public GrindProtectionManager(JobsPlugin plugin, Path setttingsFile) {
        super(plugin);
        this.settings = new GrindProtectionSettings();
        this.settingsFile = setttingsFile;

        this.mobSpawnerKey = new NamespacedKey(plugin, "grind.spawner_mob");
    }

    @Override
    protected void onLoad() {
        this.loadSettings();
        this.loadBlockTracker();

        this.addListener(new GrindProtectionListener(this.plugin, this));
    }

    @Override
    protected void onShutdown() {

    }

    private void loadSettings() {
        FileConfig.load(this.settingsFile).edit(this.settings::load);
    }

    private void loadBlockTracker() {
        if (!this.settings.getTrackBlockPlayers()) return;

        PlayerBlockTracker.initialize();
        PlayerBlockTracker.BLOCK_FILTERS.add(block -> true);
    }

    public void handleCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (this.settings.getArtificalMobSpawns().contains(reason)) {
            this.markSpawnerMob(entity, true);
        }
    }

    public void handleEntityTransform(EntityTransformEvent event) {
        if (this.isArtificalMob(event.getEntity())) {
            event.getTransformedEntities().forEach(entity -> this.markSpawnerMob(entity, true));
            this.markSpawnerMob(event.getTransformedEntity(), true);
        }
    }

    public void handleBlockFertilize(BlockFertilizeEvent event) {
        Block block = event.getBlock();
        Set<String> badBlocks = this.settings.getForbiddenFertilisers();

        boolean isBadBlock = badBlocks.contains(Placeholders.WILDCARD) || badBlocks.contains(BukkitThing.getValue(block
            .getType()));

        event.getBlocks().forEach(blockState -> {
            if (isBadBlock) {
                PlayerBlockTracker.trackForce(blockState.getBlock());
            }
            else {
                PlayerBlockTracker.unTrack(blockState.getBlock());
            }
        });
    }

    public void handleBlockForm(BlockFormEvent event) {
        Material targetType = event.getNewState().getType();
        Location location = event.getBlock().getLocation();

        if (this.settings.getArtificalBlockFormations().contains(targetType)) {
            this.plugin.runTask(location, () -> {
                PlayerBlockTracker.trackForce(event.getBlock());
            });
        }
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        // An extra marker that a block was placed by a player for the #isPlayerBlock method,
        // that will handle both, BlockBreak and BlockDrop phases.
        if (PlayerBlockTracker.isTracked(block) && event.isDropItems()) {
            this.markPlayerBlock(block, true);
        }
    }

    public void handleBlockItemDrop(BlockDropItemEvent event) {
        Block block = event.getBlock();
        this.markPlayerBlock(block, false);
    }

    public void handleDailyLimits(GrindRewardEvent event) {
        if (!this.settings.getDailyLimitsEnabled()) return;

        GrindReward reward = event.getReward();
        Player player = event.getPlayer();
        Job job = event.getJob();
        JobData data = event.getJobData();
        if (data.isLimitTimeExceed()) {
            data.resetLimits();
            data.setLimitTimestamp(this.createDailyLimitTimestamp());
        }

        for (GrindObjectiveProperty property : GrindObjectiveProperty.values()) {
            this.handleDailyLimit(player, job, data, property, reward);
        }
    }

    private void handleDailyLimit(Player player,
                                  Job job,
                                  JobData data,
                                  GrindObjectiveProperty property,
                                  GrindReward reward) {
        double cap = this.settings.getDailyLimitsValues().getOrDefault(property, -1D);
        if (cap <= 0) return;

        double earning = reward.get(property);
        if (earning == 0D) return;

        double progress = data.getLimitProgress(property);
        if (progress >= 1D) {
            reward.remove(property);
            return;
        }

        double partition = earning / cap;
        double newProgress = progress + partition;

        data.setLimitProgress(property, newProgress);
        data.markDirty();

        if (newProgress < 1D) return;

        reward.remove(property);

        GrindLang.PROTECTION_LIMIT_REACHED.message().sendWith(player, ctx -> ctx
            .with(job.placeholders())
            .with(JobsPlaceholders.GENERIC_TYPE, () -> Lang.OBJECTIVE_PROPERTY.getLocalized(property))
            .with(CommonPlaceholders.GENERIC_TIME, () -> TimeFormats.formatDuration(data.getLimitTimestamp(),
                TimeFormatType.LITERAL)
            )
        );
    }

    public long createDailyLimitTimestamp() {
        LocalDateTime midnight = TimeUtil.getCurrentDateTime().with(LocalTime.MIDNIGHT).plusDays(1);

        return TimeUtil.toEpochMillis(midnight);
    }

    @Override
    public boolean isGrindAllowed(Player player) {
        if (this.isForbiddenGamemode(player.getGameMode())) return false;
        if (this.isForbiddenWorld(player.getWorld())) return false;

        Entity vehicle = player.getVehicle();
        return vehicle == null || !this.isForbiddenVehicle(vehicle);
    }

    @Override
    public boolean isArtificalBlock(Block block) {
        return block.hasMetadata(PLAYER_BLOCK_MARKER) || PlayerBlockTracker.isTracked(block);
    }

    @Override
    public boolean isArtificalMob(Entity entity) {
        return PDCUtil.getBoolean(entity, this.mobSpawnerKey).isPresent();
    }

    @Override
    public boolean isNaturalBlock(Block block) {
        return !this.isArtificalBlock(block);
    }

    @Override
    public boolean isNaturalMob(Entity entity) {
        return !this.isArtificalMob(entity);
    }

    public void markSpawnerMob(Entity entity, boolean flag) {
        PDCUtil.set(entity, this.mobSpawnerKey, flag);
    }

    public void markPlayerBlock(Block block, boolean flag) {
        if (flag) {
            block.setMetadata(PLAYER_BLOCK_MARKER, new FixedMetadataValue(this.plugin, true));
        }
        else {
            block.removeMetadata(PLAYER_BLOCK_MARKER, this.plugin);
        }
    }

    public boolean isForbiddenWorld(World world) {
        return this.settings.getPreventInWorlds().contains(world.getName());
    }

    public boolean isForbiddenGamemode(GameMode mode) {
        return this.settings.getPreventInCreative() && mode == GameMode.CREATIVE;
    }

    public boolean isForbiddenVehicle(Entity entity) {
        return this.settings.getForbiddenVehicles().contains(entity.getType());
    }
}
