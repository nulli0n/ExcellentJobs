package su.nightexpress.excellentjobs.grind.type;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.grind.GrindAdapterFamily;
import su.nightexpress.excellentjobs.api.grind.GrindModifier;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class GenericGrindType<T> implements GrindType<T> {

    protected final String                                   id;
    protected final GrindAdapterFamily<T>                    family;
    protected final @Nullable Class<? extends GrindModifier> modifierType;

    public GenericGrindType(String id,
                            GrindAdapterFamily<T> family,
                            @Nullable Class<? extends GrindModifier> modifierType) {
        this.id = LowerCase.INTERNAL.apply(id);
        this.family = family;
        this.modifierType = modifierType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public GrindAdapterFamily<T> getFamily() {
        return this.family;
    }

    @Override
    public @Nullable Class<@NonNull ? extends GrindModifier> getModifierType() {
        return this.modifierType;
    }
}
