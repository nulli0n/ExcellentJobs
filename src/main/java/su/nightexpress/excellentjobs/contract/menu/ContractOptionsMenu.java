package su.nightexpress.excellentjobs.contract.menu;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.bukkit.Color;
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
import su.nightexpress.excellentjobs.config.Lang;
import su.nightexpress.excellentjobs.contract.ContractConstants;
import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.excellentjobs.contract.ContractPlaceholders;
import su.nightexpress.excellentjobs.contract.data.ContractData;
import su.nightexpress.excellentjobs.contract.definition.ContractPromotion;
import su.nightexpress.excellentjobs.contract.dialog.ContractDialogKeys;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.contract.model.ContractJob;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class ContractOptionsMenu extends AbstractObjectMenu<ContractJob> {

    private static final String DEFAULT_TITLE = "%s • Contract Settings"
        .formatted(JobsPlaceholders.JOB_NAME);

    private final ContractManager manager;

    public ContractOptionsMenu(NightPlugin plugin, ContractManager contractManager) {
        super(plugin, MenuType.GENERIC_9X4, DEFAULT_TITLE, ContractJob.class);
        this.manager = contractManager;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        ContractJob src = this.getObject(context);
        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(src.contract().placeholders())
            .with(src.job().placeholders())
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
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 27);

        this.addDefaultButton("current_contract", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.MAP)
                    .setDisplayName(TagWrappers.COLOR.with(ContractConstants.ACCENT)
                        .and(TagWrappers.BOLD).wrap("Active Contract"))
                    .setLore(Lists.newList(
                        TagWrappers.DARK_GRAY.wrap("» ") + TagWrappers.GRAY.wrap("Current: ") + TagWrappers.WHITE
                            .wrap(ContractPlaceholders.CONTRACT_NAME),
                        CommonPlaceholders.EMPTY_IF_BELOW,
                        ContractPlaceholders.CONTRACT_DESCRIPTION
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .build()
            )
            .slots(10)
            .build()
        );

        this.addDefaultButton("contract_list", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BOOK)
                    .setDisplayName(TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("All Contracts"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("View all other contracts available"),
                        TagWrappers.GRAY.wrap("for this job."),
                        "",
                        TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to open"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .action(this::handleContractList)
                .build()
            )
            .slots(13)
            .build()
        );

        this.addDefaultButton("contract_promotion", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLASS_BOTTLE)
                    .setDisplayName(TagWrappers.WHITE.and(TagWrappers.BOLD).wrap("Contract Promotion"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("This contract has no promotion"),
                        TagWrappers.GRAY.wrap("available.")
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .condition(this::isPromotionDisabled)
                .build()
            )
            .state("available", ItemState.builder()
                .icon(NightItem.fromType(Material.POTION)
                    .setDisplayName(TagWrappers.AQUA.and(TagWrappers.BOLD).wrap("Contract Promotion"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Complete promotion in this contract"),
                        TagWrappers.GRAY.wrap("to get special rewards and unlock"),
                        TagWrappers.GRAY.wrap("even better contracts!"),
                        "",
                        TagWrappers.AQUA.and(TagWrappers.BOLD).wrap("Conditions:"),
                        TagWrappers.GRAY.wrap("Get " +
                            TagWrappers.AQUA.wrap(ContractPlaceholders.CONTRACT_PROMOTION_POINTS) +
                            " contract points " +
                            TagWrappers.GRAY.wrap("in ") +
                            TagWrappers.AQUA.wrap(ContractPlaceholders.CONTRACT_PROMOTION_DURATION)),
                        "",
                        TagWrappers.AQUA.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to start!"))
                    ))
                    .hideAllComponents()
                    .setColor(Color.AQUA)
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .condition(this::isPromotionAvailable)
                .action(this::handlePromotion)
                .build()
            )
            .state("started", ItemState.builder()
                .icon(NightItem.fromType(Material.POTION)
                    .setDisplayName(TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Contract Promotion"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Contract promotion is in progress."),
                        "",
                        TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Progress:"),
                        TagWrappers.DARK_GRAY.wrap(" » ") + TagWrappers.GRAY.wrap("Points: " +
                            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_AMOUNT) + "/" +
                            TagWrappers.WHITE.wrap(ContractPlaceholders.CONTRACT_PROMOTION_POINTS)),
                        TagWrappers.DARK_GRAY.wrap(" » ") + TagWrappers.GRAY.wrap("Timeleft: " +
                            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_TIME))
                    ))
                    .hideAllComponents()
                    .setColor(Color.LIME)
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> {
                        return this.formatData(context, data -> NumberUtil.format(data == null ? 0 : data
                            .getContractPoints()));
                    })
                    .with(CommonPlaceholders.GENERIC_TIME, () -> {
                        return this.formatData(context, data -> TimeFormats.formatDuration(data.getPromotionDeadline(),
                            TimeFormatType.LITERAL));
                    })
                ))
                .condition(this::isPromotionStarted)
                .build()
            )
            .state("cooldown", ItemState.builder()
                .icon(NightItem.fromType(Material.POTION)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Contract Promotion"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("You must wait before you can"),
                        TagWrappers.GRAY.wrap("start promotion again."),
                        "",
                        TagWrappers.RED.and(TagWrappers.BOLD).wrap("Cooldown:"),
                        TagWrappers.DARK_GRAY.wrap(" » ") + TagWrappers.GRAY.wrap("Timeleft: " +
                            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_TIME))
                    ))
                    .hideAllComponents()
                    .setColor(Color.RED)
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .condition(this::isPromotionCooldown)
                .build()
            )
            .state("completed", ItemState.builder()
                .icon(NightItem.fromType(Material.EXPERIENCE_BOTTLE)
                    .setDisplayName(TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("Contract Promotion"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("You have successfully completed"),
                        TagWrappers.GRAY.wrap("promotion of this contract."),
                        "",
                        TagWrappers.GRAY.wrap("Check out the " + TagWrappers.YELLOW.wrap("All Contracts") + " section"),
                        TagWrappers.GRAY.wrap("for better contracts!")
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .condition(this::isPromotionCompleted)
                .build()
            )
            .slots(16)
            .build()
        );

        this.addDefaultButton("cancel_contract", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Cancel Contract"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("You can cancel your current"),
                        TagWrappers.GRAY.wrap("contract."),
                        "",
                        TagWrappers.RED.wrap("[*] ") + TagWrappers.SOFT_RED.wrap("Contract progress will be lost."),
                        "",
                        TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to cancel"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                ))
                .condition(context -> !this.isContractOnLeaveCooldown(context))
                .action(this::handleCancellation)
                .build()
            )
            .state("cooldown", ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Cancel Contract"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("You " + TagWrappers.RED.wrap("can't") + " cancel your current"),
                        TagWrappers.GRAY.wrap("contract at this time."),
                        "",
                        TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEMS.apply("item/clock_12")) + TagWrappers.RED.wrap(
                            " You still have to work"),
                        TagWrappers.RED.wrap("for another " +
                            TagWrappers.SOFT_RED.wrap(CommonPlaceholders.GENERIC_TIME)),
                        TagWrappers.RED.wrap("before you can cancel contract.")
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> item.replace(builder -> builder
                    .with(this.getObject(context).contract().placeholders())
                    .with(this.getObject(context).job().placeholders())
                    .with(CommonPlaceholders.GENERIC_TIME, () -> {
                        ContractJob contractJob = this.getObject(context);
                        ContractData data = this.manager.getActiveContractData(context.getPlayer(), contractJob.job());
                        if (data == null || data.isLeaveCooldownExpired()) return Lang.OTHER_N_A.text();

                        return TimeFormats.formatDuration(data.getLeaveCooldown(), TimeFormatType.LITERAL);
                    })
                ))
                .condition(this::isContractOnLeaveCooldown)
                .build()
            )
            .slots(35)
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

    private String formatData(ViewerContext context,
                              Function<ContractData, String> function) {
        ContractJob src = this.getObject(context);
        ContractData data = this.manager.getContractData(context.getPlayer(), src.job());
        return data == null || !data.isEffectivelyActive() ? "?" : function.apply(data);
    }

    private boolean isContractOnLeaveCooldown(ViewerContext context) {
        Player player = context.getPlayer();
        ContractJob contractJob = this.getObject(context);
        Job job = contractJob.job();

        return this.manager.hasContractLeaveCooldown(player, job);
    }

    private boolean isPromotionDisabled(ViewerContext context) {
        return this.checkPromotion(context, (data, promotion) -> !promotion.isEnabled());
    }

    private boolean isPromotionAvailable(ViewerContext context) {
        return this.checkPromotion(context, (data, promotion) -> promotion.isEnabled() && !data
            .isPromotionStarted() && !data.isPromotionCompleted());
    }

    private boolean isPromotionStarted(ViewerContext context) {
        return this.checkPromotion(context, (data, promotion) -> promotion.isEnabled() && data
            .isPromotionStarted() && !data.isPromotionCompleted());
    }

    private boolean isPromotionCooldown(ViewerContext context) {
        return this.checkPromotion(context, (data, promotion) -> promotion.isEnabled() && !data
            .isPromotionStarted() && !data.isPromotionCompleted() && data.isPromotionOnCooldown());
    }

    private boolean isPromotionCompleted(ViewerContext context) {
        return this.checkPromotion(context, (data, promotion) -> promotion.isEnabled() && data.isPromotionCompleted());
    }

    private boolean checkPromotion(ViewerContext context,
                                   BiPredicate<ContractData, ContractPromotion> predicate) {
        Player player = context.getPlayer();
        ContractJob contractJob = this.getObject(context);
        Contract contract = contractJob.contract();
        Job job = contractJob.job();
        ContractData contractData = this.manager.getContractData(player, job);
        if (contractData == null || !contractData.isEffectivelyActive()) return false;

        ContractPromotion promotion = contract.getPromotion();
        return predicate.test(contractData, promotion);
    }

    private void handleBack(ActionContext context) {
        Player player = context.getPlayer();
        ContractJob contractJob = this.getObject(context);
        Job job = contractJob.job();

        this.manager.showJobSettingsOrSelection(player, job);
    }

    private void handleContractList(ActionContext context) {
        Player player = context.getPlayer();
        ContractJob contractJob = this.getObject(context);
        Job job = contractJob.job();

        this.manager.openContractSelection(player, job);
    }

    private void handleCancellation(ActionContext context) {
        Player player = context.getPlayer();
        ContractJob contractJob = this.getObject(context);

        this.plugin.showDialog(player, ContractDialogKeys.CONTRACT_CANCELLATION, contractJob, player::closeInventory);
    }

    private void handlePromotion(ActionContext context) {
        Player player = context.getPlayer();
        ContractJob contractJob = this.getObject(context);

        this.plugin.showDialog(player, ContractDialogKeys.CONTRACT_PROMOTION, contractJob, player::closeInventory);
    }
}
