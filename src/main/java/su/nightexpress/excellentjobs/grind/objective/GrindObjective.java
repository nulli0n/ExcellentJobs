package su.nightexpress.excellentjobs.grind.objective;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.table.SourceTable;

@NullMarked
public class GrindObjective {

    private final GrindType<?>            grindType;
    private final @Nullable GrindModifier modifier;
    private final SourceTable             sourceTable;

    public GrindObjective(GrindType<?> grindType,
                          @Nullable GrindModifier modifier,
                          SourceTable grindTable) {
        this.grindType = grindType;
        this.modifier = modifier;
        this.sourceTable = grindTable;
    }

    public String getTypeId() {
        return this.grindType.getId();
    }

    public @Nullable GrindModifier getModifier() {
        return this.modifier;
    }

    public SourceTable getSourceTable() {
        return this.sourceTable;
    }
}
