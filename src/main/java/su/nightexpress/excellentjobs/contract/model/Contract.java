package su.nightexpress.excellentjobs.contract.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.ContractPlaceholders;
import su.nightexpress.excellentjobs.contract.core.ContractPerms;
import su.nightexpress.excellentjobs.contract.definition.ContractBehavior;
import su.nightexpress.excellentjobs.contract.definition.ContractDefinition;
import su.nightexpress.excellentjobs.contract.definition.ContractPromotion;
import su.nightexpress.excellentjobs.contract.definition.ContractUnlocking;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class Contract implements PlaceholderResolvable {

    private final String id;

    private final ContractDefinition definition;
    private final ContractBehavior   behavior;
    private final ContractUnlocking  unlocking;
    private final ContractPromotion  promotion;

    public Contract(String id,
                    ContractDefinition definition,
                    ContractBehavior behavior,
                    ContractUnlocking unlocking,
                    ContractPromotion completion) {
        this.id = id;
        this.definition = definition;
        this.behavior = behavior;
        this.unlocking = unlocking;
        this.promotion = completion;
    }

    @Override
    public PlaceholderResolver placeholders() {
        return ContractPlaceholders.CONTRACT.resolver(this);
    }

    public String getId() {
        return this.id;
    }

    public int getPriority() {
        return this.definition.getPriority();
    }

    public String getPermission(Job job) {
        return ContractPerms.getJobContractPermission(this.id, job.getId());
    }

    public ContractDefinition getDefinition() {
        return this.definition;
    }

    public ContractBehavior getBehavior() {
        return this.behavior;
    }

    public ContractUnlocking getUnlocking() {
        return this.unlocking;
    }

    public ContractPromotion getPromotion() {
        return this.promotion;
    }
}
