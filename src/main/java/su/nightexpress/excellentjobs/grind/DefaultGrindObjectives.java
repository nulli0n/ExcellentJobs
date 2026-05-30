package su.nightexpress.excellentjobs.grind;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.api.grind.GrindObjectiveProperty;
import su.nightexpress.excellentjobs.api.grind.GrindType;
import su.nightexpress.excellentjobs.grind.objective.GrindObjective;
import su.nightexpress.excellentjobs.grind.table.SourceTable;

@NullMarked
public class DefaultGrindObjectives {

    private DefaultGrindObjectives() {
    }

    public static final String MINER_BREAK_BLOCK = "miner_break_block";
    public static final String MINER_BLOCK_LOOT  = "miner_block_loot";
    public static final String MINER_CRAFT       = "miner_craft";
    public static final String MINER_SMITH       = "miner_smith";

    public static final String LUMBERJACK_BREAK_BLOCK = "lumberjack_break_block";
    public static final String LUMBERJACK_STRIP_BLOCK = "lumberjack_strip_block";
    public static final String LUMBERJACK_BLOCK_LOOT  = "lumberjack_block_loot";
    public static final String LUMBERJACK_CRAFT       = "lumberjack_craft";
    public static final String LUMBERJACK_SMITH       = "lumberjack_smith";

    public static final String FARMER_BREAK_BLOCK = "farmer_break_block";
    public static final String FARMER_CRAFT       = "farmer_craft";
    public static final String FARMER_SMITH       = "farmer_smith";

    public static final String FISHERMAN_FISH_ITEM = "fisherman_fish_item";
    public static final String FISHERMAN_CRAFT     = "fisherman_craft";
    public static final String FISHERMAN_KILL_MOB  = "fisherman_kill_mob";
    public static final String FISHERMAN_MOB_LOOT  = "fisherman_mob_loot";

    public static final String MONSTER_HUNTER_MOB_KILL    = "monster_hunter_kill_mob";
    public static final String MONSTER_HUNTER_BREAK_BLOCK = "monster_hunter_break_block";
    public static final String MONSTER_HUNTER_CRAFT       = "monster_hunter_craft";
    public static final String MONSTER_HUNTER_SMITH       = "monster_hunter_smith";

    public static final String BUTCHER_KILL_MOB = "butcher_kill_mob";
    public static final String BUTCHER_MOB_LOOT = "butcher_mob_loot";
    public static final String BUTCHER_COOK     = "butcher_cook";

    public static final String ANIMAL_KEEP_BREED = "animal_keeper_breed";
    public static final String ANIMAL_KEEP_SHEAR = "animal_keeper_shear";
    public static final String ANIMAL_KEEP_MILK  = "animal_keeper_milk";
    public static final String ANIMAL_KEEP_TAME  = "animal_keeper_tame";
    public static final String ANIMAL_KEEP_CRAFT = "animal_keeper_craft";

    public static final String ENCHANTER_ENCHANT = "enchanter_enchant";

    public static final String ALCHEMIST_BREW  = "alchemist_brew";
    public static final String ALCHEMIST_CRAFT = "alchemist_craft";

    public static final String BAKER_CRAFT = "baker_craft";
    public static final String BAKER_SMELT = "baker_smelt";
    public static final String BAKER_EAT   = "baker_eat";

    public static Map<String, GrindObjective> createDefaultObjectives() {
        Map<String, GrindObjective> objectives = new HashMap<>();

        objectives.put(MINER_BREAK_BLOCK, forMinerBreakBlock());
        objectives.put(MINER_BLOCK_LOOT, forMinerBlockLoot());
        objectives.put(MINER_CRAFT, forMinerCraft());
        objectives.put(MINER_SMITH, forMinerSmith());

        objectives.put(LUMBERJACK_BREAK_BLOCK, forLumberjackBreakBlock());
        objectives.put(LUMBERJACK_CRAFT, forLumberjackCraft());
        objectives.put(LUMBERJACK_BLOCK_LOOT, forLumberjackBlockLoot());
        objectives.put(LUMBERJACK_STRIP_BLOCK, forLumberjackStripBlock());
        objectives.put(LUMBERJACK_SMITH, forLumberjackSmith());

        objectives.put(FARMER_BREAK_BLOCK, forFarmerBlockBreak());
        objectives.put(FARMER_CRAFT, forFarmerCraft());
        objectives.put(FARMER_SMITH, forFarmerSmith());

        objectives.put(FISHERMAN_FISH_ITEM, forFishermanFishItem());
        objectives.put(FISHERMAN_CRAFT, forFishermanCraft());
        objectives.put(FISHERMAN_KILL_MOB, forFishermanKillMob());
        objectives.put(FISHERMAN_MOB_LOOT, forFishermanMobLoot());

        objectives.put(MONSTER_HUNTER_MOB_KILL, forMonsterHunterKillMob());
        objectives.put(MONSTER_HUNTER_BREAK_BLOCK, forMonsterHunterBreakBlock());
        objectives.put(MONSTER_HUNTER_CRAFT, forMonsterHunterCraft());
        objectives.put(MONSTER_HUNTER_SMITH, forMonsterHunterSmith());

        objectives.put(BUTCHER_KILL_MOB, forButcherKillMob());
        objectives.put(BUTCHER_MOB_LOOT, forButcherMobLoot());
        objectives.put(BUTCHER_COOK, forButcherCook());

        objectives.put(ANIMAL_KEEP_BREED, forAnimalKeeperBreed());
        objectives.put(ANIMAL_KEEP_SHEAR, forAnimalKeeperShear());
        objectives.put(ANIMAL_KEEP_MILK, forAnimalKeeperMilk());
        objectives.put(ANIMAL_KEEP_TAME, forAnimalKeeperTame());
        objectives.put(ANIMAL_KEEP_CRAFT, forAnimalKeeperCraft());

        objectives.put(ENCHANTER_ENCHANT, forEnchanterEnchant());

        objectives.put(ALCHEMIST_BREW, forAlchemistBrew());
        objectives.put(ALCHEMIST_CRAFT, forAlchemistCraft());

        objectives.put(BAKER_CRAFT, forBakerCraft());
        objectives.put(BAKER_SMELT, forBakerSmelt());
        objectives.put(BAKER_EAT, forBakerEat());

        return objectives;
    }

