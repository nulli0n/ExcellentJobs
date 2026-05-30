package su.nightexpress.excellentjobs.api.grind;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GrindType<B> {

    @Nullable
    Class<? extends GrindModifier> getModifierType();

    String getId();

    GrindAdapterFamily<B> getFamily();
}
