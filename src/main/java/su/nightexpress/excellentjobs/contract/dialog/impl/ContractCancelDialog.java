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

public class ContractCancelDialog extends Dialog<ContractJob> {

    private static final TextLocale TITLE = ContractLang.builder("Dialog.Cancellation.Title")
        .text(title("Contract", "Cancellation"));

    private static final DialogElementLocale BODY = ContractLang.builder("Dialog.Cancellation.Body")
        .dialogElement(
            400,
            "You're about to cancel the " + TagWrappers.YELLOW.wrap(ContractPlaceholders.CONTRACT_NAME) +
                " contract for " +
                TagWrappers.RED.wrap(JobsPlaceholders.JOB_NAME) + " job.",
            "",
            TagWrappers.SOFT_RED.wrap("Please review the terms and conditions of the cancellation before confirming:"),
            "",
            TagWrappers.RED.wrap("- All promotion progress will be reset;"),
            TagWrappers.RED.wrap("- A promotion cooldown will be applied;"),
            TagWrappers.RED.wrap("- A contract switch cooldown will be applied;")
        );

    private final ContractManager manager;

    public ContractCancelDialog(@NonNull ContractManager manager) {
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
                this.manager.cancelContract(player, data.job());
                viewer.callback();
            });
        });
    }
}
