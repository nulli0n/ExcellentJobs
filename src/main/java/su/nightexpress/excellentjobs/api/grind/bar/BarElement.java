package su.nightexpress.excellentjobs.api.grind.bar;

import org.jspecify.annotations.NonNull;

public interface BarElement extends Comparable<BarElement> {

    @NonNull
    String getId();

    int getPosition();

    void merge(@NonNull BarElement from);

    @NonNull
    String render();

    boolean shouldRender();

    default double getBarFill() {
        return 0D;
    }

    @Override
    default int compareTo(BarElement other) {
        return Integer.compare(this.getPosition(), other.getPosition());
    }
}
