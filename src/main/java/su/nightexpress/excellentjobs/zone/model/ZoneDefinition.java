package su.nightexpress.excellentjobs.zone.model;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.zone.codec.ZoneDefinitionCodec;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class ZoneDefinition {

    public static final ZoneDefinitionCodec CODEC = new ZoneDefinitionCodec();

    private String       worldName;
    private Cuboid       cuboid;
    private String       name;
    private List<String> description;
    private NightItem    icon;

    ZoneDefinition(Builder builder) {
        this.worldName = builder.worldName;
        this.cuboid = builder.cuboid;
        this.name = builder.name;
        this.description = builder.description;
        this.icon = builder.icon;
    }

    public static ZoneDefinition defaults() {
        return new Builder().build();
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public NightItem getIcon() {
        return icon.copy();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String       worldName   = "world";
        private Cuboid       cuboid      = new Cuboid(BlockPos.empty(), BlockPos.empty());
        private String       name        = "Unnamed";
        private List<String> description = List.of();
        private NightItem    icon        = NightItem.fromType(Material.GRASS_BLOCK);

        public Builder setWorldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        public Builder setCuboid(Cuboid cuboid) {
            this.cuboid = cuboid;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(List<String> description) {
            this.description = description;
            return this;
        }

        public Builder setIcon(NightItem icon) {
            this.icon = icon;
            return this;
        }

        public ZoneDefinition build() {
            return new ZoneDefinition(this);
        }
    }
}
