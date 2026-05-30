package su.nightexpress.excellentjobs.grind;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.modifier.impl.BrewingGrindModifier;
import su.nightexpress.excellentjobs.grind.modifier.impl.CookingGrindModifier;
import su.nightexpress.excellentjobs.grind.modifier.impl.EnchantingGrindModifier;
import su.nightexpress.excellentjobs.grind.modifier.impl.MobKillGrindModifier;
import su.nightexpress.excellentjobs.grind.type.GenericGrindType;

public class DefaultGrindTypes {

    private DefaultGrindTypes() {
    }

    public static final GrindType<ItemStack>  BREW_INGREDIENT = new GenericGrindType<>("brewing", DefaultAdapterFamilies.ITEM, BrewingGrindModifier.class);
    public static final GrindType<PotionType> BREW_POTION     = new GenericGrindType<>("brew_potion", DefaultAdapterFamilies.POTION, BrewingGrindModifier.class);

    public static final GrindType<ItemStack> COOKING    = new GenericGrindType<>("cooking", DefaultAdapterFamilies.ITEM, CookingGrindModifier.class);
    public static final GrindType<ItemStack> CRAFTING   = new GenericGrindType<>("crafting", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> EATING     = new GenericGrindType<>("eating", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> FISH_ITEM  = new GenericGrindType<>("fishing_items", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> FORGING    = new GenericGrindType<>("forging", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> BLOCK_LOOT = new GenericGrindType<>("block_loot", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> MOB_LOOT   = new GenericGrindType<>("mob_loot", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> GRINDSTONE = new GenericGrindType<>("grindstone", DefaultAdapterFamilies.ITEM, null);
    public static final GrindType<ItemStack> SMITHING   = new GenericGrindType<>("smithing", DefaultAdapterFamilies.ITEM, null);

    public static final GrindType<Entity> BREEDING  = new GenericGrindType<>("breeding", DefaultAdapterFamilies.ENTITY, null);
    public static final GrindType<Entity> KILLING   = new GenericGrindType<>("killing", DefaultAdapterFamilies.ENTITY, MobKillGrindModifier.class);
    public static final GrindType<Entity> FISH_MOBS = new GenericGrindType<>("fishing_mobs", DefaultAdapterFamilies.ENTITY, null);
    public static final GrindType<Entity> MILKING   = new GenericGrindType<>("milking", DefaultAdapterFamilies.ENTITY, null);
    public static final GrindType<Entity> SHEARING  = new GenericGrindType<>("shearing", DefaultAdapterFamilies.ENTITY, null);
    public static final GrindType<Entity> TAMING    = new GenericGrindType<>("taming", DefaultAdapterFamilies.ENTITY, null);

    public static final GrindType<Block>      MINING      = new GenericGrindType<>("mining", DefaultAdapterFamilies.BLOCK, null);
    public static final GrindType<Block>      BUILDING    = new GenericGrindType<>("building", DefaultAdapterFamilies.BLOCK, null);
    public static final GrindType<BlockState> FERTILIZING = new GenericGrindType<>("fertilizing", DefaultAdapterFamilies.BLOCK_STATE, null);
    public static final GrindType<BlockState> STRIP_BLOCK = new GenericGrindType<>("strip_block", DefaultAdapterFamilies.BLOCK_STATE, null);

    public static final GrindType<Enchantment> ENCHANTING = new GenericGrindType<>("enchanting", DefaultAdapterFamilies.ENCHANTMENT, EnchantingGrindModifier.class);

}
