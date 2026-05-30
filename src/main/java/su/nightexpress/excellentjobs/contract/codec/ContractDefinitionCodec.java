package su.nightexpress.excellentjobs.contract.codec;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.definition.ContractDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class ContractDefinitionCodec implements ConfigCodec<ContractDefinition> {

    @Override
    public ContractDefinition read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".Name", ConfigCodecs.STRING, "Unnamed Contract");
        List<String> description = config.getOrSet(path + ".Description", ConfigCodecs.STRING_LIST, List.of());
        NightItem icon = config.getOrSet(path + ".Icon", ConfigCodecs.NIGHT_ITEM, NightItem.fromType(Material.PAPER));
        int priority = config.getOrSet(path + ".Priority", ConfigCodecs.INT, 0);

        return new ContractDefinition.Builder()
            .setName(name)
            .setDescription(description)
            .setIcon(icon)
            .setPriority(priority)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, ContractDefinition value) {
        config.set(path + ".Name", value.getName());
        config.set(path + ".Description", value.getDescription());
        config.set(path + ".Icon", value.getIcon());
        config.set(path + ".Priority", value.getPriority());
    }
}
