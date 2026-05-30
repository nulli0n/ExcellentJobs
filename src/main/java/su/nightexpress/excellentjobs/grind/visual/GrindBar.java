package su.nightexpress.excellentjobs.grind.visual;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.bar.BarElement;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.util.BossBarUtils;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class GrindBar {

    private final Job     job;
    private final JobData data;

    private final String prefix;
    private final String delimiter;

    private final Map<String, BarElement> elements;

    private NightBossBar bar;
    private int          ticksLived;

    public GrindBar(Job job,
                    JobData data,
                    NightBarColor color,
                    NightBarOverlay overlay,
                    String prefix,
                    String delimiter) {
        this.job = job;
        this.data = data;

        this.prefix = prefix;
        this.delimiter = delimiter;

        this.elements = new HashMap<>();

        this.ticksLived = 0;

        this.bar = BossBarUtils.createBossBar("", color, overlay);
    }

    public void tick() {
        this.ticksLived++;
    }

    public void resetTicks() {
        this.ticksLived = 0;
    }

    public void addViewer(Player player) {
        this.bar.addViewer(player);
    }

    public void addElements(Collection<BarElement> elements) {
        elements.forEach(element -> {
            BarElement currentElement = this.elements.get(element.getId());
            if (currentElement != null) {
                currentElement.merge(element);
            }
            else {
                this.elements.put(element.getId(), element);
            }
        });
    }

    public void render() {
        PlaceholderContext ctx = PlaceholderContext.builder()
            .with(this.job.placeholders())
            .with(this.data.placeholders())
            .build();

        String elementsRendered = this.elements.values().stream()
            .filter(BarElement::shouldRender)
            .sorted()
            .map(BarElement::render)
            .collect(Collectors.joining(this.delimiter));

        double progress = this.elements.values().stream()
            .mapToDouble(BarElement::getBarFill)
            .max()
            .orElse(1D);

        String display = this.prefix + elementsRendered;

        this.bar.setName(NightMessage.parse(ctx.apply(display)));
        this.bar.setProgress((float) progress);
    }

    public void discard() {
        this.bar.removeViewers();
    }

    public Job getJob() {
        return this.job;
    }

    public int getTicksLived() {
        return this.ticksLived;
    }
}
