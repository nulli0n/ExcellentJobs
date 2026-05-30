package su.nightexpress.excellentjobs.api.grind;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GrindProtection {

    boolean isGrindAllowed(Player player);

    boolean isNaturalBlock(Block block);

    boolean isNaturalMob(Entity entity);

    boolean isArtificalBlock(Block block);

    boolean isArtificalMob(Entity entity);
}
