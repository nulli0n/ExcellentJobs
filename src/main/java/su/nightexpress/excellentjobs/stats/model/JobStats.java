package su.nightexpress.excellentjobs.stats.model;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.db.state.StatefulData;

@NullMarked
public class JobStats extends StatefulData {

    private final UUID   playerId;
    private final String jobId;

    private double income;

    public JobStats(UUID playerId, String jobId) {
        this.playerId = playerId;
        this.jobId = jobId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getJobId() {
        return jobId;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = Math.max(0, income);
    }

    public void addIncome(double amount) {
        this.setIncome(this.income + amount);
    }
}
