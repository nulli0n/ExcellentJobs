package su.nightexpress.excellentjobs.grind.adapter.impl;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import su.nightexpress.excellentjobs.grind.adapter.AbstractGrindAdapter;

@NullMarked
public class MythicMobAdapter extends AbstractGrindAdapter<MythicMob, Entity> {

    public MythicMobAdapter(String name) {
        super(name, "mythicmobs");
    }

    private static MythicBukkit getAPI() {
        return MythicBukkit.inst();
    }

    @Override
    public boolean isCaseSensetive() {
        return true;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getInternalName(MythicMob type) {
        return type.getInternalName();
    }

    @Override
    public boolean canHandle(Entity entity) {
        return getAPI().getMobManager().isMythicMob(entity);
    }

    @Override
    public @Nullable MythicMob adaptFromName(String name) {
        return getAPI().getMobManager().getMythicMob(name).orElse(null);
    }

    @Override
    public @Nullable MythicMob adaptFromBukkit(Entity entity) {
        ActiveMob activeMob = getAPI().getMobManager().getMythicMobInstance(entity);
        if (activeMob == null) return null;

        return activeMob.getType();
    }
}
