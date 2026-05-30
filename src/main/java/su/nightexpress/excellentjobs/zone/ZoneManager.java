package su.nightexpress.excellentjobs.zone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindReward;
import su.nightexpress.excellentjobs.api.zone.ZoneRequirement;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.zone.activity.ActiveZone;
import su.nightexpress.excellentjobs.zone.command.ZoneCommands;
import su.nightexpress.excellentjobs.zone.command.argument.ZoneArgumentType;
import su.nightexpress.excellentjobs.zone.core.ZoneLang;
import su.nightexpress.excellentjobs.zone.core.ZonePerms;
import su.nightexpress.excellentjobs.zone.core.ZoneSettings;
import su.nightexpress.excellentjobs.zone.listener.EntranceZoneListener;
import su.nightexpress.excellentjobs.zone.listener.GenericZoneListener;
import su.nightexpress.excellentjobs.zone.listener.SelectionZoneListener;
import su.nightexpress.excellentjobs.zone.model.BlockList;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.excellentjobs.zone.model.ZoneBehavior;
import su.nightexpress.excellentjobs.zone.model.ZoneDefinition;
import su.nightexpress.excellentjobs.zone.requirement.RequirementMode;
import su.nightexpress.excellentjobs.zone.requirement.RequirementRegistry;
import su.nightexpress.excellentjobs.zone.requirement.common.AllowedJobRequirement;
import su.nightexpress.excellentjobs.zone.requirement.common.LevelRequirement;
import su.nightexpress.excellentjobs.zone.selection.BlockHighlighter;
import su.nightexpress.excellentjobs.zone.selection.BlockInfo;
import su.nightexpress.excellentjobs.zone.selection.BlockPacketsHighlighter;
import su.nightexpress.excellentjobs.zone.selection.BlockProtocolHighlighter;
import su.nightexpress.excellentjobs.zone.selection.Selection;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class ZoneManager extends AbstractManager<JobsPlugin> {

    private static final String PROTOCOL_LIB  = "ProtocolLib";
    private static final String PACKET_EVENTS = "packetevents";

    private final JobManager jobManager;

    private final RequirementRegistry requirementRegistry;

    private final ZoneSettings settings;

    private final Map<String, Zone>       zoneMap;
    private final Map<String, ActiveZone> activeZoneMap;
    private final Map<UUID, Selection>    selectionMap;

    private final NamespacedKey wandItem;
    private final NamespacedKey wandZoneId;

    @Nullable
    private BlockHighlighter highlighter;

    public ZoneManager(JobsPlugin plugin, JobManager jobManager) {
        super(plugin);
        this.jobManager = jobManager;
        this.requirementRegistry = new RequirementRegistry();
        this.settings = new ZoneSettings();

        this.zoneMap = new HashMap<>();
        this.activeZoneMap = new HashMap<>();
        this.selectionMap = new HashMap<>();

        this.wandItem = new NamespacedKey(plugin, "wand.item");
        this.wandZoneId = new NamespacedKey(plugin, "wand.zone_id");
    }

    @Override
    protected void onLoad() {
        this.plugin.injectLang(ZoneLang.class);
        this.plugin.registerPermissions(ZonePerms.ROOT);

        this.initRegistries();
        this.registerRequirements();
        this.registerCodecs();

        this.loadCommands();
        this.loadSettings();
        this.loadHighlighter();
        this.loadZones();

        this.addListener(new GenericZoneListener(this.plugin, this));
        this.addListener(new SelectionZoneListener(this.plugin, this));
        if (this.settings.isControlEntrance()) {
            this.addListener(new EntranceZoneListener(this.plugin, this));
        }

        this.addTask(this::tickActiveZones, this.settings.getRegenerationTickInterval());
    }

    @Override
    protected void onShutdown() {
        this.getActiveZones().forEach(this::deactivateZone);

        this.zoneMap.clear();
        RequirementRegistry.shutdown();
    }

    private void initRegistries() {
        RequirementRegistry.init(this.requirementRegistry);
    }

    private void registerRequirements() {
        this.registerRequirement("allowed_jobs", AllowedJobRequirement.class, AllowedJobRequirement.CODEC);
        this.registerRequirement("job_level", LevelRequirement.class, new LevelRequirement.Codec(this.jobManager));
    }

    public <T extends ZoneRequirement> void registerRequirement(String name, Class<T> type, ConfigCodec<T> codec) {
        this.requirementRegistry.register(name, type);
        ConfigCodecs.register(type, codec);
    }

    private void registerCodecs() {
        ConfigCodecs.register(BlockList.class, BlockList.CODEC);
        ConfigCodecs.register(ZoneDefinition.class, ZoneDefinition.CODEC);
        ConfigCodecs.register(ZoneBehavior.class, ZoneBehavior.CODEC);
    }

    private void loadCommands() {
        Arguments.register(Zone.class, new ZoneArgumentType(this));

        this.plugin.addCommandProvider(new ZoneCommands(this.plugin, this));
    }

    private void loadSettings() {
        Path file = this.plugin.dataPath().resolve(ZoneFiles.FILE_SETTINGS);
        FileConfig.load(file).edit(this.settings::load);
    }

    private void loadHighlighter() {
        if (Plugins.isInstalled(PACKET_EVENTS)) {
            this.highlighter = new BlockPacketsHighlighter(this.plugin);
        }
        else if (Plugins.isInstalled(PROTOCOL_LIB)) {
            this.highlighter = new BlockProtocolHighlighter(this.plugin);
        }
    }

    private void loadZones() {
        Path dir = this.plugin.dataPath().resolve(ZoneFiles.DIR_ZONES);
        FileUtil.findFiles(dir).forEach(file -> {
            try {
                Zone zone = this.loadZone(file);
                this.cacheZone(zone);
                this.activateZone(zone);
            }
            catch (ModelLoadException exception) {
                this.plugin.error("Failed to load zone from file: " + file);
                this.plugin.error("Reason: " + exception.getMessage());

                exception.printStackTrace();
            }
        });

        this.plugin.info("Loaded %s zones.".formatted(this.zoneMap.size()));
    }

    private Zone loadZone(Path file) throws ModelLoadException {
        String name = FileUtil.getNameWithoutExtension(file);
        String id = Strings.varStyle(name).orElse(null);
        if (id == null) throw new ModelLoadException("Zone ID invalid: '%s'".formatted(file));

        try {
            FileConfig config = FileConfig.load(file);

            ZoneDefinition definition = config.read("Definition", ZoneDefinition.class, ZoneDefinition.defaults());
            ZoneBehavior behavior = config.read("Behavior", ZoneBehavior.class, ZoneBehavior.defaults());

            config.saveChanges();

            return new Zone(id, definition, behavior);
        }
        catch (CodecReadException exception) {
            throw new ModelLoadException("Failed to parse zone configuration.", exception);
        }
    }

    private void tickActiveZones() {
        this.getActiveZones().forEach(ActiveZone::regenerateBlocks);
    }

    public boolean isInZone(Block block) {
        return this.getZone(block) != null;
    }

    public boolean isInZone(Entity entity) {
        return this.getZone(entity) != null;
    }

    public boolean isInSelection(Player player) {
        return this.getSelection(player) != null;
    }

    public boolean isCuboidWand(ItemStack itemStack) {
        return PDCUtil.getBoolean(itemStack, this.wandItem).isPresent();
    }

    public Map<String, Zone> getZoneMap() {
        return zoneMap;
    }

    public Set<Zone> getZones() {
        return Set.copyOf(this.zoneMap.values());
    }

    public List<String> getZoneIds() {
        return new ArrayList<>(this.zoneMap.keySet());
    }

    public Set<Zone> getZones(World world) {
        return this.getZones(world.getName());
    }

    public Set<Zone> getZones(String worldName) {
        return this.getZones().stream().filter(zone -> zone.getWorldName().equalsIgnoreCase(worldName)).collect(
            Collectors.toSet());
    }

    public @Nullable ActiveZone getActiveZone(String id) {
        return this.activeZoneMap.get(LowerCase.INTERNAL.apply(id));
    }

    public @Nullable ActiveZone getActiveZone(Zone zone) {
        return this.getActiveZone(zone.getId());
    }

    public Set<ActiveZone> getActiveZones() {
        return Set.copyOf(this.activeZoneMap.values());
    }

    public @Nullable Zone getZoneById(String id) {
        return this.zoneMap.get(LowerCase.INTERNAL.apply(id));
    }

    public @Nullable Zone getZoneByLocation(Location location) {
        World world = location.getWorld();
        if (world == null) return null;

        return this.getZones(world).stream().filter(zone -> zone.contains(location)).findFirst().orElse(null);
    }

    public @Nullable Zone getZoneByWandItem(ItemStack item) {
        String id = PDCUtil.getString(item, this.wandZoneId).orElse(null);
        return id == null ? null : this.getZoneById(id);
    }

    public @Nullable Zone getZone(Entity entity) {
        return this.getZoneByLocation(entity.getLocation());
    }

    public @Nullable Zone getZone(Block block) {
        return this.getZoneByLocation(block.getLocation());
    }

    public @Nullable Selection getSelection(Player player) {
        return this.selectionMap.get(player.getUniqueId());
    }

    private void cacheZone(Zone zone) {
        this.zoneMap.put(zone.getId(), zone);
    }

    private void uncacheZone(Zone zone) {
        this.zoneMap.remove(zone.getId());
    }

    public void removeVisuals(Player player) {
        if (this.highlighter != null) {
            this.highlighter.removeVisuals(player);
        }
    }

    private void writeZone(Zone zone) {
        Path file = this.plugin.dataPath().resolve(ZoneFiles.DIR_ZONES).resolve(FileConfig.withExtension(zone.getId()));
        FileConfig.load(file).edit(config -> {
            config.set("Definition", zone.getDefinition());
            config.set("Behavior", zone.getBehavior());
        });
    }

    public void handleWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        this.getZones(world).forEach(this::activateZone);
    }

    public void handleWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        this.getZones(world).forEach(this::deactivateZone);
    }

    public void handleGrindReward(GrindRewardEvent event) {
        Player player = event.getPlayer();
        Zone zone = this.getZone(player);
        if (zone == null && this.settings.isStrictMode()) {
            event.setCancelled(true);
            return;
        }
        if (zone == null) return;

        ActiveZone activeZone = this.getActiveZone(zone);
        if (activeZone == null) return;

        GrindReward reward = event.getReward();
        for (GrindObjectiveProperty property : GrindObjectiveProperty.values()) {
            double multiplier = zone.getBehavior().getGrindMultiplier(property);
            reward.modify(property, current -> current * multiplier);
        }
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Zone zone = this.getZoneByLocation(block.getLocation());
        if (zone == null) return;

        ActiveZone activeZone = this.getActiveZone(zone);
        if (activeZone == null) return;

        activeZone.handleBlockBreak(event, block);
    }

    public Zone createZone(World world, Cuboid cuboid, String id) {
        ZoneDefinition definition = ZoneDefinition.builder()
            .setWorldName(world.getName())
            .setCuboid(cuboid)
            .setName(StringUtil.capitalizeUnderscored(id))
            .build();

        RequirementMode mode = RequirementMode.ANY_JOB;
        ZoneBehavior behavior = new ZoneBehavior(false, Set.of(), mode, Map.of(), Map.of(), Map.of());

        Zone zone = new Zone(id, definition, behavior);

        this.writeZone(zone);
        this.cacheZone(zone);

        return zone;
    }

    public boolean defineZone(Player player, String name) {
        String id = Strings.varStyle(name).orElse(null);
        if (id == null) {
            ZoneLang.ZONE_DEFINE_BAD_NAME.message().send(player);
            return false;
        }

        if (this.getZoneById(id) != null) {
            ZoneLang.ZONE_DEFINE_EXISTS.message().sendWith(player, ctx -> ctx
                .with(Placeholders.GENERIC_NAME, () -> id)
            );
            return false;
        }

        Selection selection = this.getSelection(player);
        if (selection == null) {
            ZoneLang.ZONE_DEFINE_INCOMPLETE_SELECTION.message().send(player);
            return false;
        }

        Cuboid cuboid = selection.toCuboid();
        if (cuboid == null) {
            ZoneLang.ZONE_DEFINE_INCOMPLETE_SELECTION.message().send(player);
            return false;
        }

        World world = player.getWorld();

        Zone current = this.getZoneById(id);
        if (current != null) {
            ZoneDefinition definition = current.getDefinition();
            definition.setWorldName(world.getName());
            definition.setCuboid(cuboid);
            this.writeZone(current);
            ZoneLang.ZONE_DEFINE_OVERRIDE.message().sendWith(player, replacer -> replacer.with(current.placeholders()));
        }
        else {
            Zone zone = this.createZone(world, cuboid, id);
            ZoneLang.ZONE_DEFINE_CREATED.message().sendWith(player, replacer -> replacer.with(zone.placeholders()));
        }

        this.exitSelection(player);

        return true;
    }

    public boolean deleteZone(Zone zone) {
        Path file = this.plugin.dataPath().resolve(ZoneFiles.DIR_ZONES).resolve(FileConfig.withExtension(zone.getId()));
        try {
            if (Files.deleteIfExists(file)) {
                this.uncacheZone(zone);
                return true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void activateZone(Zone zone) {
        ActiveZone current = this.getActiveZone(zone);
        if (current != null) return;

        World world = this.plugin.getServer().getWorld(zone.getWorldName());
        if (world == null) return;

        ActiveZone activeZone = new ActiveZone(this.plugin, this.jobManager, world, zone);
        this.activeZoneMap.put(zone.getId(), activeZone);
    }

    public void deactivateZone(ActiveZone activeZone) {
        activeZone.regenerateBlocks(true);
        this.activeZoneMap.remove(activeZone.getZone().getId());
    }

    public void deactivateZone(Zone zone) {
        ActiveZone activeZone = this.getActiveZone(zone);
        if (activeZone == null) return;

        this.deactivateZone(activeZone);
    }

    public ItemStack getCuboidWand(@Nullable Zone zone) {
        ItemStack item = this.settings.getWandItem().getItemStack();
        if (zone != null) {
            PDCUtil.set(item, this.wandZoneId, zone.getId());
        }
        PDCUtil.set(item, this.wandItem, true);
        return item;
    }

    public Selection startSelection(Player player, @Nullable Zone zone) {
        Selection selection = new Selection();
        this.selectionMap.put(player.getUniqueId(), selection);

        Players.addItem(player, this.getCuboidWand(zone));

        if (zone != null) {
            this.highlightCuboid(player, zone.getCuboid());
        }
        if (zone == null) {
            ZoneLang.ZONE_CREATE_INFO.message().send(player);
        }

        return selection;
    }

    public void exitSelection(Player player) {
        if (this.isInSelection(player)) {
            Players.takeItem(player, this::isCuboidWand);
            this.selectionMap.remove(player.getUniqueId());
        }
        this.removeVisuals(player);
    }

    public void selectPosition(Player player, ItemStack itemStack, Location location, Action action) {
        if (!this.isCuboidWand(itemStack)) return;

        Selection selection = this.getSelection(player);
        if (selection == null) return;

        BlockPos blockPos = BlockPos.from(location);
        if (action == Action.LEFT_CLICK_BLOCK) {
            selection.setFirst(blockPos);
        }
        else selection.setSecond(blockPos);

        int position = action == Action.LEFT_CLICK_BLOCK ? 1 : 2;

        ZoneLang.ZONE_SELECTION_INFO.message().sendWith(player, ctx -> ctx
            .with(Placeholders.GENERIC_VALUE, () -> String.valueOf(position))
        );

        this.highlightCuboid(player, selection);
    }


    public void highlightCuboid(Player player, Selection selection) {
        this.highlightCuboid(player, selection.getFirst(), selection.getSecond());
    }

    private void highlightCuboid(Player player, @Nullable BlockPos min, @Nullable BlockPos max) {
        if (min == null) min = BlockPos.empty();
        if (max == null) max = BlockPos.empty();
        if (min.isEmpty() && !max.isEmpty()) min = max;
        if (max.isEmpty() && !min.isEmpty()) max = min;

        this.highlightCuboid(player, new Cuboid(min, max));
    }

    public void highlightCuboid(Player player, Cuboid cuboid) {
        this.highlightCuboid(player, cuboid, true);
    }

    public void highlightCuboid(Player player, Cuboid cuboid, boolean reset) {
        BlockHighlighter highlight = this.highlighter;
        if (highlight == null) return;

        if (reset) {
            this.removeVisuals(player);
        }

        World world = player.getWorld();
        Material cornerType = this.settings.getHighlightCorner();
        Material wireType = this.settings.getHighlightWire();
        Set<BlockInfo> dataSet = new HashSet<>();

        // Draw corners of the chunk/region all the time.
        this.collectBlockData(cuboid.getCorners(), dataSet, cornerType.createBlockData());
        this.collectBlockData(cuboid.getCornerWiresY(), dataSet, wireType.createBlockData());

        // Draw connections only for regions or when player is inside a chunk.
        BlockData dataX = this.createBlockData(wireType, Axis.X);
        BlockData dataZ = this.createBlockData(wireType, Axis.Z);

        this.collectBlockData(cuboid.getCornerWiresX(), dataSet, dataX);
        this.collectBlockData(cuboid.getCornerWiresZ(), dataSet, dataZ);

        // Draw all visual blocks at prepated positions with prepared block data.
        dataSet.forEach(blockInfo -> {
            BlockPos blockPos = blockInfo.getBlockPos();
            Location location = blockPos.toLocation(world);

            highlight.addVisualBlock(player, location, blockInfo.getBlockData());
        });
    }

    private void collectBlockData(Collection<BlockPos> source, Set<BlockInfo> target, BlockData data) {
        if (data.getMaterial().isAir()) return;

        source.stream().filter(blockPos -> blockPos != null && !blockPos.isEmpty()).map(
            blockPos -> new BlockInfo(blockPos, data)).forEach(target::add);
    }


    private BlockData createBlockData(Material material, Axis axis) {
        BlockData data = material.createBlockData();
        if (data instanceof Orientable orientable) {
            orientable.setAxis(axis);
        }
        return data;
    }
}
