package su.nightexpress.excellentjobs.grind.visual;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardProceedEvent;
import su.nightexpress.excellentjobs.api.grind.bar.BarElement;
import su.nightexpress.excellentjobs.job.data.JobData;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobGrinding;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class GrindBarManager extends AbstractManager<JobsPlugin> {

    private final Path             settingsFile;
    private final GrindBarSettings settings;

    private final Map<UUID, Map<String, GrindBar>> barByIdMap;

    public GrindBarManager(JobsPlugin plugin, Path settingsFile) {
        super(plugin);
        this.settingsFile = settingsFile;
        this.settings = new GrindBarSettings();

        this.barByIdMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        this.loadSettings();

        this.addListener(new GrindBarListener(this.plugin, this));

        this.addAsyncTask(this::tickBars, this.settings.getTickInterval());
    }

    @Override
    protected void onShutdown() {
        this.barByIdMap.values().forEach(map -> map.values().forEach(GrindBar::discard));
        this.barByIdMap.clear();

    }

    private void loadSettings() {
        FileConfig.load(this.settingsFile).edit(this.settings::load);
    }

    private void tickBars() {
        int stayTicks = this.settings.getStayTicks();

        this.barByIdMap.values().removeIf(map -> {
            map.values().removeIf(bar -> {
                int ticksLived = bar.getTicksLived();
                if (ticksLived >= stayTicks) {
                    bar.discard();
                    return true;
                }
                bar.tick();
                bar.render();
                return false;
            });
            return map.isEmpty();
        });
    }

    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.removeAll(player);
    }

    public void handleGrindRewardEvent(GrindRewardProceedEvent event) {
        List<BarElement> elements = event.getBarElements();
        if (elements.isEmpty()) return;

        Player player = event.getPlayer();
        Job job = event.getJob();
        JobData data = event.getJobData();
        GrindBar bar = this.getBarOrCreate(player, job, data);
        if (bar == null) return;

        bar.resetTicks();
        bar.addElements(elements);
        bar.render();
    }

    public void removeAll(Player player) {
        Map<String, GrindBar> map = this.barByIdMap.remove(player.getUniqueId());
        if (map == null) return;

        map.values().forEach(GrindBar::discard);
    }

    public @Nullable GrindBar getBarOrCreate(Player player, Job job, JobData data) {
        JobGrinding grinding = job.getGrinding();
        if (grinding == null) return null;

        GrindBar bar = this.getBar(player.getUniqueId(), job.getId());
        if (bar == null) {

            NightBarColor color = grinding.getBarColor();
            NightBarOverlay overlay = this.settings.getBossBarOverlay();
            String prefix = this.settings.getPrefix();
            String delimiter = this.settings.getElementDelimiter();

            bar = new GrindBar(job, data, color, overlay, prefix, delimiter);
            bar.addViewer(player);
            this.barByIdMap.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(job.getId(), bar);
        }
        return bar;
    }

    public Map<UUID, Map<String, GrindBar>> getBarByIdMap() {
        return this.barByIdMap;
    }

    public @Nullable GrindBar getBar(UUID playerId, String jobId) {
        return this.barByIdMap.getOrDefault(playerId, Map.of()).get(LowerCase.INTERNAL.apply(jobId));
    }
}
