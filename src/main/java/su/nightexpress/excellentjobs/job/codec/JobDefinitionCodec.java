package su.nightexpress.excellentjobs.job.codec;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.model.JobDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class JobDefinitionCodec implements ConfigCodec<JobDefinition> {

    @Override
    public JobDefinition read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".Name", ConfigCodecs.STRING, "Unnamed");
        List<String> description = config.getOrSet(path + ".Description", ConfigCodecs.STRING_LIST, List.of());
        NightItem icon = config.getOrSet(path + ".Icon", ConfigCodecs.NIGHT_ITEM, NightItem.fromType(
            Material.GOLDEN_HOE));

        int[] menuSlots = config.getOrSet(path + ".Menu-Slots", ConfigCodecs.INT_ARRAY, new int[0]);
        int menuPage = config.getOrSet(path + ".Menu-Page", ConfigCodecs.INT, 1);

        return JobDefinition.builder()
            .setName(name)
            .setDescription(description)
            .setIcon(icon)
            .setMenuSlots(menuSlots)
            .setMenuPage(menuPage)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, JobDefinition definition) {
        config.set(path + ".Name", definition.getName());
        config.set(path + ".Description", definition.getDescription());
        config.set(path + ".Icon", definition.getIcon());

        config.set(path + ".Menu-Slots", ConfigCodecs.INT_ARRAY, definition.getMenuSlots());
        config.set(path + ".Menu-Page", definition.getMenuPage());
    }
}