    private static GrindObjective forMinerBreakBlock() {
        SourceTable blockBreakTable = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Common Bulk Blocks
            .entry(Material.STONE).xp(12, 18).income(0.04, 0.06).contractPoints(0.008, 0.012).add()
            .entry(Material.COBBLESTONE).xp(12, 18).income(0.04, 0.06).contractPoints(0.008, 0.012).add()
            .entry(Material.DEEPSLATE).xp(14, 22).income(0.05, 0.07).contractPoints(0.012, 0.018).add()
            .entry(Material.SANDSTONE).xp(10, 14).income(0.03, 0.05).contractPoints(0.008, 0.012).add()
            .entry(Material.NETHERRACK).xp(6, 10).income(0.01, 0.03).contractPoints(0.004, 0.006).add()

            // Uncommon Pockets
            .entry(Material.ANDESITE).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.DIORITE).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.GRANITE).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.TUFF).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.TERRACOTTA).xp(14, 22).income(0.05, 0.07).contractPoints(0.012, 0.018).add()

            // Dimension Specific / Harder Blocks
            .entry(Material.BASALT).xp(18, 26).income(0.10, 0.14).contractPoints(0.02, 0.03).add()
            .entry(Material.BLACKSTONE).xp(18, 26).income(0.10, 0.14).contractPoints(0.02, 0.03).add()
            .entry(Material.CALCITE).xp(20, 30).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.END_STONE).xp(20, 30).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.PRISMARINE_BRICKS).xp(24, 36).income(0.16, 0.24).contractPoints(0.032, 0.048).add()

            // Ice Types
            .entry(Material.ICE).xp(8, 12).income(0.04, 0.06).contractPoints(0.008, 0.012).add()
            .entry(Material.BLUE_ICE).xp(32, 48).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .entry(Material.FROSTED_ICE).xp(0, 0).income(0.0, 0.0).contractPoints(0.0, 0.0).add()
            .build();

        return createObjective(DefaultGrindTypes.MINING, blockBreakTable);
    }

    private static GrindObjective forMinerBlockLoot() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Common Drops (High quantity per block)
            .entry(Material.REDSTONE).xp(8, 12).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.LAPIS_LAZULI).xp(8, 12).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.COAL).xp(12, 18).income(0.12, 0.18).contractPoints(0.04, 0.06).add()
            .entry(Material.QUARTZ).xp(12, 18).income(0.16, 0.24).contractPoints(0.032, 0.048).add()
            .entry(Material.RAW_COPPER).xp(16, 24).income(0.16, 0.24).contractPoints(0.048, 0.072).add()

            // Low Value / Secondary Drops
            .entry(Material.IRON_NUGGET).xp(4, 6).income(0.04, 0.06).contractPoints(0.008, 0.012).add()
            .entry(Material.GOLD_NUGGET).xp(4, 6).income(0.04, 0.06).contractPoints(0.008, 0.012).add()

            // Uncommon Drops
            .entry(Material.AMETHYST_BLOCK).xp(24, 36).income(0.32, 0.48).contractPoints(0.064, 0.096).add()
            .entry(Material.RAW_IRON).xp(32, 48).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(Material.RAW_GOLD).xp(48, 72).income(0.64, 0.96).contractPoints(0.12, 0.18).add()

            // Rare & High-Value Drops
            .entry(Material.EMERALD).xp(120, 180).income(2.00, 3.00).contractPoints(0.40, 0.60).add()
            .entry(Material.DIAMOND).xp(200, 300).income(4.00, 6.00).contractPoints(0.80, 1.20).add()
            .entry(Material.OBSIDIAN).xp(240, 360).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(Material.NETHERITE_SCRAP).xp(1200, 1800).income(20.00, 30.00).contractPoints(4.00, 6.00).add()
            .build();

        return createObjective(DefaultGrindTypes.BLOCK_LOOT, table);
    }

    private static GrindObjective forMinerCraft() {
        SourceTable minerCraftingTable = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.WOODEN_PICKAXE).xp(1, 2).income(0.01, 0.02).contractPoints(0.001, 0.002).add()
            .entry(Material.STONE_PICKAXE).xp(2, 4).income(0.04, 0.06).contractPoints(0.005, 0.010).add()
            .entry(Material.IRON_PICKAXE).xp(80, 120).income(1.60, 2.40).contractPoints(0.32, 0.48).add()
            .entry(Material.GOLDEN_PICKAXE).xp(40, 60).income(0.80, 1.20).contractPoints(0.16, 0.24).add()
            .entry(Material.DIAMOND_PICKAXE).xp(400, 600).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, minerCraftingTable);
    }

    private static GrindObjective forMinerSmith() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.NETHERITE_PICKAXE).xp(1200, 1400).income(24.00, 32.00).contractPoints(4.80, 7.20).add()
            .build();

        return createObjective(DefaultGrindTypes.SMITHING, table);
    }

    private static GrindObjective forLumberjackBreakBlock() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Basic Trees
            .entry(Material.OAK_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.OAK_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_OAK_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_OAK_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()

            .entry(Material.BIRCH_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.BIRCH_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_BIRCH_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_BIRCH_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()

            .entry(Material.SPRUCE_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.SPRUCE_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_SPRUCE_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_SPRUCE_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()

            .entry(Material.CHERRY_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.CHERRY_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_CHERRY_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_CHERRY_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()

            .entry(Material.PALE_OAK_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.PALE_OAK_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_PALE_OAK_LOG).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()
            .entry(Material.STRIPPED_PALE_OAK_WOOD).xp(14, 22).income(0.12, 0.18).contractPoints(0.032, 0.048).add()

            // Dense Trees
            .entry(Material.DARK_OAK_LOG).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.DARK_OAK_WOOD).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.STRIPPED_DARK_OAK_LOG).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.STRIPPED_DARK_OAK_WOOD).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()

            .entry(Material.JUNGLE_LOG).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.JUNGLE_WOOD).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.STRIPPED_JUNGLE_LOG).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.STRIPPED_JUNGLE_WOOD).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()

            .entry(Material.ACACIA_LOG).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.ACACIA_WOOD).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.STRIPPED_ACACIA_LOG).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()
            .entry(Material.STRIPPED_ACACIA_WOOD).xp(16, 24).income(0.14, 0.22).contractPoints(0.036, 0.054).add()

            // Swamp Trees
            .entry(Material.MANGROVE_LOG).xp(18, 26).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.MANGROVE_WOOD).xp(18, 26).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.STRIPPED_MANGROVE_LOG).xp(18, 26).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.STRIPPED_MANGROVE_WOOD).xp(18, 26).income(0.16, 0.24).contractPoints(0.04, 0.06).add()

            // Nether "Trees"
            .entry(Material.CRIMSON_STEM).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .entry(Material.CRIMSON_HYPHAE).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .entry(Material.STRIPPED_CRIMSON_STEM).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .entry(Material.STRIPPED_CRIMSON_HYPHAE).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()

            .entry(Material.WARPED_STEM).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .entry(Material.WARPED_HYPHAE).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .entry(Material.STRIPPED_WARPED_STEM).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .entry(Material.STRIPPED_WARPED_HYPHAE).xp(20, 30).income(0.18, 0.26).contractPoints(0.048, 0.072).add()
            .build();

        return createObjective(DefaultGrindTypes.MINING, table);
    }

    private static GrindObjective forLumberjackBlockLoot() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Sticks drop frequently from decaying leaves
            .entry(Material.STICK).xp(2, 4).income(0.02, 0.04).contractPoints(0.004, 0.008).add()

            // Extra saplings caught from decay
            .entry(Material.OAK_SAPLING).xp(12, 18).income(0.16, 0.24).contractPoints(0.032, 0.048).add()
            // (You can map all other saplings to these same values)

            // Apples drop rarely from Oak/Dark Oak
            .entry(Material.APPLE).xp(24, 36).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .build();

        return createObjective(DefaultGrindTypes.BLOCK_LOOT, table);
    }

    private static GrindObjective forLumberjackStripBlock() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.OAK_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.OAK_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.BIRCH_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.BIRCH_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.SPRUCE_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.SPRUCE_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.CHERRY_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.CHERRY_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.PALE_OAK_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.PALE_OAK_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.DARK_OAK_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.DARK_OAK_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.JUNGLE_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.JUNGLE_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.ACACIA_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.ACACIA_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.MANGROVE_LOG).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.MANGROVE_WOOD).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.CRIMSON_STEM).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.CRIMSON_HYPHAE).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.WARPED_STEM).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .entry(Material.WARPED_HYPHAE).xp(6, 10).income(0.06, 0.10).contractPoints(0.016, 0.024).add()
            .build();


        return createObjective(DefaultGrindTypes.STRIP_BLOCK, table);
    }

    private static GrindObjective forLumberjackCraft() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.WOODEN_AXE).xp(1, 2).income(0.01, 0.02).contractPoints(0.001, 0.002).add()
            .entry(Material.STONE_AXE).xp(2, 4).income(0.04, 0.06).contractPoints(0.005, 0.010).add()
            .entry(Material.IRON_AXE).xp(80, 120).income(1.60, 2.40).contractPoints(0.32, 0.48).add()
            .entry(Material.GOLDEN_AXE).xp(40, 60).income(0.80, 1.20).contractPoints(0.16, 0.24).add()
            .entry(Material.DIAMOND_AXE).xp(400, 600).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, table);
    }

    private static GrindObjective forLumberjackSmith() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.NETHERITE_AXE).xp(1200, 1400).income(24.00, 32.00).contractPoints(4.80, 7.20).add()
            .build();

        return createObjective(DefaultGrindTypes.SMITHING, table);
    }

    private static GrindObjective forFarmerBlockBreak() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // High-Volume / Zero-Effort
            .entry(Material.KELP).xp(3, 5).income(0.03, 0.05).contractPoints(0.006, 0.010).add()
            .entry(Material.BAMBOO).xp(3, 5).income(0.03, 0.05).contractPoints(0.006, 0.010).add()
            .entry(Material.SUGAR_CANE).xp(3, 5).income(0.03, 0.05).contractPoints(0.006, 0.010).add()
            .entry(Material.SWEET_BERRIES).xp(3, 5).income(0.03, 0.05).contractPoints(0.006, 0.010).add()
            .entry(Material.GLOW_BERRIES).xp(3, 5).income(0.03, 0.05).contractPoints(0.006, 0.010).add()
            .entry(Material.CACTUS).xp(3, 5).income(0.03, 0.05).contractPoints(0.006, 0.010).add()

            // Standard Crops
            .entry(Material.WHEAT).xp(10, 14).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.POTATO).xp(10, 14).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.CARROT).xp(10, 14).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.BEETROOT).xp(10, 14).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.COCOA_BEANS).xp(10, 14).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.NETHER_WART).xp(10, 14).income(0.12, 0.18).contractPoints(0.024, 0.036).add()
            .entry(Material.MELON_SLICE).xp(8, 12).income(0.10, 0.14).contractPoints(0.02, 0.03).add()

            // Single-Block / Fungi / Flora
            .entry(Material.PUMPKIN).xp(16, 24).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .entry(Material.BROWN_MUSHROOM).xp(16, 24).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .entry(Material.RED_MUSHROOM).xp(16, 24).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .entry(Material.CRIMSON_FUNGUS).xp(16, 24).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .entry(Material.WARPED_FUNGUS).xp(16, 24).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .entry(Material.HONEY_BOTTLE).xp(24, 36).income(0.32, 0.48).contractPoints(0.06, 0.09).add()

            // Botany & Flowers
            .entry(Material.DANDELION).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.POPPY).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.BLUE_ORCHID).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.ALLIUM).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.CORNFLOWER).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.LILY_OF_THE_VALLEY).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.SUNFLOWER).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.LILAC).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.LILY_PAD).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.ROSE_BUSH).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.PEONY).xp(20, 30).income(0.24, 0.36).contractPoints(0.048, 0.072).add()
            .entry(Material.PITCHER_PLANT).xp(32, 48).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(Material.TORCHFLOWER).xp(32, 48).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(Material.OPEN_EYEBLOSSOM).xp(32, 48).income(0.40, 0.60).contractPoints(0.08, 0.12).add()

            // Rare Flora
            .entry(Material.WITHER_ROSE).xp(80, 120).income(1.60, 2.40).contractPoints(0.32, 0.48).add()
            .entry(Material.SPORE_BLOSSOM).xp(80, 120).income(1.60, 2.40).contractPoints(0.32, 0.48).add()
            .build();

        return createObjective(DefaultGrindTypes.BLOCK_LOOT, table);
    }

    private static GrindObjective forFarmerCraft() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Tools
            .entry(Material.WOODEN_HOE).xp(1, 2).income(0.01, 0.02).contractPoints(0.001, 0.002).add()
            .entry(Material.STONE_HOE).xp(2, 4).income(0.04, 0.06).contractPoints(0.005, 0.010).add()
            .entry(Material.IRON_HOE).xp(50, 70).income(1.00, 1.40).contractPoints(0.20, 0.30).add()
            .entry(Material.DIAMOND_HOE).xp(250, 350).income(5.00, 7.00).contractPoints(1.00, 1.40).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, table);
    }

    private static GrindObjective forFarmerSmith() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.NETHERITE_HOE).xp(1000, 1200).income(20.00, 24.00).contractPoints(3.80, 6.20).add()
            .build();

        return createObjective(DefaultGrindTypes.SMITHING, table);
    }

    private static GrindObjective forAnimalKeeperBreed() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Common Livestock
            .entry(EntityType.COW).xp(120, 180).income(2.00, 3.00).contractPoints(0.40, 0.60).add()
            .entry(EntityType.PIG).xp(120, 180).income(2.00, 3.00).contractPoints(0.40, 0.60).add()
            .entry(EntityType.SHEEP).xp(120, 180).income(2.00, 3.00).contractPoints(0.40, 0.60).add()
            .entry(EntityType.CHICKEN).xp(80, 120).income(1.20, 1.80).contractPoints(0.24, 0.36).add()
            .entry(EntityType.RABBIT).xp(120, 180).income(2.00, 3.00).contractPoints(0.40, 0.60).add()

            // Pets & Mounts
            .entry(EntityType.WOLF).xp(160, 240).income(2.80, 4.20).contractPoints(0.56, 0.84).add()
            .entry(EntityType.CAT).xp(160, 240).income(2.80, 4.20).contractPoints(0.56, 0.84).add()
            .entry(EntityType.HORSE).xp(200, 300).income(3.60, 5.40).contractPoints(0.72, 1.08).add()
            .entry(EntityType.DONKEY).xp(200, 300).income(3.60, 5.40).contractPoints(0.72, 1.08).add()
            .entry(EntityType.CAMEL).xp(240, 360).income(4.00, 6.00).contractPoints(0.80, 1.20).add()
            .entry(EntityType.LLAMA).xp(200, 300).income(3.60, 5.40).contractPoints(0.72, 1.08).add()

            // Exotic & Difficult
            .entry(EntityType.TURTLE).xp(400, 600).income(6.40, 9.60).contractPoints(1.20, 1.80).add()
            .entry(EntityType.PANDA).xp(400, 600).income(6.40, 9.60).contractPoints(1.20, 1.80).add()
            .entry(EntityType.SNIFFER).xp(800, 1200).income(12.00, 18.00).contractPoints(2.40, 3.60).add()
            .entry(EntityType.ARMADILLO).xp(320, 480).income(4.80, 7.20).contractPoints(0.96, 1.44).add()
            .entry(EntityType.AXOLOTL).xp(320, 480).income(4.80, 7.20).contractPoints(0.96, 1.44).add()
            .entry(EntityType.BEE).xp(160, 240).income(2.40, 3.60).contractPoints(0.48, 0.72).add()
            .entry(EntityType.STRIDER).xp(200, 300).income(3.60, 5.40).contractPoints(0.72, 1.08).add()
            .entry(EntityType.HOGLIN).xp(240, 360).income(4.00, 6.00).contractPoints(0.80, 1.20).add()
            .entry(EntityType.VILLAGER).xp(400, 600).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .build();

        return createObjective(DefaultGrindTypes.BREEDING, table);
    }

    private static GrindObjective forAnimalKeeperShear() {
        SourceTable shearTable = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(EntityType.SHEEP).xp(24, 36).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(EntityType.MOOSHROOM).xp(80, 120).income(1.60, 2.40).contractPoints(0.32, 0.48).add()
            .entry(EntityType.SNOW_GOLEM).xp(16, 24).income(0.24, 0.36).contractPoints(0.04, 0.06).add()
            .build();

        return createObjective(DefaultGrindTypes.SHEARING, shearTable);
    }

    private static GrindObjective forAnimalKeeperMilk() {
        SourceTable milkTable = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(EntityType.COW).xp(24, 36).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(EntityType.GOAT).xp(24, 36).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(EntityType.MOOSHROOM).xp(32, 48).income(0.64, 0.96).contractPoints(0.12, 0.18).add()
            .build();

        return createObjective(DefaultGrindTypes.MILKING, milkTable);
    }

    private static GrindObjective forAnimalKeeperTame() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Common Pets
            .entry(EntityType.WOLF).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .entry(EntityType.CAT).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()

            // Mounts & Pack Animals
            .entry(EntityType.HORSE).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()
            .entry(EntityType.DONKEY).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()
            .entry(EntityType.MULE).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()
            .entry(EntityType.LLAMA).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()
            .entry(EntityType.TRADER_LLAMA).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()
            .entry(EntityType.CAMEL).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()

            // Rare / Exotic Tames
            .entry(EntityType.PARROT).xp(2000, 3000).income(20.00, 30.00).contractPoints(5.00, 7.50).add()

            // Event-Based Tames
            .entry(EntityType.SKELETON_HORSE).xp(4000, 6000).income(40.00, 60.00).contractPoints(10.00, 15.00).add()
            .build();

        return createObjective(DefaultGrindTypes.TAMING, table);
    }

    private static GrindObjective forAnimalKeeperCraft() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.CARROT_ON_A_STICK).xp(40, 60).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(Material.WARPED_FUNGUS_ON_A_STICK).xp(80, 120).income(0.80, 1.20).contractPoints(0.16, 0.24).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, table);
    }

    private static GrindObjective forFishermanFishItem() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // JUNK
            .entry(Material.STICK).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.STRING).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.BOWL).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.ROTTEN_FLESH).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(Material.BONE).xp(16, 24).income(0.08, 0.12).contractPoints(0.016, 0.024).add()

            // Slightly more useful junk
            .entry(Material.LILY_PAD).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.LEATHER).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.LEATHER_BOOTS).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.BAMBOO).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.INK_SAC).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.TRIPWIRE_HOOK).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.POTION).xp(32, 48).income(0.16, 0.24).contractPoints(0.04, 0.06).add()

            // FISH
            .entry(Material.COD).xp(112, 168).income(0.96, 1.44).contractPoints(0.24, 0.36).add()
            .entry(Material.SALMON).xp(112, 168).income(0.96, 1.44).contractPoints(0.24, 0.36).add()

            // Rarer/more useful fish
            .entry(Material.TROPICAL_FISH).xp(160, 240).income(1.44, 2.16).contractPoints(0.36, 0.54).add()
            .entry(Material.PUFFERFISH).xp(160, 240).income(1.44, 2.16).contractPoints(0.36, 0.54).add()

            // TREASURE
            .entry(Material.BOW).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .entry(Material.FISHING_ROD).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .entry(Material.SADDLE).xp(1000, 1500).income(10.00, 15.00).contractPoints(2.50, 3.75).add()
            .entry(Material.NAME_TAG).xp(1000, 1500).income(10.00, 15.00).contractPoints(2.50, 3.75).add()
            .entry(Material.NAUTILUS_SHELL).xp(1400, 2100).income(14.00, 21.00).contractPoints(3.50, 5.25).add()
            .entry(Material.ENCHANTED_BOOK).xp(1600, 2400).income(16.00, 24.00).contractPoints(4.00, 6.00).add()
            .build();

        return createObjective(DefaultGrindTypes.FISH_ITEM, table);
    }

    private static GrindObjective forFishermanCraft() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.FISHING_ROD).xp(20, 30).income(0.20, 0.30).contractPoints(0.04, 0.06).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, table);
    }

    private static GrindObjective forFishermanKillMob() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(EntityType.COD).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(EntityType.SALMON).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(EntityType.TROPICAL_FISH).xp(48, 72).income(0.48, 0.72).contractPoints(0.12, 0.18).add()
            .entry(EntityType.PUFFERFISH).xp(48, 72).income(0.48, 0.72).contractPoints(0.12, 0.18).add()
            .build();

        return createObjective(DefaultGrindTypes.KILLING, table);
    }

    private static GrindObjective forFishermanMobLoot() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Raw Drops
            .entry(Material.COD).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.SALMON).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.TROPICAL_FISH).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(Material.PUFFERFISH).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()

            // Cooked Drops (Obtained via Fire Aspect kills)
            .entry(Material.COOKED_COD).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(Material.COOKED_SALMON).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .build();

        return createObjective(DefaultGrindTypes.MOB_LOOT, table);
    }

    private static GrindObjective forMonsterHunterKillMob() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Standard Grunts
            .entry(EntityType.ZOMBIE).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.SKELETON).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.SPIDER).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.DROWNED).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.HUSK).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.STRAY).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.BOGGED).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.SILVERFISH).xp(64, 96).income(0.64, 0.96).contractPoints(0.16, 0.24).add()
            .entry(EntityType.ENDERMITE).xp(64, 96).income(0.64, 0.96).contractPoints(0.16, 0.24).add()

            // Dangerous / Nether Grunts
            .entry(EntityType.CREEPER).xp(200, 300).income(2.00, 3.00).contractPoints(0.48, 0.72).add()
            .entry(EntityType.CAVE_SPIDER).xp(200, 300).income(2.00, 3.00).contractPoints(0.48, 0.72).add()
            .entry(EntityType.SLIME).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.MAGMA_CUBE).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.ZOMBIFIED_PIGLIN).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.PIGLIN).xp(200, 300).income(2.00, 3.00).contractPoints(0.48, 0.72).add()
            .entry(EntityType.HOGLIN).xp(240, 360).income(2.40, 3.60).contractPoints(0.56, 0.84).add()
            .entry(EntityType.ZOGLIN).xp(240, 360).income(2.40, 3.60).contractPoints(0.56, 0.84).add()
            .entry(EntityType.PHANTOM).xp(240, 360).income(2.40, 3.60).contractPoints(0.56, 0.84).add()

            // Elite / Hard Hitter
            .entry(EntityType.ENDERMAN).xp(400, 600).income(4.00, 6.00).contractPoints(0.96, 1.44).add()
            .entry(EntityType.BLAZE).xp(400, 600).income(4.00, 6.00).contractPoints(0.96, 1.44).add()
            .entry(EntityType.WITHER_SKELETON).xp(400, 600).income(4.00, 6.00).contractPoints(0.96, 1.44).add()
            .entry(EntityType.GHAST).xp(480, 720).income(4.80, 7.20).contractPoints(1.12, 1.68).add()
            .entry(EntityType.GUARDIAN).xp(400, 600).income(4.00, 6.00).contractPoints(0.96, 1.44).add()
            .entry(EntityType.SHULKER).xp(480, 720).income(4.80, 7.20).contractPoints(1.12, 1.68).add()
            .entry(EntityType.BREEZE).xp(480, 720).income(4.80, 7.20).contractPoints(1.12, 1.68).add()
            .entry(EntityType.CREAKING).xp(480, 720).income(4.80, 7.20).contractPoints(1.12, 1.68).add()

            // Illagers & Minibosses
            .entry(EntityType.PILLAGER).xp(800, 1200).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .entry(EntityType.VINDICATOR).xp(800, 1200).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .entry(EntityType.WITCH).xp(800, 1200).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .entry(EntityType.EVOKER).xp(1200, 1800).income(12.00, 18.00).contractPoints(2.40, 3.60).add()
            .entry(EntityType.ILLUSIONER).xp(1200, 1800).income(12.00, 18.00).contractPoints(2.40, 3.60).add()
            .entry(EntityType.PIGLIN_BRUTE).xp(1200, 1800).income(12.00, 18.00).contractPoints(2.40, 3.60).add()
            .entry(EntityType.RAVAGER).xp(1600, 2400).income(16.00, 24.00).contractPoints(3.20, 4.80).add()
            .entry(EntityType.ELDER_GUARDIAN).xp(4000, 6000).income(40.00, 60.00).contractPoints(8.00, 12.00).add()

            // Bosses
            .entry(EntityType.WARDEN).xp(8000, 12000).income(80.00, 120.00).contractPoints(16.00, 24.00).add()
            .entry(EntityType.WITHER).xp(16000, 24000).income(160.00, 240.00).contractPoints(32.00, 48.00).add()
            .entry(EntityType.ENDER_DRAGON).xp(24000, 36000).income(240.00, 360.00).contractPoints(48.00, 72.00).add()
            .build();

        return createObjective(DefaultGrindTypes.KILLING, table);
    }

    private static GrindObjective forMonsterHunterBreakBlock() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.SPAWNER).xp(400, 600).income(50, 100).contractPoints(2, 4).add()
            .build();

        return createObjective(DefaultGrindTypes.MINING, table);
    }

    private static GrindObjective forMonsterHunterCraft() {
        SourceTable minerCraftingTable = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.WOODEN_SWORD).xp(1, 2).income(0.01, 0.02).contractPoints(0.001, 0.002).add()
            .entry(Material.STONE_SWORD).xp(2, 4).income(0.04, 0.06).contractPoints(0.005, 0.010).add()
            .entry(Material.IRON_SWORD).xp(80, 120).income(1.60, 2.40).contractPoints(0.32, 0.48).add()
            .entry(Material.GOLDEN_SWORD).xp(40, 60).income(0.80, 1.20).contractPoints(0.16, 0.24).add()
            .entry(Material.DIAMOND_SWORD).xp(400, 600).income(8.00, 12.00).contractPoints(1.60, 2.40).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, minerCraftingTable);
    }

    private static GrindObjective forMonsterHunterSmith() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.NETHERITE_SWORD).xp(1200, 1800).income(24.00, 36.00).contractPoints(4.80, 7.20).add()
            .build();

        return createObjective(DefaultGrindTypes.SMITHING, table);
    }

    private static GrindObjective forButcherKillMob() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Tier 1
            .entry(EntityType.PIG).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(EntityType.CHICKEN).xp(64, 96).income(0.64, 0.96).contractPoints(0.16, 0.24).add()
            .entry(EntityType.RABBIT).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(EntityType.SHEEP).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(EntityType.COW).xp(96, 144).income(0.96, 1.44).contractPoints(0.24, 0.36).add()

            // Tier 2
            .entry(EntityType.MOOSHROOM).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add() // Mooshroom
            .entry(EntityType.SQUID).xp(96, 144).income(0.96, 1.44).contractPoints(0.24, 0.36).add()
            .entry(EntityType.GLOW_SQUID).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.BAT).xp(40, 60).income(0.40, 0.60).contractPoints(0.08, 0.12).add()
            .entry(EntityType.FROG).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.TURTLE).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.ARMADILLO).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.FOX).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(EntityType.GOAT).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()

            // Tier 3
            .entry(EntityType.HORSE).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.DONKEY).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.MULE).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.LLAMA).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.TRADER_LLAMA).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.CAMEL).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(EntityType.POLAR_BEAR).xp(200, 300).income(2.00, 3.00).contractPoints(0.48, 0.72).add()
            .entry(EntityType.PANDA).xp(200, 300).income(2.00, 3.00).contractPoints(0.48, 0.72).add()
            .entry(EntityType.DOLPHIN).xp(200, 300).income(2.00, 3.00).contractPoints(0.48, 0.72).add()
            .entry(EntityType.SNIFFER).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()
            .build();

        return createObjective(DefaultGrindTypes.KILLING, table);
    }

    private static GrindObjective forButcherMobLoot() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Raw Meats
            .entry(Material.BEEF).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.PORKCHOP).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.MUTTON).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.CHICKEN).xp(12, 18).income(0.12, 0.18).contractPoints(0.03, 0.05).add()
            .entry(Material.RABBIT).xp(20, 30).income(0.20, 0.30).contractPoints(0.05, 0.07).add()

            // Byproducts
            .entry(Material.LEATHER).xp(24, 36).income(0.24, 0.36).contractPoints(0.06, 0.09).add()
            .entry(Material.FEATHER).xp(8, 12).income(0.08, 0.12).contractPoints(0.02, 0.03).add()
            .entry(Material.RABBIT_HIDE).xp(24, 36).income(0.24, 0.36).contractPoints(0.06, 0.09).add()
            .entry(Material.RABBIT_FOOT).xp(80, 120).income(0.80, 1.20).contractPoints(0.16, 0.24).add() // Rare
            .build();

        return createObjective(DefaultGrindTypes.MOB_LOOT, table);
    }

    private static GrindObjective forButcherCook() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.COOKED_BEEF).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(Material.COOKED_PORKCHOP).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(Material.COOKED_MUTTON).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(Material.COOKED_CHICKEN).xp(24, 36).income(0.24, 0.36).contractPoints(0.06, 0.09).add()
            .entry(Material.COOKED_RABBIT).xp(40, 60).income(0.40, 0.60).contractPoints(0.10, 0.15).add()
            .build();

        return createObjective(DefaultGrindTypes.COOKING, table);
    }

    private static GrindObjective forEnchanterEnchant() {
        SourceTable enchanterTable = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Tier 1
            .entry(Enchantment.BANE_OF_ARTHROPODS).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.SMITE).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.BLAST_PROTECTION).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.PROJECTILE_PROTECTION).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.FIRE_PROTECTION).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.AQUA_AFFINITY).xp(64, 96).income(0.64, 0.96).contractPoints(0.16, 0.24).add()
            .entry(Enchantment.RESPIRATION).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.FEATHER_FALLING).xp(96, 144).income(0.96, 1.44).contractPoints(0.24, 0.36).add()
            .entry(Enchantment.PUNCH).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Enchantment.KNOCKBACK).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()

            // Tier 2
            .entry(Enchantment.PROTECTION).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()
            .entry(Enchantment.SHARPNESS).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()
            .entry(Enchantment.EFFICIENCY).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()
            .entry(Enchantment.UNBREAKING).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()
            .entry(Enchantment.POWER).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()
            .entry(Enchantment.FORTUNE).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()
            .entry(Enchantment.LOOTING).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()
            .entry(Enchantment.SILK_TOUCH).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()
            .entry(Enchantment.INFINITY).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()
            .entry(Enchantment.LUCK_OF_THE_SEA).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()
            .entry(Enchantment.LURE).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add()

            // Tier 3
            .entry(Enchantment.MENDING).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .entry(Enchantment.SWIFT_SNEAK).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .entry(Enchantment.SOUL_SPEED).xp(640, 960).income(6.40, 9.60).contractPoints(1.60, 2.40).add()
            .entry(Enchantment.WIND_BURST).xp(1200, 1800).income(12.00, 18.00).contractPoints(3.00, 4.50).add()
            .entry(Enchantment.BREACH).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .entry(Enchantment.DENSITY).xp(800, 1200).income(8.00, 12.00).contractPoints(2.00, 3.00).add()
            .build();

        return createObjective(DefaultGrindTypes.ENCHANTING, enchanterTable);
    }

    private static GrindObjective forAlchemistBrew() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Basics
            .entry(PotionType.AWKWARD).xp(8, 12).income(0.08, 0.12).contractPoints(0.016, 0.024).add()
            .entry(PotionType.MUNDANE).xp(4, 6).income(0.04, 0.06).contractPoints(0.008, 0.012).add()
            .entry(PotionType.THICK).xp(4, 6).income(0.04, 0.06).contractPoints(0.008, 0.012).add()

            // Tier 1
            .entry(PotionType.NIGHT_VISION).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.INVISIBILITY).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.LEAPING).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.FIRE_RESISTANCE).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.SWIFTNESS).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.SLOWNESS).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.WATER_BREATHING).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.HEALING).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.HARMING).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.POISON).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.REGENERATION).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(PotionType.STRENGTH).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(PotionType.WEAKNESS).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .entry(PotionType.SLOW_FALLING).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()

            // Tier 2
            .entry(PotionType.LONG_NIGHT_VISION).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.LONG_INVISIBILITY).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.STRONG_LEAPING).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.LONG_FIRE_RESISTANCE).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.STRONG_SWIFTNESS).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.STRONG_HEALING).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.STRONG_HARMING).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.STRONG_POISON).xp(240, 360).income(2.40, 3.60).contractPoints(0.64, 0.96).add()
            .entry(PotionType.STRONG_REGENERATION).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()
            .entry(PotionType.STRONG_STRENGTH).xp(320, 480).income(3.20, 4.80).contractPoints(0.80, 1.20).add()

            // Tier 3
            .entry(PotionType.TURTLE_MASTER).xp(480, 720).income(4.80, 7.20).contractPoints(1.20, 1.80).add()
            .entry(PotionType.STRONG_TURTLE_MASTER).xp(640, 960).income(6.40, 9.60).contractPoints(1.60, 2.40).add()
            .entry(PotionType.WIND_CHARGED).xp(400, 600).income(4.00, 6.00).contractPoints(1.00, 1.50).add()
            .entry(PotionType.WEAVING).xp(400, 600).income(4.00, 6.00).contractPoints(1.00, 1.50).add()
            .entry(PotionType.OOZING).xp(400, 600).income(4.00, 6.00).contractPoints(1.00, 1.50).add()
            .entry(PotionType.INFESTED).xp(400, 600).income(4.00, 6.00).contractPoints(1.00, 1.50).add()
            .build();

        return createObjective(DefaultGrindTypes.BREW_POTION, table);
    }

    private static GrindObjective forAlchemistCraft() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.GLASS_BOTTLE).xp(2, 4).income(0.02, 0.04).contractPoints(0.004, 0.006).add() // Per 3 pieces
            .entry(Material.BREWING_STAND).xp(240, 360).income(2.40, 3.60).contractPoints(0.60, 0.90).add() // Requires Blaze Rod
            .entry(Material.CAULDRON).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add() // 7 Iron
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, table);
    }

    private static GrindObjective forBakerCraft() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Tier 1
            .entry(Material.SUGAR).xp(2, 4).income(0.01, 0.03).contractPoints(0.002, 0.006).add()
            .entry(Material.COOKIE).xp(4, 6).income(0.04, 0.06).contractPoints(0.01, 0.015).add() // Per 1 cookie
            .entry(Material.BREAD).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()

            // Tier 2
            .entry(Material.PUMPKIN_PIE).xp(40, 60).income(0.40, 0.60).contractPoints(0.10, 0.15).add()
            .entry(Material.MUSHROOM_STEW).xp(40, 60).income(0.40, 0.60).contractPoints(0.10, 0.15).add()
            .entry(Material.BEETROOT_SOUP).xp(40, 60).income(0.40, 0.60).contractPoints(0.10, 0.15).add()

            // Tier 3
            .entry(Material.CAKE).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add() // Requires 3 milk buckets, eggs, sugar, wheat
            .entry(Material.RABBIT_STEW).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Material.GOLDEN_CARROT).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()

            // Tier 4
            .entry(Material.GOLDEN_APPLE).xp(400, 600).income(4.00, 6.00).contractPoints(1.00, 1.50).add()
            .build();

        return createObjective(DefaultGrindTypes.CRAFTING, table);
    }

    private static GrindObjective forBakerSmelt() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            .entry(Material.BAKED_POTATO).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.POPPED_CHORUS_FRUIT).xp(24, 36).income(0.24, 0.36).contractPoints(0.06, 0.09).add()
            .build();

        return createObjective(DefaultGrindTypes.COOKING, table);
    }

    private static GrindObjective forBakerEat() {
        SourceTable table = SourceTable.builder()
            .scale(GrindObjectiveProperty.XP, 0.1)
            // Light Snacks
            .entry(Material.COOKIE).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.SWEET_BERRIES).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.GLOW_BERRIES).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.MELON_SLICE).xp(16, 24).income(0.16, 0.24).contractPoints(0.04, 0.06).add()
            .entry(Material.APPLE).xp(24, 36).income(0.24, 0.36).contractPoints(0.06, 0.09).add()

            // Staples
            .entry(Material.CAKE).xp(32, 48).income(0.32, 0.48).contractPoints(0.08, 0.12).add()
            .entry(Material.BREAD).xp(40, 60).income(0.40, 0.60).contractPoints(0.10, 0.15).add()
            .entry(Material.BAKED_POTATO).xp(40, 60).income(0.40, 0.60).contractPoints(0.10, 0.15).add()
            .entry(Material.CARROT).xp(24, 36).income(0.24, 0.36).contractPoints(0.06, 0.09).add()

            // Heavy Meals
            .entry(Material.PUMPKIN_PIE).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Material.MUSHROOM_STEW).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Material.BEETROOT_SOUP).xp(80, 120).income(0.80, 1.20).contractPoints(0.20, 0.30).add()
            .entry(Material.RABBIT_STEW).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()

            // Magical / Buff Foods
            .entry(Material.GOLDEN_CARROT).xp(160, 240).income(1.60, 2.40).contractPoints(0.40, 0.60).add()
            .entry(Material.GOLDEN_APPLE).xp(400, 600).income(4.00, 6.00).contractPoints(1.00, 1.50).add()
            .entry(Material.ENCHANTED_GOLDEN_APPLE).xp(4000, 6000).income(40.00, 60.00).contractPoints(10.00, 15.00)
            .add()
            .entry(Material.HONEY_BOTTLE).xp(120, 180).income(1.20, 1.80).contractPoints(0.32, 0.48).add()
            .build();

        return createObjective(DefaultGrindTypes.EATING, table);
    }

    private static <B> GrindObjective createObjective(GrindType<B> grindType, SourceTable table) {
        return new GrindObjective(grindType, null, table);
    }
}
