package su.nightexpress.excellentjobs.zone.listener;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import su.nightexpress.excellentjobs.JobsPlugin;
import su.nightexpress.excellentjobs.api.event.GrindRewardEvent;
import su.nightexpress.excellentjobs.zone.ZoneManager;
import su.nightexpress.excellentjobs.zone.activity.ActiveZone;
import su.nightexpress.excellentjobs.zone.core.ZoneLang;
import su.nightexpress.excellentjobs.zone.core.ZonePerms;
import su.nightexpress.excellentjobs.zone.model.BlockList;
import su.nightexpress.excellentjobs.zone.model.Zone;
import su.nightexpress.nightcore.manager.AbstractListener;

public class GenericZoneListener extends AbstractListener<JobsPlugin> {

    private final ZoneManager manager;

    public GenericZoneListener(JobsPlugin plugin, ZoneManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        this.manager.handleWorldLoad(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldUnload(WorldUnloadEvent event) {
        this.manager.handleWorldUnload(event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneJobIncome(GrindRewardEvent event) {
        this.manager.handleGrindReward(event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneEntityDamage(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();

        Zone zone = this.manager.getZone(victim);
        if (zone == null) return;

        DamageSource source = event.getDamageSource();
        if (!(source.getCausingEntity() instanceof Player damager)) return;

        ActiveZone activeZone = this.manager.getActiveZone(zone);
        if (activeZone == null || !activeZone.isAvailable(damager)) {
            ZoneLang.ZONE_NOT_AVAILABLE.message().send(damager);
            event.setCancelled(true);
            return;
        }

        if (victim instanceof Player) {
            ZoneLang.ZONE_NO_PVP.message().send(damager);
            event.setCancelled(!zone.getBehavior().isPvpAllowed());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneDecorationBreak(HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player player)) return;
        if (player.hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return;

        event.setCancelled(this.manager.isInZone(player));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return;

        Block block = event.getBlock();
        Zone zone = this.manager.getZone(block);
        if (zone == null) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneBlockProtection(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return;

        Block block = event.getBlock();
        Zone zone = this.manager.getZone(block);
        if (zone == null) return;

        Player player = event.getPlayer();
        BlockList blockList = zone.getBehavior().getBlockList(block.getType());
        if (blockList == null) {
            event.setCancelled(true);
            return;
        }

        ActiveZone activeZone = this.manager.getActiveZone(zone);
        if (activeZone == null || !activeZone.isAvailable(player)) {
            ZoneLang.ZONE_NOT_AVAILABLE.message().send(player);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onZoneBlockBreak(BlockBreakEvent event) {
        this.manager.handleBlockBreak(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProtectionSignChangeAndBlockUsage(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Player player = event.getPlayer();
        if (player.hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return;

        Zone zone = this.manager.getZone(player);
        if (zone == null) return;

        if (block.getState() instanceof Sign || zone.getBehavior().isDisabledInteraction(block.getType())) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onProtectionLuqidFill(PlayerBucketFillEvent event) {
        event.setCancelled(this.checkProtectionLuqid(event));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onProtectionLuqidEmpty(PlayerBucketEmptyEvent event) {
        event.setCancelled(this.checkProtectionLuqid(event));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onProtectionLuqidEntity(PlayerBucketEntityEvent event) {
        event.setCancelled(this.checkProtectionLuqid(event));
    }

    private boolean checkProtectionLuqid(PlayerEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return false;

        Zone zone = this.manager.getZone(player);
        return zone != null;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> manager.getZone(block) != null);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> manager.getZone(block) != null);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (event.getEntity().hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return;
        if (this.manager.isInZone(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onZoneBlockChange(EntityChangeBlockEvent event) {
        if (event.getEntity().hasPermission(ZonePerms.BYPASS_ZONE_PROTECTION)) return;
        if (this.manager.isInZone(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
