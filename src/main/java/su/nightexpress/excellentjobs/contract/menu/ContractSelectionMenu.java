package su.nightexpress.excellentjobs.contract.menu;

import java.util.ArrayList;
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
import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.excellentjobs.contract.ContractPlaceholders;
import su.nightexpress.excellentjobs.contract.definition.ContractDefinition;
import su.nightexpress.excellentjobs.contract.dialog.ContractDialogKeys;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.contract.model.ContractJob;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.item.populator.SlotPattern;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.format.adaptive.AdaptiveFormatter;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class ContractSelectionMenu extends AbstractObjectMenu<Job> {

    private static final String DEFAULT_TITLE = "%s • Contracts".formatted(JobsPlaceholders.JOB_NAME);

    private static final SlotPattern DEFAULT_SLOTS = new SlotPattern()
        .with(1, 13)
        .with(2, 12, 14)
        .with(3, 11, 13, 15)
        .with(4, 10, 12, 14, 16)
        .with(5, 10, 11, 13, 15, 16)
        .with(6, 10, 11, 12, 14, 15, 16)
        .with(7, 10, 11, 12, 13, 14, 15, 16);

    private final ContractManager                manager;
    private final AdaptiveFormatter<ContractJob> formatter;

    @Nullable
    private ItemPopulator<Contract> contractPopulator;

    public ContractSelectionMenu(JobsPlugin plugin,
                                 ContractManager manager,
                                 AdaptiveFormatter<ContractJob> formatter) {
        super(plugin, MenuType.GENERIC_9X4, DEFAULT_TITLE, Job.class);
        this.manager = manager;
        this.formatter = formatter;
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

        this.addNextPageButton(32);
        this.addPreviousPageButton(30);
    }

    @Override
    protected void onLoad(FileConfig config) {
        SlotPattern contractSlots = config.getOrSet("Contract.Slots", ConfigCodecs.SLOT_PATTERN, DEFAULT_SLOTS);

        String itemName = config.getOrSet("Contract.Name", ConfigCodecs.STRING, ContractPlaceholders.CONTRACT_NAME);
        List<String> itemLore = config.getOrSet("Contract.Lore", ConfigCodecs.STRING_LIST, List.of(
            "<if_active>" +
                TagWrappers.GREEN.wrap("✔" + " " + TagWrappers.GRAY.wrap("This is your current contract.")) +
                "</if_active>",
            "<if_completed>" +
                TagWrappers.GREEN.wrap("✔" + " " + TagWrappers.GRAY.wrap("You have been promoted in this contract.")) +
                "</if_completed>",
            "<if_can_not_use>" +
                TagWrappers.RED.wrap("✘" + " " + TagWrappers.UNDERLINED.wrap("You do not met requirements.")) +
                "</if_can_not_use>",
            CommonPlaceholders.EMPTY_IF_ABOVE,
            ContractPlaceholders.CONTRACT_DESCRIPTION,
            CommonPlaceholders.EMPTY_IF_BELOW,
            "<if_available>" +
                TagWrappers.GREEN.wrap("→" + " " + TagWrappers.UNDERLINED.wrap("Click to activate!")) +
                "</if_available>"
        ));

        this.contractPopulator = ItemPopulator.builder(Contract.class)
            .slots(contractSlots)
            .itemProvider((context, contract) -> {
                Player player = context.getPlayer();
                Job job = this.getObject(context);
                ContractJob contractJob = new ContractJob(contract, job);
                ContractDefinition definition = contract.getDefinition();

                List<String> lore = new ArrayList<>();
                itemLore.forEach(line -> {
                    String formatted = this.formatter.formatLine(line, contractJob, player);

                    if (!formatted.isEmpty() || line.isEmpty()) lore.add(formatted);
                });

                return definition.getIcon()
                    .setDisplayName(itemName)
                    .setLore(lore)
                    .replace(builder -> builder.with(contract.placeholders()))
                    .hideAllComponents();
            })
            .actionProvider(contract -> context -> this.handleContract(context, contract))
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
        Job job = this.getObject(context);

        List<Contract> contracts = this.manager.getContractsForJob(job)
            .stream()
            .sorted(Comparator.comparingInt(Contract::getPriority))
            .toList();
        if (contracts.isEmpty()) return;

        if (this.contractPopulator != null) {
            this.contractPopulator.populateTo(context, contracts, items);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleContract(ActionContext context, Contract contract) {
        Player player = context.getPlayer();
        Job job = this.getObject(context);
        if (this.manager.getCurrenctContract(player, job) == contract) return;

        if (this.manager.getSettings().isActivationConfirmation()) {
            this.plugin.showDialog(player, ContractDialogKeys.CONTRACT_ACTIVATION, new ContractJob(contract, job),
                player::closeInventory);
            return;
        }

        player.closeInventory();
        this.manager.activateContract(player, job, contract, false);
    }

    private void handleBack(ActionContext context) {
        Player player = context.getPlayer();
        Job job = this.getObject(context);

        if (this.manager.hasActiveContract(player, job)) {
            this.manager.openContractOptions(player, job);
        }
        else {
            this.manager.showJobSettingsOrSelection(player, job);
        }
    }
}
