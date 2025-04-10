package content.global.skill.slayer

import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.NPCs
import java.util.*

/**
 * Represents a Slayer Master who assigns Slayer tasks.
 *
 * Each Slayer Master has specific:
 * - NPC ID
 * - Required combat level
 * - Additional requirements (e.g., Slayer level or quest completion)
 * - Range of task amounts they can assign
 * - Task points awarded based on streaks
 * - A unique list of tasks they can assign
 *
 * @property npc The npc id of the Slayer Master.
 * @property requiredCombat The minimum combat level required to receive tasks.
 * @property requirements Additional requirement level (e.g., Slayer level or quest req).
 * @property assignmentCount The possible min and max range of tasks assigned.
 * @property taskPoints Points awarded per task streak milestone.
 * @property tasks List of possible Slayer tasks assigned by the master.
 */
enum class SlayerMaster(
    var npc: Int,
    var requiredCombat: Int,
    var requirements: Int,
    var assignmentCount: IntArray,
    var taskPoints: IntArray,
    vararg tasks: Task,
) {

    /**
     * Turael - beginner Slayer Master with no requirements.
     */
    TURAEL(
        NPCs.TURAEL_8273,
        0,
        0,
        intArrayOf(15, 50),
        intArrayOf(0, 0, 0),
        Task(Tasks.BANSHEE, 8),
        Task(Tasks.BATS, 7),
        Task(Tasks.BIRDS, 6),
        Task(Tasks.BEARS, 7),
        Task(Tasks.CAVE_BUG, 8),
        Task(Tasks.CAVE_CRAWLERS, 8),
        Task(Tasks.CAVE_SLIMES, 8),
        Task(Tasks.COWS, 8),
        Task(Tasks.CRAWLING_HAND, 8),
        Task(Tasks.DOG, 7),
        Task(Tasks.DWARF, 7),
        Task(Tasks.GHOSTS, 7),
        Task(Tasks.GOBLINS, 7),
        Task(Tasks.ICE_FIENDS, 8),
        Task(Tasks.KALPHITES, 6),
        Task(Tasks.DESERT_LIZARDS, 8),
        Task(Tasks.MINOTAURS, 7),
        Task(Tasks.MONKEYS, 6),
        Task(Tasks.RATS, 7),
        Task(Tasks.SCORPIONS, 7),
        Task(Tasks.SKELETONS, 7),
        Task(Tasks.SPIDERS, 6),
        Task(Tasks.WOLVES, 7),
        Task(Tasks.ZOMBIES, 7),
    ),

    /**
     * Mazchna - requires 20 Combat.
     */
    MAZCHNA(
        NPCs.MAZCHNA_8274,
        20,
        0,
        intArrayOf(30, 70),
        intArrayOf(2, 5, 15),
        Task(Tasks.BANSHEE, 8),
        Task(Tasks.BATS, 7),
        Task(Tasks.BEARS, 6),
        Task(Tasks.CATABLEPONS, 8),
        Task(Tasks.CAVE_BUG, 8),
        Task(Tasks.CAVE_CRAWLERS, 8),
        Task(Tasks.CAVE_SLIMES, 8),
        Task(Tasks.COCKATRICES, 8),
        Task(Tasks.CRAWLING_HAND, 8),
        Task(Tasks.DESERT_LIZARDS, 8),
        Task(Tasks.DOG, 7),
        Task(Tasks.EARTH_WARRIORS, 6),
        Task(Tasks.FLESH_CRAWLERS, 7),
        Task(Tasks.GHOSTS, 7),
        Task(Tasks.GHOULS, 7),
        Task(Tasks.HILL_GIANTS, 7),
        Task(Tasks.HOBGOBLINS, 7),
        Task(Tasks.ICE_WARRIOR, 7),
        Task(Tasks.KALPHITES, 6),
        Task(Tasks.MOGRES, 8),
        Task(Tasks.PYREFIENDS, 8),
        Task(Tasks.ROCK_SLUGS, 8),
        Task(Tasks.SCORPIONS, 7),
        Task(Tasks.SHADES, 8),
        Task(Tasks.SKELETONS, 7),
        Task(Tasks.VAMPIRES, 6),
        Task(Tasks.WOLVES, 7),
        Task(Tasks.ZOMBIES, 7)
    ),

    /**
     * Vannaka - mid-tier Slayer Master requiring 40 Combat.
     */
    VANNAKA(
        NPCs.VANNAKA_1597,
        40,
        0,
        intArrayOf(30, 80),
        intArrayOf(4, 20, 60),
        Task(Tasks.ABERRANT_SPECTRES, 8),
        Task(Tasks.ABYSSAL_DEMONS, 5),
        Task(Tasks.ANKOU, 7),
        Task(Tasks.BANSHEE, 6),
        Task(Tasks.BASILISKS, 8),
        Task(Tasks.BLOODVELDS, 8),
        Task(Tasks.BLUE_DRAGONS, 7),
        Task(Tasks.BRINE_RATS, 7),
        Task(Tasks.BRONZE_DRAGONS, 7),
        Task(Tasks.CAVE_BUG, 7),
        Task(Tasks.CAVE_CRAWLERS, 7),
        Task(Tasks.CAVE_SLIMES, 7),
        Task(Tasks.COCKATRICES, 8),
        Task(Tasks.CRAWLING_HAND, 6),
        Task(Tasks.CROCODILES, 6),
        Task(Tasks.DAGANNOTHS, 7),
        Task(Tasks.DUST_DEVILS, 8),
        Task(Tasks.EARTH_WARRIORS, 6),
        Task(Tasks.ELVES, 7),
        Task(Tasks.FROGS, 8),
        Task(Tasks.FIRE_GIANTS, 7),
        Task(Tasks.GARGOYLES, 5),
        Task(Tasks.GHOULS, 7),
        Task(Tasks.GREEN_DRAGONS, 6),
        Task(Tasks.HARPIE_BUG_SWARMS, 8),
        Task(Tasks.HELLHOUNDS, 7),
        Task(Tasks.HILL_GIANTS, 7),
        Task(Tasks.HOBGOBLINS, 7),
        Task(Tasks.ICE_GIANTS, 7),
        Task(Tasks.ICE_WARRIOR, 7),
        Task(Tasks.INFERNAL_MAGES, 8),
        Task(Tasks.JELLIES, 8),
        Task(Tasks.JUNGLE_HORRORS, 8),
        Task(Tasks.KALPHITES, 7),
        Task(Tasks.KURASKS, 7),
        Task(Tasks.DESERT_LIZARDS, 7),
        Task(Tasks.LESSER_DEMONS, 7),
        Task(Tasks.MOGRES, 7),
        Task(Tasks.MOSS_GIANTS, 7),
        Task(Tasks.NECHRYAELS, 5),
        Task(Tasks.OGRES, 7),
        Task(Tasks.OTHERWORDLY_BEING, 8),
        Task(Tasks.PYREFIENDS, 8),
        Task(Tasks.ROCK_SLUGS, 7),
        Task(Tasks.SHADES, 8),
        Task(Tasks.SPIRTUAL_MAGES, 3),
        Task(Tasks.SPIRTUAL_RANGERS, 3),
        Task(Tasks.SPIRTUAL_WARRIORS, 3),
        Task(Tasks.TROLLS, 7),
        Task(Tasks.TUROTHS, 8),
        Task(Tasks.VAMPIRES, 7),
        Task(Tasks.WEREWOLFS, 7),
    ),

    /**
     * Chaeldar - requires 70 Combat, gives higher tier tasks.
     */
    CHAELDAR(
        NPCs.CHAELDAR_1598,
        70,
        0,
        intArrayOf(110, 170),
        intArrayOf(10, 50, 150),
        Task(Tasks.ABERRANT_SPECTRES, 8),
        Task(Tasks.ABYSSAL_DEMONS, 12),
        Task(Tasks.AVIANSIES, 9),
        Task(Tasks.BANSHEE, 5),
        Task(Tasks.BASILISKS, 7),
        Task(Tasks.BLACK_DEMONS, 10),
        Task(Tasks.BLOODVELDS, 8),
        Task(Tasks.BLUE_DRAGONS, 8),
        Task(Tasks.BRINE_RATS, 7),
        Task(Tasks.BRONZE_DRAGONS, 11),
        Task(Tasks.CAVE_CRAWLERS, 5),
        Task(Tasks.CAVE_HORRORS, 10),
        Task(Tasks.CAVE_SLIMES, 6),
        Task(Tasks.COCKATRICES, 6),
        Task(Tasks.DAGANNOTHS, 11),
        Task(Tasks.DUST_DEVILS, 9),
        Task(Tasks.ELVES, 8),
        Task(Tasks.FIRE_GIANTS, 12),
        Task(Tasks.GARGOYLES, 11),
        Task(Tasks.GREATER_DEMONS, 9),
        Task(Tasks.HARPIE_BUG_SWARMS, 6),
        Task(Tasks.HELLHOUNDS, 9),
        Task(Tasks.INFERNAL_MAGES, 7),
        Task(Tasks.IRON_DRAGONS, 12),
        Task(Tasks.JELLIES, 10),
        Task(Tasks.JUNGLE_HORRORS, 10),
        Task(Tasks.KALPHITES, 11),
        Task(Tasks.KURASKS, 12),
        Task(Tasks.LESSER_DEMONS, 9),
        Task(Tasks.DESERT_LIZARDS, 5),
        Task(Tasks.NECHRYAELS, 12),
        Task(Tasks.PYREFIENDS, 6),
        Task(Tasks.ROCK_SLUGS, 5),
        Task(Tasks.SKELETAL_WYVERN, 7),
        Task(Tasks.SPIRTUAL_WARRIORS, 4),
        Task(Tasks.SPIRTUAL_RANGERS, 4),
        Task(Tasks.SPIRTUAL_MAGES, 4),
        Task(Tasks.STEEL_DRAGONS, 9),
        Task(Tasks.TROLLS, 11),
        Task(Tasks.TUROTHS, 10),
        Task(Tasks.ZYGOMITES, 7),
    ),

    /**
     * Sumona - requires 90 Combat and 35 Slayer.
     */
    SUMONA(
        NPCs.SUMONA_7780,
        90,
        35,
        intArrayOf(50, 185),
        intArrayOf(12, 60, 180),
        Task(Tasks.ABERRANT_SPECTRES, 15),
        Task(Tasks.ABYSSAL_DEMONS, 10),
        Task(Tasks.AVIANSIES, 7),
        Task(Tasks.BANSHEE, 15),
        Task(Tasks.BASILISKS, 15),
        Task(Tasks.BLACK_DEMONS, 10),
        Task(Tasks.BLOODVELDS, 10),
        Task(Tasks.BLUE_DRAGONS, 5),
        Task(Tasks.CAVE_CRAWLERS, 15),
        Task(Tasks.CAVE_HORRORS, 15),
        Task(Tasks.CROCODILES, 4),
        Task(Tasks.DAGANNOTHS, 10),
        Task(Tasks.DESERT_LIZARDS, 4),
        Task(Tasks.DUST_DEVILS, 15),
        Task(Tasks.ELVES, 10),
        Task(Tasks.FIRE_GIANTS, 10),
        Task(Tasks.GARGOYLES, 10),
        Task(Tasks.GREATER_DEMONS, 10),
        Task(Tasks.HELLHOUNDS, 10),
        Task(Tasks.IRON_DRAGONS, 7),
        Task(Tasks.KALPHITES, 10),
        Task(Tasks.KURASKS, 15),
        Task(Tasks.NECHRYAELS, 10),
        Task(Tasks.SCORPIONS, 4),
        Task(Tasks.SPIRTUAL_MAGES, 10),
        Task(Tasks.SPIRTUAL_WARRIORS, 10),
        Task(Tasks.TROLLS, 10),
        Task(Tasks.TUROTHS, 15),
        Task(Tasks.VAMPIRES, 10),
    ),

    /**
     * Duradel - high-level Slayer Master requiring 100 Combat and 50 Slayer.
     */
    DURADEL(
        NPCs.DURADEL_8275,
        100,
        50,
        intArrayOf(50, 199),
        intArrayOf(15, 75, 225),
        Task(Tasks.ABERRANT_SPECTRES, 7),
        Task(Tasks.ABYSSAL_DEMONS, 12),
        Task(Tasks.ANKOU, 5),
        Task(Tasks.AVIANSIES, 8),
        Task(Tasks.BLACK_DEMONS, 8),
        Task(Tasks.BLACK_DRAGONS, 9),
        Task(Tasks.BLOODVELDS, 8),
        Task(Tasks.BLUE_DRAGONS, 4),
        Task(Tasks.CAVE_HORRORS, 4),
        Task(Tasks.DAGANNOTHS, 9),
        Task(Tasks.DARK_BEASTS, 11),
        Task(Tasks.DUST_DEVILS, 5),
        Task(Tasks.ELVES, 4),
        Task(Tasks.FIRE_GIANTS, 7),
        Task(Tasks.GARGOYLES, 8),
        Task(Tasks.GREATER_DEMONS, 9),
        Task(Tasks.HELLHOUNDS, 10),
        Task(Tasks.IRON_DRAGONS, 5),
        Task(Tasks.KALPHITES, 9),
        Task(Tasks.KURASKS, 4),
        Task(Tasks.MITHRIL_DRAGONS, 9),
        Task(Tasks.NECHRYAELS, 9),
        Task(Tasks.SKELETAL_WYVERN, 7),
        Task(Tasks.SPIRTUAL_MAGES, 2),
        Task(Tasks.SPIRTUAL_RANGERS, 2),
        Task(Tasks.SPIRTUAL_WARRIORS, 2),
        Task(Tasks.STEEL_DRAGONS, 7),
        Task(Tasks.SUQAHS, 8),
        Task(Tasks.TROLLS, 6),
        Task(Tasks.TZHAAR, 10),
        Task(Tasks.VAMPIRES, 8),
        Task(Tasks.WATERFIENDS, 2),
    ),
    ;

    var tasks: List<Task> = ArrayList(arrayListOf(*tasks))

    /**
     * Checks if the player meets the combat and slayer level requirements to receive a task from this Slayer Master.
     *
     * @param player The player to check against.
     * @return `True` if the player meets the requirements; `false` otherwise.
     */
    fun hasRequirements(player: Player): Boolean =
        player.properties.currentCombatLevel >= this.requiredCombat &&
            player.getSkills().getLevel(Skills.SLAYER) >= this.requirements

    /**
     * Represents a slayer task assigned by a Slayer Master.
     *
     * @property task The specific task assigned.
     * @property weight The weighting used in task assignment randomness.
     */
    class Task internal constructor(
        var task: Tasks,
        var weight: Int,
    )

    companion object {
        /**
         * A map used to get a [SlayerMaster] based on their npc id.
         */
        private val idMap = HashMap<Int, SlayerMaster>()

        init {
            Arrays.stream(values()).forEach { m: SlayerMaster ->
                idMap.putIfAbsent(m.npc, m)
            }
        }

        /**
         * Retrieves a [SlayerMaster] instance by its npc id.
         *
         * @param id The npc id of the Slayer Master.
         * @return The corresponding SlayerMaster instance.
         * @throws KotlinNullPointerException If no SlayerMaster exists for the given id.
         */
        @JvmStatic
        fun forId(id: Int): SlayerMaster = idMap[id]!!

        /**
         * Checks if the player currently has a task assigned by the given Slayer Master.
         *
         * @param master The Slayer Master to compare against.
         * @param player The player whose current task is being checked.
         * @return `True` if the task matches any task from the master; `false` otherwise.
         */
        @JvmStatic
        fun hasSameTask(
            master: SlayerMaster,
            player: Player,
        ): Boolean =
            master.tasks
                .stream()
                .filter { task: Task ->
                    task.task == SlayerManager.getInstance(player).task
                }.count() != 0L
    }
}
