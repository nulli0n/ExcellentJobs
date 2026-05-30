package su.nightexpress.excellentjobs.grind;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import su.nightexpress.excellentjobs.api.grind.GrindAdapterFamily;

public class DefaultAdapterFamilies {

    private DefaultAdapterFamilies() {
    }

    public static final GrindAdapterFamily<Entity>      ENTITY      = new GrindAdapterFamily<>();
    public static final GrindAdapterFamily<Block>       BLOCK       = new GrindAdapterFamily<>();
    public static final GrindAdapterFamily<BlockState>  BLOCK_STATE = new GrindAdapterFamily<>();
    public static final GrindAdapterFamily<ItemStack>   ITEM        = new GrindAdapterFamily<>();
    public static final GrindAdapterFamily<Enchantment> ENCHANTMENT = new GrindAdapterFamily<>();
    public static final GrindAdapterFamily<PotionType>  POTION      = new GrindAdapterFamily<>();

}
