package su.nightexpress.excellentjobs.stats.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.stats.TopEntry;
import su.nightexpress.excellentjobs.api.stats.TopTracker;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.stats.StatsManager;
import su.nightexpress.excellentjobs.stats.StatsPlaceholders;
import su.nightexpress.excellentjobs.stats.core.StatsLang;
import su.nightexpress.excellentjobs.stats.model.JobTracker;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class TopPlayersMenu extends AbstractObjectMenu<JobTracker> {

    private static final String DEFAULT_TITLE = "%s • Top %s"
        .formatted(JobsPlaceholders.JOB_NAME, JobsPlaceholders.GENERIC_TYPE);

    private final StatsManager manager;

    @Nullable
    private ItemPopulator<TopEntry> entryItemPopulator;

    public TopPlayersMenu(NightPlugin plugin, StatsManager manager) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, JobTracker.class);
        this.manager = manager;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        JobTracker jobTracker = this.getObject(context);
        Job job = jobTracker.job();
        TopTracker tracker = jobTracker.tracker();

        PlaceholderContext ctx = PlaceholderContext.builder()
            .with(job.placeholders())
            .with(JobsPlaceholders.GENERIC_TYPE, () -> NightMessage.stripTags(tracker.getName()))
            .build();

        return ctx.apply(super.getRawTitle(context));
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(0, 9).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 45).toArray());

        this.addNextPageButton(50);
        this.addPreviousPageButton(48);

        this.addBackButton(this::handleBack, 45);
    }

    @Override
    protected void onLoad(FileConfig config) {
        NightItem entryIcon = config.getOrSet("Entry.Icon", ConfigCodecs.NIGHT_ITEM,
            NightItem.fromType(Material.PLAYER_HEAD)
        );

        int[] defSlots = {13, 21, 22, 23, 29, 30, 31, 32, 33, 37, 38, 39, 40, 41, 42, 43};
        int[] entrySlots = config.getOrSet("Entry.Slots", ConfigCodecs.INT_ARRAY, defSlots);

        this.entryItemPopulator = ItemPopulator.builder(TopEntry.class)
            .actionProvider(entry -> context -> {
            })
            .itemProvider((context, entry) -> {
                return entryIcon.copy()
                    .hideAllComponents()
                    .localized(StatsLang.UI_LEADERBOARD_ENTRY)
                    .setPlayerProfile(entry.user().getEffectiveProfile())
                    .replace(builder -> builder
                        .with(StatsPlaceholders.GENERIC_POS, () -> String.valueOf(entry.position()))
                        .with(CommonPlaceholders.PLAYER_NAME, () -> entry.user().getName())
                        .with(StatsPlaceholders.GENERIC_SCORE, entry::score)
                    );
            })
            .slots(entrySlots)
            .build();
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        JobTracker jobTracker = this.getObject(context);

        Job job = jobTracker.job();
        TopTracker tracker = jobTracker.tracker();

        List<TopEntry> entries = new ArrayList<>();
        for (int count = 0; count < 10; count++) {
            int position = count + 1;
            TopEntry entry = tracker.getTop(job, position);
            if (entry == null) break;

            entries.add(entry);
        }

        if (this.entryItemPopulator != null) {
            this.entryItemPopulator.populateTo(context, entries, items);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        Player player = context.getPlayer();
        JobTracker jobTracker = this.getObject(context);
        Job job = jobTracker.job();

        if (this.manager.hasTrackers()) {
            this.manager.showTrackersMenu(player, job);
        }
        else {
            player.closeInventory();
        }
    }
}
