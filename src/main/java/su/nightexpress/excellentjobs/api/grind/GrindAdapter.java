package su.nightexpress.excellentjobs.api.grind;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GrindAdapter<B> {

    boolean isCaseSensetive();

    boolean canHandle(@NonNull B entity);

    boolean canHandle(String serializedName);

    int getPriority();

    String getId();

    @Nullable
    String serializeFromBukkit(@NonNull B entity);

    String serializeFromName(String internalName);
}
