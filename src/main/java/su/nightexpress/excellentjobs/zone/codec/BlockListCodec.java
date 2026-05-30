package su.nightexpress.excellentjobs.zone.codec;

import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.zone.model.BlockList;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class BlockListCodec implements ConfigCodec<BlockList> {

    @Override
    public BlockList read(FileConfig config, String path) throws CodecReadException {
        Set<Material> materials = config.getOrSet(path + ".Materials", ConfigCodecs.MATERIAL_SET, Set.of());
        Material fallback = config.getOrSet(path + ".Fallback_Material", ConfigCodecs.MATERIAL, Material.STONE);
        int resetTime = config.getInt(path + ".Reset_Time");
        boolean dropItems = config.getBoolean(path + ".Drop_Items");

        return new BlockList(materials, fallback, resetTime, dropItems);
    }

    @Override
    public void write(FileConfig config, String path, BlockList value) {
        config.set(path + ".Materials", ConfigCodecs.MATERIAL_SET, value.getMaterials());
        config.set(path + ".Fallback_Material", ConfigCodecs.MATERIAL, value.getFallbackMaterial());
        config.set(path + ".Reset_Time", value.getResetTime());
        config.set(path + ".Drop_Items", value.isDropItems());
    }
}
