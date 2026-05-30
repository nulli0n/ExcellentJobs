package su.nightexpress.excellentjobs.job.model;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.job.codec.JobGrindingCodec;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;

@NullMarked
public class JobGrinding {

    public static final JobGrindingCodec CODEC = new JobGrindingCodec();

    private NightBarColor barColor;
    private Set<String>   objectiveIds;

    public JobGrinding(NightBarColor barColor, Set<String> objectiveIds) {
        this.barColor = barColor;
        this.objectiveIds = objectiveIds;
    }

    public NightBarColor getBarColor() {
        return barColor;
    }

    public Set<String> getObjectiveIds() {
        return objectiveIds;
    }
}
