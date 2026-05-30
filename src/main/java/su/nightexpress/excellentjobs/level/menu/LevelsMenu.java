package su.nightexpress.excellentjobs.level.menu;

import static su.nightexpress.excellentjobs.JobsPlaceholders.GENERIC_LEVEL;
import static su.nightexpress.excellentjobs.JobsPlaceholders.REWARD_NAME;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.BOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GRAY;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GREEN;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.ORANGE;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.RED;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.SOFT_ORANGE;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.UNDERLINED;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.WHITE;

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
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.leveling.Reward;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobInfo;
import su.nightexpress.excellentjobs.level.LevelingConstants;
import su.nightexpress.excellentjobs.level.LevelingManager;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class LevelsMenu extends AbstractObjectMenu<JobInfo> {

    private static final String DEFAULT_TITLE = "%s • Progression".formatted(JobsPlaceholders.JOB_NAME);
    private static final int[]  DEFAULT_SLOTS = {0, 1, 10, 19, 28, 37, 38, 39, 30, 21, 12, 3, 4, 5, 14, 23, 32, 41, 42, 43, 34, 25, 16, 7, 8};

    private final LevelingManager manager;

    private @Nullable ItemPopulator<Integer> populator;

    public LevelsMenu(JobsPlugin plugin, LevelingManager manager) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, JobInfo.class);
        this.manager = manager;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        JobInfo info = this.getObject(context);
        PlaceholderContext ctx = PlaceholderContext.builder()
            .with(info.job().placeholders())
            .with(info.data().placeholders())
            .build();

        return ctx.apply(super.getRawTitle(context));
    }

    public boolean openAtLevel(Player player, Job job, JobData data) {
        int level = data.getLevel();
        int maxLevel = this.manager.getMaxLevel(job);
        if (this.populator == null) return false;

        int limit = this.populator.slotProvider().getSlots(maxLevel).length;
        int page = (int) Math.ceil((double) level / (double) limit);

        return this.show(player, new JobInfo(job, data), viewer -> {
            viewer.setCurrentPage(page);
        });
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
        this.addBackButton(this::handleBack, 45);
    }

    @Override
    protected void onLoad(FileConfig config) {
        NightItem lockedLevel = config.getOrSet("Level.Locked", ConfigCodecs.NIGHT_ITEM,
            NightItem.fromType(Material.GUNPOWDER)
                .setDisplayName(RED.and(BOLD).wrap("Level " + GENERIC_LEVEL) + GRAY.wrap(" • ") + WHITE.wrap("Locked"))
                .setLore(Lists.newList(
                    TagWrappers.GRAY.wrap(TagWrappers.RED.wrap("✘") + " Complete previous levels to unlock!"),
                    CommonPlaceholders.EMPTY_IF_BELOW,
                    JobsPlaceholders.GENERIC_REWARD
                ))
        );

        NightItem progressLevel = config.getOrSet("Level.InProgress", ConfigCodecs.NIGHT_ITEM,
            NightItem.fromType(Material.GUNPOWDER)
                .setDisplayName(ORANGE.and(BOLD).wrap("Level " + GENERIC_LEVEL) + GRAY.wrap(" • ") + WHITE.wrap(
                    "In Progress"))
                .setLore(Lists.newList(
                    TagWrappers.GRAY.wrap(TagWrappers.DARK_GRAY.wrap("»") + " Progress: " + ORANGE.wrap(
                        JobsPlaceholders.JOB_DATA_XP) + "/" + ORANGE.wrap(JobsPlaceholders.GENERIC_REQUIRED + " XP")),
                    CommonPlaceholders.EMPTY_IF_BELOW,
                    JobsPlaceholders.GENERIC_REWARD
                ))
                .setEnchantGlint(true)
        );

        NightItem unlockedLevel = config.getOrSet("Level.Unlocked", ConfigCodecs.NIGHT_ITEM,
            NightItem.fromType(Material.SUGAR)
                .setDisplayName(GREEN.and(BOLD).wrap("Level " + GENERIC_LEVEL) + GRAY.wrap(" • ") + WHITE.wrap(
                    "Unlocked"))
                .setLore(Lists.newList(
                    TagWrappers.GREEN.wrap("✔") + " " + GRAY.wrap("All rewards are claimed."),
                    CommonPlaceholders.EMPTY_IF_BELOW,
                    JobsPlaceholders.GENERIC_REWARD
                ))
        );

        NightItem unclaimedReward = config.getOrSet("Level.Unclaimed", ConfigCodecs.NIGHT_ITEM,
            NightItem.fromType(Material.REDSTONE)
                .setEnchantGlint(true)
                .setDisplayName(
                    TagWrappers.SOFT_RED.and(BOLD).wrap("Level " + GENERIC_LEVEL) + GRAY.wrap(" • ") + WHITE.wrap(
                        "Unclaimed")
                )
                .setLore(Lists.newList(
                    TagWrappers.GRAY.wrap("Level reward(s) are available."),
                    TagWrappers.GRAY.wrap("Claim them now!"),
                    "",
                    JobsPlaceholders.GENERIC_REWARD,
                    "",
                    SOFT_ORANGE.wrap("→ " + UNDERLINED.wrap("Click to claim!"))
                ))
        );

        NightItem premiumReward = config.getOrSet("Level.Premium", ConfigCodecs.NIGHT_ITEM,
            NightItem.fromType(Material.GLOWSTONE_DUST)
                .setDisplayName(
                    TagWrappers.GOLD.and(BOLD).wrap("Level " + GENERIC_LEVEL) + GRAY.wrap(" • ") + TagWrappers.GOLD
                        .wrap("Premium")
                )
                .setLore(Lists.newList(
                    TagWrappers.GRAY.wrap("Upgrade your " + GOLD.wrap("/rank") + " to unlock!"),
                    "",
                    JobsPlaceholders.GENERIC_REWARD
                ))
        );


        List<String> rewardFormat = config.getOrSet("Reward.Format", ConfigCodecs.STRING_LIST,
            Lists.newList(
                GREEN.and(BOLD).wrap("Rewards:"),
                GREEN.wrap("➥") + " " + GRAY.wrap(REWARD_NAME)
            )
        );

        int[] rewardSlots = config.getOrSet("Reward.Slots", ConfigCodecs.INT_ARRAY, DEFAULT_SLOTS);

        this.populator = ItemPopulator.builder(Integer.class)
            .slots(rewardSlots)
            .itemProvider((context, level) -> {
                Player player = context.getPlayer();
                JobInfo info = this.getObject(context);
                Job job = info.job();
                JobData data = info.data();
                Reward reward = this.manager.getLevelReward(job, level);

                NightItem item = lockedLevel.copy();

                if (data.getLevel() >= level) {
                    if (data.getLevel() == level) {
                        item = progressLevel.copy();
                    }
                    else if (reward == null || data.hasClaimedLevelReward(level)) {
                        item = unlockedLevel.copy();
                    }
                    else if (!reward.isAvailable(player, job)) {
                        item = premiumReward.copy();
                    }
                    else {
                        item = unclaimedReward.copy();
                    }
                }

                return item
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(job.placeholders())
                        .with(data.placeholders())
                        .with(JobsPlaceholders.GENERIC_REQUIRED,
                            () -> NumberUtil.format(this.manager.getRequiredXP(job, data.getLevel()))
                        )
                        .with(JobsPlaceholders.GENERIC_LEVEL, () -> NumberUtil.format(level))
                        .with(JobsPlaceholders.GENERIC_REWARD, () -> {
                            if (reward == null) return "";

                            return String.join(TagWrappers.BR, PlaceholderContext.builder()
                                .with(reward.placeholders())
                                .build()
                                .apply(rewardFormat));
                        })
                    );
            })
            .actionProvider(level -> context -> this.handleReward(context, level))
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
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory,
                          List<MenuItem> items) {
        Job job = this.getObject(context).job();
        int maxLevel = this.manager.getMaxLevel(job);

        List<Integer> levels = IntStream.range(LevelingConstants.START_LEVEL + 1, maxLevel + 1)
            .boxed()
            .toList();

        if (this.populator == null) return;

        this.populator.populateTo(context, levels, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        this.manager.showJobMenu(context.getPlayer(), this.getObject(context).job());
    }

    private void handleReward(ActionContext context, int level) {
        Player player = context.getPlayer();
        Job job = this.getObject(context).job();

        this.manager.claimLevelReward(player, job, level);
        context.getViewer().refresh();
    }
}
