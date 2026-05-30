package su.nightexpress.excellentjobs.level.bar;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.excellentjobs.api.grind.bar.BarElement;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class LevelingBarElement implements BarElement {

    private final String format;

    private double xp;
    private double progress;

    public LevelingBarElement(String format, double xp, double progress) {
        this.format = format;

        this.xp = xp;
        this.progress = progress;
    }

    @Override
    public boolean shouldRender() {
        return this.xp != 0D;
    }

    @Override
    public String getId() {
        return "leveling";
    }

    @Override
    public int getPosition() {
        return 1;
    }

    @Override
    public void merge(BarElement from) {
        if (from instanceof LevelingBarElement element) {
            this.xp += element.xp;
            this.progress = element.progress;
        }
    }

    @Override
    public double getBarFill() {
        return this.progress;
    }

    @Override
    public String render() {
        PlaceholderContext context = PlaceholderContext.builder()
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(this.xp))
            .with(JobsPlaceholders.GENERIC_PROGRESS, () -> NumberUtil.format(this.progress * 100D))
            .build();

        return context.apply(this.format);
    }
}
