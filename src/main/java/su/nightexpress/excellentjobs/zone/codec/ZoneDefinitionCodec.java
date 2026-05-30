package su.nightexpress.excellentjobs.zone.codec;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.zone.model.ZoneDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class ZoneDefinitionCodec implements ConfigCodec<ZoneDefinition> {

    @Override
    public ZoneDefinition read(FileConfig config, String path) throws CodecReadException {
        String worldName = config.getOrSet(path + ".Bounds.World", ConfigCodecs.STRING, "world");

        BlockPos minPos = BlockPos.read(config, path + ".Bounds.P1");
        BlockPos maxPos = BlockPos.read(config, path + ".Bounds.P2");
        Cuboid cuboid = new Cuboid(minPos, maxPos);

        String name = config.getOrSet(path + ".Name", ConfigCodecs.STRING, "Unnamed");
        List<String> description = config.getOrSet(path + ".Description", ConfigCodecs.STRING_LIST, List.of());
        NightItem icon = config.getOrSet(path + ".Icon", ConfigCodecs.NIGHT_ITEM, new NightItem(Material.MAP));
        boolean permissionRequired = config.getOrSet(path + ".Permission_Required", ConfigCodecs.BOOLEAN, false);

        return ZoneDefinition.builder()
            .setWorldName(worldName)
            .setCuboid(cuboid)
            .setName(name)
            .setDescription(description)
            .setIcon(icon)
            .setPermissionRequired(permissionRequired)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, ZoneDefinition definition) {
        config.set(path + ".Bounds.World", definition.getWorldName());
        config.set(path + ".Bounds.P1", definition.getCuboid().getMin());
        config.set(path + ".Bounds.P2", definition.getCuboid().getMax());
        config.set(path + ".Name", definition.getName());
        config.set(path + ".Description", definition.getDescription());
        config.set(path + ".Icon", definition.getIcon());
        config.set(path + ".Permission_Required", definition.isPermissionRequired());
    }
}
