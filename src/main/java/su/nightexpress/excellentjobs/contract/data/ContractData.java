package su.nightexpress.excellentjobs.contract.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.db.state.StatefulData;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class ContractData extends StatefulData {

    private final UUID        playerId;
    private final String      jobId;
    private final Set<String> completedIds;

    private boolean active;

    private String contractId;
    private double contractPoints;

    private long leaveCooldown;

    private boolean promotionStarted;
    private long    promotionDeadline;
    private boolean promotionCompleted;
    private long    promotionCooldown;

    public ContractData(UUID playerId, String jobId) {
        this.playerId = playerId;
        this.jobId = jobId;
        this.contractId = "null";
        this.completedIds = new HashSet<>();
    }

    public static ContractData create(Player player, Job job, Contract contract) {
        UUID playerId = player.getUniqueId();
        String jobId = job.getId();
        String contractId = contract.getId();

        ContractData data = new ContractData(playerId, jobId);

        data.setActive(true);
        data.setContractId(contractId);
        data.setContractPoints(0);
        data.setLeaveCooldown(0L);
        data.setPromotionStarted(false);
        data.setPromotionDeadline(0L);
        data.setPromotionCompleted(false);
        data.setPromotionCooldown(0L);

        return data;
    }

    public boolean isContract(String id) {
        return this.contractId.equalsIgnoreCase(id);
    }

    public boolean isEffectivelyActive() {
        return this.isActive() && !this.isRemoved();
    }

    public void resetPromotion() {
        this.resetPromotionButCooldown();
        this.promotionCooldown = 0L;
    }

    public void resetPromotionButCooldown() {
        this.promotionStarted = false;
        this.promotionDeadline = 0L;
        this.promotionCompleted = false;
    }

    public void resetPoints() {
        this.contractPoints = 0;
    }

    public void addPoints(double amount) {
        this.setContractPoints(this.contractPoints + amount);
    }

    public boolean isCompleted(String id) {
        return this.completedIds.contains(LowerCase.INTERNAL.apply(id));
    }

    public boolean isLeaveCooldownExpired() {
        return TimeUtil.isPassed(this.leaveCooldown);
    }

    public boolean isPromotionDeadlineExceeded() {
        return TimeUtil.isPassed(this.promotionDeadline);
    }

    public boolean isPromotionCooldownExpired() {
        return TimeUtil.isPassed(this.promotionCooldown);
    }

    public boolean isPromotionOnCooldown() {
        return this.promotionCooldown != 0L;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getJobId() {
        return this.jobId;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInactive() {
        return !this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<String> getCompletedIds() {
        return completedIds;
    }

    public void setCompletedIds(Collection<String> ids) {
        this.completedIds.clear();
        this.completedIds.addAll(ids);
    }

    public void addCompletedId(String id) {
        this.completedIds.add(LowerCase.INTERNAL.apply(id));
    }

    public String getContractId() {
        return this.contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public double getContractPoints() {
        return this.contractPoints;
    }

    public void setContractPoints(double contractPoints) {
        this.contractPoints = Math.max(0D, contractPoints);
    }

    public long getLeaveCooldown() {
        return this.leaveCooldown;
    }

    public void setLeaveCooldown(long leaveCooldown) {
        this.leaveCooldown = leaveCooldown;
    }

    public boolean isPromotionStarted() {
        return this.promotionStarted;
    }

    public void setPromotionStarted(boolean promotionStarted) {
        this.promotionStarted = promotionStarted;
    }


    public long getPromotionDeadline() {
        return this.promotionDeadline;
    }

    public void setPromotionDeadline(long promotionDeadline) {
        this.promotionDeadline = promotionDeadline;
    }

    public boolean isPromotionCompleted() {
        return this.promotionCompleted;
    }

    public void setPromotionCompleted(boolean promotionCompleted) {
        this.promotionCompleted = promotionCompleted;
    }

    public long getPromotionCooldown() {
        return this.promotionCooldown;
    }

    public void setPromotionCooldown(long promotionCooldown) {
        this.promotionCooldown = promotionCooldown;
    }
}
