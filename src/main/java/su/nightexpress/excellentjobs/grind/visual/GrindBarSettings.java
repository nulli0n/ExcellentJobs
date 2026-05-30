package su.nightexpress.excellentjobs.grind.visual;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class GrindBarSettings extends AbstractConfig {

    private final ConfigProperty<Integer> barTickInterval = this.addProperty(ConfigCodecs.INT,
        "GrindBar.Tick-Interval",
        1,
        ""
    );

    private final ConfigProperty<Integer> barStayTicks = this.addProperty(ConfigCodecs.INT,
        "GrindBar.Stay-Ticks",
        5,
        ""
    );

    private final ConfigProperty<String> barPrefix = this.addProperty(
        ConfigCodecs.STRING,
        "GrindBar.Prefix",
        JobsPlaceholders.JOB_NAME + " " +
            TagWrappers.GRAY.wrap("(Lv. " + TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_DATA_LEVEL) + ")") + " ",
        ""
    );

    private final ConfigProperty<String> barElementDelimiter = this.addProperty(
        ConfigCodecs.STRING,
        "GrindBar.Element-Delimiter",
        TagWrappers.GRAY.wrap(" | "),
        ""
    );

    private final ConfigProperty<NightBarOverlay> barOverlay = this.addProperty(
        ConfigCodecs.forEnum(NightBarOverlay.class),
        "GrindBar.BossBar.Overlay",
        NightBarOverlay.PROGRESS,
        ""
    );

    public int getTickInterval() {
        return this.barTickInterval.get();
    }

    public int getStayTicks() {
        return this.barStayTicks.get();
    }

    public String getPrefix() {
        return this.barPrefix.get();
    }

    public String getElementDelimiter() {
        return this.barElementDelimiter.get();
    }

    public NightBarOverlay getBossBarOverlay() {
        return this.barOverlay.get();
    }
}
