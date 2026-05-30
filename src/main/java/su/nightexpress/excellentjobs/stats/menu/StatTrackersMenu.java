package su.nightexpress.excellentjobs.stats.menu;

import java.util.Comparator;
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
import su.nightexpress.excellentjobs.api.stats.TopTracker;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.stats.StatsManager;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.item.populator.SlotPattern;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class StatTrackersMenu extends AbstractObjectMenu<Job> {

    private static final String      DEFAULT_TITLE        = "%s • Leaderboards".formatted(JobsPlaceholders.JOB_NAME);
    private static final SlotPattern DEFAULT_SLOT_PATTERN = new SlotPattern()
        .with(1, 13)
        .with(2, 12, 14)
        .with(3, 11, 13, 15)
        .with(4, 10, 12, 14, 16)
        .with(5, 11, 12, 13, 14, 15);

    private final StatsManager manager;

    @Nullable
    private ItemPopulator<TopTracker> trackerItemPopulator;

    public StatTrackersMenu(NightPlugin plugin, StatsManager manager) {
        super(plugin, MenuType.GENERIC_9X4, DEFAULT_TITLE, Job.class);
        this.manager = manager;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        Job job = this.getObject(context);

        PlaceholderContext ctx = PlaceholderContext.builder()
            .with(job.placeholders())
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
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 27);
    }

    @Override
    protected void onLoad(FileConfig config) {
        SlotPattern pattern = config.getOrSet("Tracker.Slots", ConfigCodecs.SLOT_PATTERN,
            DEFAULT_SLOT_PATTERN);

        this.trackerItemPopulator = ItemPopulator.builder(TopTracker.class)
            .slots(pattern)
            .itemProvider((context, tracker) -> {
                return tracker.getIcon().copy()
                    .setDisplayName(tracker.getName())
                    .setLore(tracker.getDescription())
                    .hideAllComponents();
            })
            .actionProvider(tracker -> context -> this.handleTracker(context, tracker))
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
        List<TopTracker> trackers = this.manager.getTrackers()
            .stream()
            .sorted(Comparator.comparing(TopTracker::getId))
            .toList();

        if (this.trackerItemPopulator != null) {
            this.trackerItemPopulator.populateTo(context, trackers, items);
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
        Job job = this.getObject(context);

        this.manager.backToJobMenu(player, job);
    }

    private void handleTracker(ActionContext context, TopTracker tracker) {
        Player player = context.getPlayer();
        Job job = this.getObject(context);

        this.manager.showTopPlayersMenu(player, job, tracker);
    }
}
