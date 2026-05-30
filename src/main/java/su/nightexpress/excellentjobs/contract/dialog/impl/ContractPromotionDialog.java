package su.nightexpress.excellentjobs.contract.dialog.impl;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.excellentjobs.contract.ContractPlaceholders;
import su.nightexpress.excellentjobs.contract.core.ContractLang;
import su.nightexpress.excellentjobs.contract.model.ContractJob;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class ContractPromotionDialog extends Dialog<ContractJob> {

    private static final TextLocale TITLE = ContractLang.builder("Dialog.Promotion.Title")
        .text(title("Contract", "Promotion"));

    private static final DialogElementLocale BODY = ContractLang.builder("Dialog.Promotion.Body")
        .dialogElement(
            400,
            "You're about to start promotion for the " + TagWrappers.YELLOW.wrap(ContractPlaceholders.CONTRACT_NAME) +
                " contract of " +
                TagWrappers.GOLD.wrap(JobsPlaceholders.JOB_NAME) + " job.",
            "",
            TagWrappers.SOFT_RED.wrap("Please review the terms and conditions of the promotion before confirming:"),
            "",
            TagWrappers.ORANGE.wrap("- You will have a limited time to complete it;"),
            TagWrappers.ORANGE.wrap("- In case of failure, you will have to wait before you can try again;"),
            TagWrappers.ORANGE.wrap("- Promotion can not be cancelled once started;"),
            TagWrappers.ORANGE.wrap("- Promotion progress resets if you cancel the contract or quit the job;")
        );

    private final ContractManager manager;

    public ContractPromotionDialog(@NonNull ContractManager manager) {
        this.manager = manager;
    }

    @Override
    public @NonNull WrappedDialog create(@NonNull Player player, @NonNull ContractJob data) {
        PlaceholderContext placeholderContext = PlaceholderContext.builder()
            .with(data.contract().placeholders())
            .with(data.job().placeholders())
            .build();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY.replace(placeholderContext)))
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                this.manager.startPromotion(player, data.contract(), data.job());
                viewer.callback();
            });
        });
    }
}
