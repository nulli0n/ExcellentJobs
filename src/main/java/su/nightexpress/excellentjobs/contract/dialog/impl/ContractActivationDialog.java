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

public class ContractActivationDialog extends Dialog<ContractJob> {

    private static final TextLocale TITLE = ContractLang.builder("Dialog.Activation.Title")
        .text(title("Contract", "Activation"));

    private static final DialogElementLocale BODY = ContractLang.builder("Dialog.Activation.Body")
        .dialogElement(
            400,
            "You're about to activate the " + TagWrappers.YELLOW.wrap(ContractPlaceholders.CONTRACT_NAME) +
                " contract for " +
                TagWrappers.GREEN.wrap(JobsPlaceholders.JOB_NAME) + " job.",
            "",
            TagWrappers.SOFT_RED.wrap("Please review the terms and conditions of the contract before confirming:"),
            "",
            ContractPlaceholders.CONTRACT_DESCRIPTION
        );

    private final ContractManager manager;

    public ContractActivationDialog(@NonNull ContractManager manager) {
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
                this.manager.activateContract(player, data.job(), data.contract(), false);
                viewer.callback();
            });
        });
    }
}
