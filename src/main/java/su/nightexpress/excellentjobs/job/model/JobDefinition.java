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
    private boolean      permissionRequired;
    private NightItem    icon;
    private List<String> joinCommands;
    private List<String> leaveCommands;
    private int[]        menuSlots;
    private int          menuPage;

    JobDefinition(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.permissionRequired = builder.permissionRequired;
        this.icon = builder.icon.copy();
        this.joinCommands = builder.joinCommands;
        this.leaveCommands = builder.leaveCommands;
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

    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    public NightItem getIcon() {
        return icon.copy();
    }

    public List<String> getJoinCommands() {
        return joinCommands;
    }

    public List<String> getLeaveCommands() {
        return leaveCommands;
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
        private boolean      permissionRequired;
        private NightItem    icon;
        private List<String> joinCommands;
        private List<String> leaveCommands;
        private int[]        menuSlots = {};
        private int          menuPage  = 1;

        Builder() {
            this.name = "Unnamed";
            this.description = new ArrayList<>();
            this.permissionRequired = false;
            this.icon = NightItem.fromType(Material.GOLDEN_HOE);
            this.joinCommands = new ArrayList<>();
            this.leaveCommands = new ArrayList<>();
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

        public Builder setPermissionRequired(boolean permissionRequired) {
            this.permissionRequired = permissionRequired;
            return this;
        }

        public Builder setIcon(NightItem icon) {
            this.icon = icon;
            return this;
        }

        public Builder setJoinCommands(List<String> joinCommands) {
            this.joinCommands = joinCommands;
            return this;
        }

        public Builder setLeaveCommands(List<String> leaveCommands) {
            this.leaveCommands = leaveCommands;
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
