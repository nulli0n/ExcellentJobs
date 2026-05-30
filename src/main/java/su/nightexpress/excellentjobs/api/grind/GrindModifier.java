package su.nightexpress.excellentjobs.api.grind;

import org.jspecify.annotations.NullMarked;

@FunctionalInterface
@NullMarked
public interface GrindModifier {

    void modify(GrindReward reward, GrindContext context);
}
