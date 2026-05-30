package su.nightexpress.excellentjobs.api.leveling;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface RewardProvider {

    Reward get(int level);
}
