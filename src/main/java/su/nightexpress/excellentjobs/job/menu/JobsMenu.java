package su.nightexpress.excellentjobs.job.menu;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.BOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GRAY;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.SOFT_YELLOW;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.UNDERLINED;

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

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.dialog.JobDialogKeys;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.format.adaptive.AdaptiveFormatter;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class JobsMenu extends AbstractMenu {

    private static final String DEFAULT_TITLE = "Jobs";
    private static final String DEFAULT_NAME  = TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME);

    private static final List<String> DEFAULT_JOIN_LORE = Lists.newList(
        JobsPlaceholders.JOB_DESCRIPTION,
        CommonPlaceholders.EMPTY_IF_ABOVE,
        TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.PAPER)) + " " +
            TagWrappers.COLOR.with("#15A2E8").and(BOLD).wrap("Statistics:"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("Employees:") + " " +
            TagWrappers.COLOR.with("#15A2E8").wrap("{employees}"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("You Top:") + " " +
            TagWrappers.COLOR.with("#15A2E8").wrap("Join to start compete!"),
        "",
        "<if_can_join>" +
            SOFT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to select contract")) +
            "</if_can_join>",
        "<if_can_not_join>" +
            TagWrappers.SOFT_RED.wrap("→ " + UNDERLINED.wrap("You can't join this job")) +
            "</if_can_not_join>"
    );

    private static final List<String> DEFAULT_MANAGE_LORE = Lists.newList(
        JobsPlaceholders.JOB_DESCRIPTION,
        CommonPlaceholders.EMPTY_IF_ABOVE,
        TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.PAPER)) + " " +
            TagWrappers.COLOR.with("#15A2E8").and(BOLD).wrap("Statistics:"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("Employees:") + " " +
            TagWrappers.COLOR.with("#15A2E8").wrap("{employees}"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("You Top:") + " " +
            TagWrappers.COLOR.with("#15A2E8").wrap("#{top_level_position}"),
        "",
        TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.EXPERIENCE_BOTTLE)) + " " +
            TagWrappers.COLOR.with("#A8E815").and(BOLD).wrap("Leveling:"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("XP:") + " " +
            TagWrappers.COLOR.with("#A8E815").wrap("{xp}" + GRAY.wrap("/") + "{xp_required}"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("Level:") + " " +
            TagWrappers.COLOR.with("#A8E815").wrap("{level}" + GRAY.wrap("/") + "{max_level}"),
        "",
        TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.BOOK)) + " " +
            TagWrappers.COLOR.with("#E8152E").and(BOLD).wrap("Contract:"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("Current:") + " " +
            TagWrappers.COLOR.with("#E8152E").wrap("{contract_name}"),
        TagWrappers.DARK_GRAY.wrap(" » ") + GRAY.wrap("Points:") + " " +
            TagWrappers.COLOR.with("#E8152E").wrap("{contract_points}"),
        "",
        TagWrappers.GOLD.wrap("→ " + UNDERLINED.wrap("Click to manage"))
    );

    private final JobManager             manager;
    private final AdaptiveFormatter<Job> formatter;

    private String       jobName       = DEFAULT_NAME;
    private List<String> jobJoinLore   = DEFAULT_JOIN_LORE;
    private List<String> jobManageLore = DEFAULT_MANAGE_LORE;

    public JobsMenu(JobsPlugin plugin, JobManager manager, AdaptiveFormatter<Job> formatter) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE);
        this.manager = manager;
        this.formatter = formatter;
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

        this.addNextPageButton(50);
        this.addPreviousPageButton(48);
    }

    @Override
    protected void onLoad(FileConfig config) {
        this.jobName = config.getOrSet("Job.Name", ConfigCodecs.STRING, DEFAULT_NAME);
        this.jobJoinLore = config.getOrSet("Job.Lore.Join", ConfigCodecs.STRING_LIST, DEFAULT_JOIN_LORE);
        this.jobManageLore = config.getOrSet("Job.Lore.Manage", ConfigCodecs.STRING_LIST, DEFAULT_MANAGE_LORE);
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
        Player player = context.getPlayer();
        MenuViewer viewer = context.getViewer();

        viewer.setTotalPages(this.manager.getSettings().getJobsMenuPages());
        int page = viewer.getCurrentPage();

        this.manager.getJobs().forEach(job -> {
            JobDefinition definition = job.getDefinition();
            if (definition.getMenuPage() != page) return;

            boolean isEmployed = this.manager.isEmployed(player, job);
            List<String> lore = isEmployed ? this.jobManageLore : this.jobJoinLore;
            List<String> formatted = new ArrayList<>();

            for (String line : lore) {
                String lineFormatted = this.formatter.formatLine(line, job, player);
                if (lineFormatted.isEmpty() && !line.isEmpty()) continue;

                formatted.add(lineFormatted);
            }

            MenuItem menuItem = MenuItem.custom()
                .defaultState(ItemState.builder()
                    .icon(job.getIcon()
                        .setDisplayName(this.jobName)
                        .setLore(formatted)
                        .hideAllComponents()
                        .replace(ctx -> ctx.with(job.placeholders()))
                    )
                    .action(ctx -> this.handleJob(ctx, job))
                    .build()
                )
                .slots(definition.getMenuSlots())
                .build();

            items.add(menuItem);
        });
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleJob(ActionContext context, Job job) {
        Player player = context.getPlayer();
        if (this.manager.isEmployed(player, job)) {
            this.manager.showJobOptions(player, job);
            return;
        }

        this.plugin.showDialog(player, JobDialogKeys.JOB_JOIN, job, player::closeInventory);
    }
}
