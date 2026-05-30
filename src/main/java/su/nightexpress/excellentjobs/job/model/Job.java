package su.nightexpress.excellentjobs.job.model;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.job.core.JobPerms;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class Job implements PlaceholderResolvable {

    private final String        id;
    private final JobDefinition definition;

    private final @Nullable JobLeveling  leveling;
    private final @Nullable JobGrinding  grinding;
    private final @Nullable JobContracts contracts;

    private int employees = 0;

    public Job(String id,
               JobDefinition definition,
               @Nullable JobLeveling leveling,
               @Nullable JobGrinding grinding,
               @Nullable JobContracts contracts) {
        this.id = id;
        this.definition = definition;
        this.leveling = leveling;
        this.grinding = grinding;
        this.contracts = contracts;
    }

    @Override
    public PlaceholderResolver placeholders() {
        return JobsPlaceholders.JOB.resolver(this);
    }

    public String getPermission() {
        return JobPerms.getJobPermission(this.id);
    }

    public boolean hasPermission(Player player) {
        return !this.isPermissionRequired() || player.hasPermission(this.getPermission());
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = Math.max(0, employees);
    }

    public void addEmployee(int amount) {
        this.setEmployees(this.employees + amount);
    }

    public void removeEmployee(int amount) {
        this.addEmployee(-Math.abs(amount));
    }

    public String getId() {
        return this.id;
    }

    public JobDefinition getDefinition() {
        return definition;
    }

    public @Nullable JobLeveling getLeveling() {
        return leveling;
    }

    public @Nullable JobGrinding getGrinding() {
        return grinding;
    }

    public @Nullable JobContracts getContracts() {
        return contracts;
    }

    public String getName() {
        return this.definition.getName();
    }

    public List<String> getDescription() {
        return this.definition.getDescription();
    }

    public boolean isPermissionRequired() {
        return this.definition.isPermissionRequired();
    }

    public NightItem getIcon() {
        return this.definition.getIcon();
    }

    public List<String> getJoinCommands() {
        return this.definition.getJoinCommands();
    }

    public List<String> getLeaveCommands() {
        return this.definition.getLeaveCommands();
    }
}
