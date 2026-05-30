package su.nightexpress.excellentjobs.level.core;

import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.api.leveling.Progression;
import su.nightexpress.excellentjobs.level.LevelingConstants;
import su.nightexpress.excellentjobs.level.progression.LinearProgression;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class LevelingSettings extends AbstractConfig {

    private final ConfigProperty<String> progressionType = this.addProperty(ConfigCodecs.STRING,
        "Progression.Type",
        LinearProgression.NAME,
        ""
    );

    private final ConfigProperty<Boolean> progressionResetOnLeave = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Progression.Reset-On-Leave",
        false,
        "Controls whether all leveling progress will be lost when player quits the job."
    );

    private final ConfigProperty<Boolean> rewardsClaimRequired = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Rewards.Claim-Required",
        true,
        ""
    );

    private final ConfigProperty<Boolean> grindBarElementEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "GrindBar.Element.Enabled",
        true,
        ""
    );

    private final ConfigProperty<String> grindBarElementFormat = this.addProperty(
        ConfigCodecs.STRING,
        "GrindBar.Element.Format",
        TagWrappers.YELLOW.wrap("+" + CommonPlaceholders.GENERIC_AMOUNT + " XP"),
        ""
    );

    private final ConfigProperty<Boolean> leaderboardTrackerEnabled = this.addProperty(
        ConfigCodecs.BOOLEAN,
        "Leaderboard.Tracker.Enabled",
        true,
        ""
    );

    private final ConfigProperty<String> leaderboardTrackerName = this.addProperty(
        ConfigCodecs.STRING,
        "Leaderboard.Tracker.Name",
        TagWrappers.COLOR.with(LevelingConstants.ACCENT_COLOR).and(TagWrappers.BOLD).wrap("Levels"),
        ""
    );

    private final ConfigProperty<List<String>> leaderboardTrackerDescription = this.addProperty(
        ConfigCodecs.STRING_LIST,
        "Leaderboard.Tracker.Description",
        Lists.newList(
            TagWrappers.GRAY.wrap("View players with highest levels!")
        ),
        ""
    );

    private final ConfigProperty<NightItem> leaderboardTrackerIcon = this.addProperty(
        ConfigCodecs.NIGHT_ITEM,
        "Leaderboard.Tracker.Icon",
        NightItem.fromType(Material.EXPERIENCE_BOTTLE),
        ""
    );

    public @Nullable Progression loadProgression(FileConfig config,
                                                 String name,
                                                 ConfigCodec<? extends Progression> codec,
                                                 Progression def) {
        String path = "Progression.Settings." + name;
        if (!config.contains(path)) {
            config.set(path, def);
        }
        return config.get("Progression.Settings." + name, codec);
    }

    public String getProgressionType() {
        return this.progressionType.get();
    }

    public boolean isProgressionResetOnLeave() {
        return this.progressionResetOnLeave.get();
    }

    public boolean isRewardClaimRequired() {
        return this.rewardsClaimRequired.get();
    }

    public boolean isBarElementEnabled() {
        return this.grindBarElementEnabled.get();
    }

    public String getBarElementFormat() {
        return this.grindBarElementFormat.get();
    }

    public boolean isLeaderboardTrackerEnabled() {
        return this.leaderboardTrackerEnabled.get();
    }

    public String getLeaderboardTrackerName() {
        return this.leaderboardTrackerName.get();
    }

    public List<String> getLeaderboardTrackerDescription() {
        return this.leaderboardTrackerDescription.get();
    }

    public NightItem getLeaderboardTrackerIcon() {
        return this.leaderboardTrackerIcon.get().copy();
    }
}
