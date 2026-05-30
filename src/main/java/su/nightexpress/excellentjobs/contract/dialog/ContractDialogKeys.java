package su.nightexpress.excellentjobs.contract.dialog;

import su.nightexpress.excellentjobs.contract.model.ContractJob;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public class ContractDialogKeys {

    private ContractDialogKeys() {
    }

    public static final DialogKey<ContractJob> CONTRACT_ACTIVATION   = new DialogKey<>("contract_activation");
    public static final DialogKey<ContractJob> CONTRACT_CANCELLATION = new DialogKey<>("contract_cancellation");
    public static final DialogKey<ContractJob> CONTRACT_PROMOTION    = new DialogKey<>("contract_promotion");
}
