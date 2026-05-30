package su.nightexpress.excellentjobs.contract.definition;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.codec.ContractDefinitionCodec;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class ContractDefinition {

    public static final ContractDefinitionCodec CODEC = new ContractDefinitionCodec();

    private final String       name;
    private final List<String> description;
    private final NightItem    icon;
    private final int          priority;

    ContractDefinition(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.icon = builder.icon;
        this.priority = builder.priority;
    }

    public static ContractDefinition defaults() {
        return new Builder().build();
    }

    public String getName() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public NightItem getIcon() {
        return this.icon;
    }

    public int getPriority() {
        return this.priority;
    }

    public static class Builder {

        private String       name        = "Unnamed";
        private List<String> description = List.of();
        private NightItem    icon        = NightItem.fromType(Material.MAP);
        private int          priority    = 0;

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

        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public ContractDefinition build() {
            return new ContractDefinition(this);
        }
    }
}