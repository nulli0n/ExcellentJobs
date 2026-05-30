package su.nightexpress.excellentjobs.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.grind.DefaultGrindObjectives;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobContracts;
import su.nightexpress.excellentjobs.job.model.JobDefinition;
import su.nightexpress.excellentjobs.job.model.JobGrinding;
import su.nightexpress.excellentjobs.job.model.JobLeveling;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class JobDefaults {

    public static final String REWARD_POOL_ID = "basic";

    private JobDefaults() {
    }

    public static List<Job> createDefaultJobs() {
        List<Job> jobs = new ArrayList<>();

        jobs.add(createMinerJob());
        jobs.add(createLumberjackJob());
        jobs.add(createFarmerJob());
        jobs.add(createFisherman());
        jobs.add(createMonsterHunter());
        jobs.add(createButcher());
        jobs.add(createAnimalKeeper());
        jobs.add(createEnchanter());
        jobs.add(createAlchemist());
        jobs.add(createBaker());

        return jobs;
    }

    private static Job createJob(String id, JobDefinition definition, JobGrinding grinding) {
        JobLeveling leveling = new JobLeveling(100, Set.of(REWARD_POOL_ID));
        JobContracts contracts = new JobContracts(true, Set.of(Placeholders.WILDCARD));

        return new Job(id, definition, leveling, grinding, contracts);
    }

    private static String createName(String g1, String g2, String name, Material icon) {
        return TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(icon)) + " " +
            TagWrappers.GRADIENT.with(g1, g2).and(TagWrappers.BOLD).wrap(name.toUpperCase());
    }

    private static List<String> createDescription(String... lines) {
        return Stream.of(lines).map(TagWrappers.GRAY::wrap).toList();
    }

    private static Job createMinerJob() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#f7971e", "#ffd200", "Miner", Material.IRON_PICKAXE))
            .setDescription(createDescription(
                "Dig deep, gather precious resources,",
                "and conquer the underground",
                "for money!"
            ))
            .setIcon(NightItem.fromType(Material.IRON_PICKAXE))
            .setMenuPage(1)
            .setMenuSlots(11)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.YELLOW, Lists.newSet(
            DefaultGrindObjectives.MINER_BLOCK_LOOT,
            DefaultGrindObjectives.MINER_BREAK_BLOCK,
            DefaultGrindObjectives.MINER_CRAFT,
            DefaultGrindObjectives.MINER_SMITH
        ));

        return createJob("miner", definition, grinding);
    }

    private static Job createLumberjackJob() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#f85032", "#e73827", "Lumberjack", Material.IRON_AXE))
            .setDescription(createDescription(
                "Chop down trees and strip logs. ",
                "to make money.",
                "",
                "It is the perfect job for",
                "anyone who loves using",
                "an axe in the forest."
            ))
            .setIcon(NightItem.fromType(Material.IRON_AXE))
            .setMenuPage(1)
            .setMenuSlots(12)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.RED, Lists.newSet(
            DefaultGrindObjectives.LUMBERJACK_BLOCK_LOOT,
            DefaultGrindObjectives.LUMBERJACK_BREAK_BLOCK,
            DefaultGrindObjectives.LUMBERJACK_CRAFT,
            DefaultGrindObjectives.LUMBERJACK_SMITH,
            DefaultGrindObjectives.LUMBERJACK_STRIP_BLOCK
        ));

        return createJob("lumberjack", definition, grinding);
    }


    private static Job createFarmerJob() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#96bf48", "#5e8e3e", "Farmer", Material.IRON_HOE))
            .setDescription(createDescription(
                "Plant, grow, and harvests",
                "all kinds of crops and plant.",
                "",
                "Turn your hard work in the",
                "dirt into easy cash."
            ))
            .setIcon(NightItem.fromType(Material.IRON_HOE))
            .setMenuPage(1)
            .setMenuSlots(14)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.GREEN, Lists.newSet(
            DefaultGrindObjectives.FARMER_BREAK_BLOCK,
            DefaultGrindObjectives.FARMER_CRAFT,
            DefaultGrindObjectives.FARMER_SMITH
        ));

        return createJob("farmer", definition, grinding);
    }


    private static Job createFisherman() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#2193b0", "#6dd5ed", "Fisherman", Material.FISHING_ROD))
            .setDescription(createDescription(
                "Catch fish with a rod or hunt",
                "them down in rivers and oceans.",
                "",
                "Make a living by reeling in rare",
                "catches and looting fish mobs."
            ))
            .setIcon(NightItem.fromType(Material.FISHING_ROD))
            .setMenuPage(1)
            .setMenuSlots(13)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.BLUE, Lists.newSet(
            DefaultGrindObjectives.FISHERMAN_FISH_ITEM,
            DefaultGrindObjectives.FISHERMAN_KILL_MOB,
            DefaultGrindObjectives.FISHERMAN_MOB_LOOT,
            DefaultGrindObjectives.FISHERMAN_CRAFT
        ));

        return createJob("fisherman", definition, grinding);
    }


    private static Job createMonsterHunter() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#ffecd2", "#fcb69f", "Monster Hunter", Material.IRON_SWORD))
            .setDescription(createDescription(
                "Fight the monsters that come",
                "out at night and in caves.",
                "",
                "Get paid for every hostile mob you",
                "defeat to keep the world safe."
            ))
            .setIcon(NightItem.fromType(Material.IRON_SWORD))
            .setMenuPage(1)
            .setMenuSlots(23)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.WHITE, Lists.newSet(
            DefaultGrindObjectives.MONSTER_HUNTER_BREAK_BLOCK,
            DefaultGrindObjectives.MONSTER_HUNTER_CRAFT,
            DefaultGrindObjectives.MONSTER_HUNTER_MOB_KILL,
            DefaultGrindObjectives.MONSTER_HUNTER_SMITH
        ));

        return createJob("monster_hunter", definition, grinding);
    }

    private static Job createButcher() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#ff416c", "#ff4b2b", "Butcher", Material.BEEF))
            .setDescription(createDescription(
                "Kill farm animals and cook",
                "their meat to earn money.",
                "",
                "Keep the server fed while making",
                "a nice profit."
            ))
            .setIcon(NightItem.fromType(Material.BEEF))
            .setMenuPage(1)
            .setMenuSlots(21)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.RED, Lists.newSet(
            DefaultGrindObjectives.BUTCHER_KILL_MOB,
            DefaultGrindObjectives.BUTCHER_MOB_LOOT,
            DefaultGrindObjectives.BUTCHER_COOK
        ));

        return createJob("butcher", definition, grinding);
    }

    private static Job createAnimalKeeper() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#f77062", "#fe5196", "Animal Keeper", Material.LEAD))
            .setDescription(createDescription(
                "Earn money by taking care",
                "of animals.",
                "",
                "Get paid for breeding, taming,",
                "milking, and shearing them."
            ))
            .setIcon(NightItem.fromType(Material.LEAD))
            .setMenuPage(1)
            .setMenuSlots(22)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.PINK, Lists.newSet(
            DefaultGrindObjectives.ANIMAL_KEEP_BREED,
            DefaultGrindObjectives.ANIMAL_KEEP_CRAFT,
            DefaultGrindObjectives.ANIMAL_KEEP_MILK,
            DefaultGrindObjectives.ANIMAL_KEEP_SHEAR,
            DefaultGrindObjectives.ANIMAL_KEEP_TAME
        ));

        return createJob("animal_keeper", definition, grinding);
    }

    private static Job createEnchanter() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#b224ef", "#7579ff", "Enchanter", Material.LAPIS_LAZULI))
            .setDescription(createDescription(
                "Use magic to put powerful",
                "enchantments on tools, weapons,",
                "and armor.",
                "",
                "Turn basic gear into valuable items."
            ))
            .setIcon(NightItem.fromType(Material.ENCHANTING_TABLE))
            .setMenuPage(1)
            .setMenuSlots(30)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.PURPLE, Lists.newSet(
            DefaultGrindObjectives.ENCHANTER_ENCHANT
        ));

        return createJob("enchanter", definition, grinding);
    }

    private static Job createAlchemist() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#fc4a1a", "#f7b733", "Alchemist", Material.BLAZE_ROD))
            .setDescription(createDescription(
                "Mix ingredients in a brewing",
                "stand to make potions.",
                "",
                "Turn your magical drinks into a",
                "great way to make money."
            ))
            .setIcon(NightItem.fromType(Material.BREWING_STAND))
            .setMenuPage(1)
            .setMenuSlots(32)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.YELLOW, Lists.newSet(
            DefaultGrindObjectives.ALCHEMIST_BREW,
            DefaultGrindObjectives.ALCHEMIST_CRAFT
        ));

        return createJob("alchemist", definition, grinding);
    }

    private static Job createBaker() {
        JobDefinition definition = JobDefinition.builder()
            .setName(createName("#ffeaa7", "#fdcb6e", "Baker", Material.BREAD))
            .setDescription(createDescription(
                "Make money by cooking and eating",
                "different kinds of food.",
                "",
                "Turn your love for baking into a",
                "tasty way to get rich."
            ))
            .setIcon(NightItem.fromType(Material.BREAD))
            .setMenuPage(1)
            .setMenuSlots(15)
            .build();

        JobGrinding grinding = new JobGrinding(NightBarColor.GREEN, Lists.newSet(
            DefaultGrindObjectives.BAKER_CRAFT,
            DefaultGrindObjectives.BAKER_EAT,
            DefaultGrindObjectives.BAKER_SMELT
        ));

        return createJob("baker", definition, grinding);
    }
}
