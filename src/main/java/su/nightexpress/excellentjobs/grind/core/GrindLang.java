package su.nightexpress.excellentjobs.grind.core;

import org.bukkit.Sound;

import su.nightexpress.excellentjobs.JobsPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class GrindLang implements LangContainer {

    private GrindLang() {
    }

    public static final MessageLocale PROTECTION_LIMIT_REACHED = LangEntry.builder("Grind.Protection.LimitReached")
        .chatMessage(Sound.BLOCK_BELL_RESONATE,
            TagWrappers.GRAY.wrap("You have reached your " +
                TagWrappers.WHITE.wrap(JobsPlaceholders.GENERIC_TYPE) + " limit for the " +
                TagWrappers.WHITE.wrap(JobsPlaceholders.JOB_NAME) + " job. This limit will expire in " +
                TagWrappers.ORANGE.wrap(CommonPlaceholders.GENERIC_TIME))
        );
}
