package su.nightexpress.excellentjobs.job.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.codec.JobDefinitionCodec;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class JobDefinition {

    public static final JobDefinitionCodec CODEC = new JobDefinitionCodec();

    private String       name;
    private List<String> description;
    private NightItem    icon;
    private int[]        menuSlots;
    private int          menuPage;

    JobDefinition(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.icon = builder.icon.copy();
        this.menuSlots = builder.menuSlots;
        this.menuPage = builder.menuPage;
    }

    public static Builder builder() {
        return new Builder();
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

    public int[] getMenuSlots() {
        return menuSlots;
    }

    public int getMenuPage() {
        return menuPage;
    }

    public static class Builder {

        private String       name;
        private List<String> description;
        private NightItem    icon;
        private int[]        menuSlots = {};
        private int          menuPage  = 1;

        Builder() {
            this.name = "Unnamed";
            this.description = new ArrayList<>();
            this.icon = NightItem.fromType(Material.GOLDEN_HOE);
        }

        public JobDefinition build() {
            return new JobDefinition(this);
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

        public Builder setMenuSlots(int... menuSlots) {
            this.menuSlots = menuSlots;
            return this;
        }

        public Builder setMenuPage(int menuPage) {
            this.menuPage = menuPage;
            return this;
        }
    }
}
