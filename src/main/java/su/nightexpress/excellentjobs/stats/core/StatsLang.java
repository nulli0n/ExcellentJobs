package su.nightexpress.excellentjobs.stats.core;

import su.nightexpress.excellentjobs.stats.StatsPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class StatsLang implements LangContainer {

    private StatsLang() {
    }

    public static final TextLocale TOP_ENTRY_EMPTY = LangEntry.builder("Stats.TopEntry.Empty")
        .text("< none >");

    public static final IconLocale UI_LEADERBOARD_ENTRY = LangEntry.iconBuilder("Stats.UI.Leaderboard.Entry")
        .rawName(TagWrappers.YELLOW.wrap("#" + StatsPlaceholders.GENERIC_POS) + " " + TagWrappers.WHITE.wrap(
            CommonPlaceholders.PLAYER_NAME))
        .rawLore(TagWrappers.GREEN.wrap(StatsPlaceholders.GENERIC_SCORE))
        .build();
}
