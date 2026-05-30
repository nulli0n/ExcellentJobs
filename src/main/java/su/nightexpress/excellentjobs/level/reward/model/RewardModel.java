package su.nightexpress.excellentjobs.level.reward.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.leveling.Reward;
import su.nightexpress.excellentjobs.api.leveling.RewardProvider;
import su.nightexpress.excellentjobs.api.leveling.RewardRequirement;
import su.nightexpress.excellentjobs.level.reward.LevelReward;
import su.nightexpress.excellentjobs.level.reward.codec.RewardModelCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardModel implements RewardProvider {

    public static final ConfigCodec<RewardModel> CODEC = new RewardModelCodec();

    private final RewardBase               base;
    private final Map<String, RewardValue> values;

    public RewardModel(RewardBase base, Map<String, RewardValue> values) {
        this.base = base;
        this.values = values;
    }

    public RewardBase getBase() {
        return base;
    }

    public Map<String, RewardValue> getValues() {
        return values;
    }

    @Override
    public Reward get(int level) {
        PlaceholderContext.Builder ctxBuilder = PlaceholderContext.builder();

        this.values.forEach((name, data) -> {
            double start = data.getBase();
            double step = data.getStep();
            double value = start + (step * level);

            ctxBuilder.with("%" + name + "_raw%", () -> String.valueOf(value));
            ctxBuilder.with("%" + name + "%", () -> NumberUtil.format(value));
        });

        PlaceholderContext ctx = ctxBuilder.build();

        String name = ctx.apply(this.base.getName());
        List<String> description = ctx.apply(this.base.getDescription());
        List<String> commands = ctx.apply(this.base.getCommands());
        Set<RewardRequirement> requirements = this.base.getRequirements();

        return new LevelReward(name, description, commands, requirements);
    }
}
