package su.nightexpress.excellentjobs.grind.adapter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import su.nightexpress.excellentjobs.grind.adapter.vanilla.VanillaNamespacedAdapter;
import su.nightexpress.nightcore.util.bridge.RegistryType;

public class DefaultGrindAdapters {

    private DefaultGrindAdapters() {
    }

    public static final VanillaNamespacedAdapter<EntityType, Entity> VANILLA_MOB = new VanillaNamespacedAdapter<>(
        "vanilla_mob", RegistryType.ENTITY_TYPE, Entity::getType
    );

    public static final VanillaNamespacedAdapter<Material, Block> VANILLA_BLOCK = new VanillaNamespacedAdapter<>(
        "vanilla_block", RegistryType.MATERIAL, Block::getType
    );

    public static final VanillaNamespacedAdapter<Material, BlockState> VANILLA_BLOCK_STATE = new VanillaNamespacedAdapter<>(
        "vanilla_block_state", RegistryType.MATERIAL, BlockState::getType
    );

    public static final VanillaNamespacedAdapter<Material, ItemStack> VANILLA_ITEM = new VanillaNamespacedAdapter<>(
        "vanilla_item", RegistryType.MATERIAL, ItemStack::getType
    );

    public static final VanillaNamespacedAdapter<PotionType, PotionType> VANILLA_POTION = new VanillaNamespacedAdapter<>(
        "potion", RegistryType.POTION, type -> type
    );

    public static final VanillaNamespacedAdapter<Enchantment, Enchantment> VANILLA_ENCHANTMENT = new VanillaNamespacedAdapter<>(
        "enchantment", RegistryType.ENCHANTMENT, enchantment -> enchantment
    );
}
