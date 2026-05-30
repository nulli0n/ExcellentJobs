package su.nightexpress.excellentjobs.zone.model;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.zone.codec.BlockListCodec;

@NullMarked
public class BlockList {

    public static final BlockListCodec CODEC = new BlockListCodec();

    private final Set<Material> materials;
    private final Material      fallbackMaterial;
    private final int           resetTime;
    private final boolean       dropItems;

    public BlockList(Set<Material> materials,
                     Material fallbackMaterial,
                     int resetTime,
                     boolean dropItems) {
        this.materials = new HashSet<>(materials);
        this.fallbackMaterial = fallbackMaterial;
        this.resetTime = resetTime;
        this.dropItems = dropItems;
    }

    public boolean contains(Material material) {
        return this.materials.contains(material);
    }

    public Set<Material> getMaterials() {
        return this.materials;
    }

    public Material getFallbackMaterial() {
        return this.fallbackMaterial;
    }

    public int getResetTime() {
        return this.resetTime;
    }

    public boolean isDropItems() {
        return this.dropItems;
    }
}
