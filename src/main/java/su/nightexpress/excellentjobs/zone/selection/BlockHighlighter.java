package su.nightexpress.excellentjobs.zone.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.nightcore.util.EntityUtil;

@NullMarked
public abstract class BlockHighlighter {

    public static final ChatColor COLOR = ChatColor.AQUA;
    public static final float     SIZE  = 0.98f;

    protected final JobsPlugin             plugin;
    private final Map<UUID, List<Integer>> entityIdMap;

    protected BlockHighlighter(JobsPlugin plugin) {
        this.plugin = plugin;
        this.entityIdMap = new HashMap<>();
    }

    protected int nextEntityId() {
        return EntityUtil.nextEntityId();
    }

    public void removeVisuals(Player player) {
        List<Integer> list = this.entityIdMap.remove(player.getUniqueId());
        if (list == null) return;

        this.destroyEntity(player, list);
    }

    public void addVisualBlock(Player player, Location location, BlockData blockData) {
        List<Integer> idList = this.entityIdMap.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

        // To shift scaled down/up displays to the center of a block location.
        float offset = 1f - SIZE;
        // Half-sized (0.5f) block displays got shifted on 1/2 of the size difference, so 0.5f modifier comes from here.
        float shift = 0.5f * offset;

        location.setX(location.getBlockX() + shift);
        location.setY(location.getBlockY() + shift);
        location.setZ(location.getBlockZ() + shift);

        int entityID = this.nextEntityId();

        this.spawnVisualBlock(entityID, player, location, blockData, COLOR, SIZE);

        idList.add(entityID);
    }

    protected abstract void spawnVisualBlock(int entityID, Player player, Location location, BlockData blockData,
                                             ChatColor color, float size);

    protected abstract void destroyEntity(Player player, List<Integer> idList);
}
