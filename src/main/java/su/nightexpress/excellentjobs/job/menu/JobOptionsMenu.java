package su.nightexpress.excellentjobs.job.menu;

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

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.event.JobMenuDefineLayoutEvent;
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.dialog.JobDialogKeys;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobInfo;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class JobOptionsMenu extends AbstractObjectMenu<Job> {

    private static final String DEFAULT_TITLE = "%s • Management".formatted(JobsPlaceholders.JOB_NAME);

    private final JobManager manager;

    public JobOptionsMenu(NightPlugin plugin, JobManager jobManager) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, Job.class);
        this.manager = jobManager;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        Job src = this.getObject(context);
        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(src.placeholders())
            .build();

        return placeholderContext.apply(super.getRawTitle(context));
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 45).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());

        this.addBackButton(this::handleBack, 45);

        JobMenuDefineLayoutEvent event = new JobMenuDefineLayoutEvent(this);
        this.plugin.getPluginManager().callEvent(event);

        this.addDefaultButton("missions_placeholder", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_EYE)
                    .setDisplayName(TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Missions"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Coming soon!")
                    ))
                    .hideAllComponents()
                )
                .build()
            )
            .slots(30)
            .build()
        );

        this.addDefaultButton("boosters_placeholder", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLOWSTONE_DUST)
                    .setDisplayName(TagWrappers.GRADIENT.with("#ff416c", "#ff4b2b").and(TagWrappers.BOLD).wrap(
                        "Boosters"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Coming soon!")
                    ))
                    .setEnchantGlint(true)
                    .hideAllComponents()
                )
                .build()
            )
            .slots(15)
            .build()
        );

        this.addDefaultButton("quit", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Quit Job"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("You can quit " + JobsPlaceholders.JOB_NAME + " job."),
                        "",
                        TagWrappers.RED.wrap("[*] ") + TagWrappers.SOFT_RED.wrap("Job progress will be lost."),
                        "",
                        TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to quit"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(ctx -> ctx
                    .with(this.getObject(context).placeholders())
                ))
                .condition(context -> !this.isJobOnLeaveCooldown(context))
                .action(this::handeJobQuit)
                .build()
            )
            .state("cooldown", ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Quit Job"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("You " + TagWrappers.RED.wrap("can't") + " quit this job"),
                        TagWrappers.GRAY.wrap("currently."),
                        "",
                        TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEMS.apply("item/clock_12")) + TagWrappers.RED.wrap(
                            " You still have to work"),
                        TagWrappers.RED.wrap("for another " +
                            TagWrappers.SOFT_RED.wrap(CommonPlaceholders.GENERIC_TIME)),
                        TagWrappers.RED.wrap("before you can quit this job.")
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).placeholders())
                    .with(CommonPlaceholders.GENERIC_TIME, () -> {
                        Job job = this.getObject(context);
                        JobData data = this.manager.getActiveData(context.getPlayer(), job);
                        if (data == null || data.isLeaveCooldownExpired()) return Lang.OTHER_N_A.text();

                        return TimeFormats.formatDuration(data.getLeaveCooldown(), TimeFormatType.LITERAL);
                    })
                ))
                .condition(this::isJobOnLeaveCooldown)
                .build()
            )
            .slots(53)
            .build()
        );
    }

    @Override
    protected void onLoad(FileConfig config) {

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
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory,
                          List<MenuItem> items) {

    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private boolean isJobOnLeaveCooldown(ViewerContext context) {
        Player player = context.getPlayer();
        Job job = this.getObject(context);

        return this.manager.hasJobLeaveCooldown(player, job);
    }

    private void handleBack(ActionContext context) {
        this.manager.showJobsMenu(context.getPlayer());
    }

    private void handeJobQuit(ActionContext context) {
        Player player = context.getPlayer();
        Job job = this.getObject(context);
        JobData data = this.manager.getActiveData(player, job);
        if (data == null) {
            player.closeInventory();
            return;
        }

        JobInfo info = new JobInfo(job, data);

        this.plugin.showDialog(player, JobDialogKeys.JOB_QUIT, info, player::closeInventory);
    }
}
