package su.nightexpress.excellentjobs.api.grind;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class GrindAdapterFamily<B> {

    private final Set<GrindAdapter<B>> adapters;

    public GrindAdapterFamily() {
        this.adapters = new HashSet<>();
    }

    public <A extends GrindAdapter<B>> void addAdapter(A adapter) {
        this.adapters.add(adapter);
    }

    public Set<GrindAdapter<B>> getAdapters() {
        return this.adapters;
    }

    public Set<GrindAdapter<B>> getAdaptersFor(B entity) {
        return this.adapters.stream().filter(adapter -> adapter.canHandle(entity)).collect(Collectors.toSet());
    }

    public @Nullable GrindAdapter<B> getBestAdapterFor(B entity) {
        return this.getAdaptersFor(entity).stream()
            .max(Comparator.comparingInt(GrindAdapter::getPriority))
            .orElse(null);
    }
}
