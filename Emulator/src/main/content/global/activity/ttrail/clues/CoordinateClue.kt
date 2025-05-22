package content.global.activity.ttrail.clues

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.scrolls.CoordinateClueScroll
import core.game.world.map.Location
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Represents the coordinate clues.
 */
class CoordinateClue
/**
 * Instantiates a new Coordinate clue plugin.
 *
 * @param name     the name
 * @param clueId   the clue id
 * @param level    the level
 * @param location the location
 * @param clue     the clue
 */
@JvmOverloads constructor(
    name: String? = null,
    clueId: Int = -1,
    level: ClueLevel? = null,
    location: Location? = null,
    clue: String? = null
) : CoordinateClueScroll(name, clueId, level, location, clue) {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(CoordinateClue("tree-gnome-coord", Items.CLUE_SCROLL_2807, ClueLevel.MEDIUM, Location(2478, 3158, 0), "00 degrees 05 minutes south<br>01 degrees 13 minutes east"))
        register(CoordinateClue("karamja-coord", Items.CLUE_SCROLL_2809, ClueLevel.MEDIUM, Location(2888, 3153, 0), "00 degrees 13 minutes south<br>13 degrees 58 minutes east"))
        register(CoordinateClue("brimhaven-coord", Items.CLUE_SCROLL_2811, ClueLevel.MEDIUM, Location(2743, 3151, 0), "00 degrees 18 minutes south<br>09 degrees 28 minutes east"))
        register(CoordinateClue("lumbridge-swamp-coord", Items.CLUE_SCROLL_2813, ClueLevel.MEDIUM, Location(3183, 3151, 0), "00 degrees 20 minutes south<br>23 degrees 15 minutes east"))
        register(CoordinateClue("zanaris-shed-coord", Items.CLUE_SCROLL_2815, ClueLevel.MEDIUM, Location(3217, 3178, 0), "00 degrees 30 minutes north<br>24 degrees 16 minutes east"))
        register(CoordinateClue("port-sarim-coord", Items.CLUE_SCROLL_2817, ClueLevel.MEDIUM, Location(3007, 3145, 0), "00 degrees 31 minutes south<br>17 degrees 43 minutes east"))
        register(CoordinateClue("musa-point-coord", Items.CLUE_SCROLL_2819, ClueLevel.MEDIUM, Location(2896, 3119, 0), "01 degrees 18 minutes south<br>14 degrees 15 minutes east"))
        register(CoordinateClue("moss-giant-coord", Items.CLUE_SCROLL_2821, ClueLevel.MEDIUM, Location(2696, 3206, 0), "01 degrees 26 minutes north<br>08 degrees 01 minutes east"))
        register(CoordinateClue("hazelmere-coord", Items.CLUE_SCROLL_2823, ClueLevel.MEDIUM, Location(2680, 3111, 0), "01 degrees 35 minutes south<br>07 degrees 28 minutes east"))
        register(CoordinateClue("uzer-coord", Items.CLUE_SCROLL_2825, ClueLevel.MEDIUM, Location(3510, 3075, 0), "02 degrees 43 minutes south<br>33 degrees 26 minutes east"))
        register(CoordinateClue("ham-coord", Items.CLUE_SCROLL_2827, ClueLevel.MEDIUM, Location(3161, 3251, 0), "02 degrees 48 minutes north<br>22 degrees 30 minutes east"))
        register(CoordinateClue("ardy-zoo-coord", Items.CLUE_SCROLL_2829, ClueLevel.MEDIUM, Location(2644, 3251, 0), "02 degrees 50 minutes north<br>06 degrees 20 minutes east"))
        register(CoordinateClue("nature-altar-coord", Items.CLUE_SCROLL_2831, ClueLevel.HARD, Location(2874, 3047, 0), "03 degrees 35 minutes south<br>13 degrees 35 minutes east"))
        register(CoordinateClue("shilo-mine-coord", Items.CLUE_SCROLL_2833, ClueLevel.MEDIUM, Location(2849, 3033, 0), "04 degrees 00 minutes south<br>12 degrees 46 minutes east"))
        register(CoordinateClue("bedapin-camp-coord", Items.CLUE_SCROLL_2835, ClueLevel.HARD, Location(3168, 3041, 0), "03 degrees 45 minutes south<br>22 degrees 45 minutes east"))
        register(CoordinateClue("crandor-coord", Items.CLUE_SCROLL_2839, ClueLevel.MEDIUM, Location(2848, 3297, 0), "04 degrees 13 minutes north<br>12 degrees 45 minutes east"))
        register(CoordinateClue("champions-guild-coord", Items.CLUE_SCROLL_2841, ClueLevel.MEDIUM, Location(3179, 3343, 0), "05 degrees 43 minutes north<br>23 degrees 05 minutes east"))
        register(CoordinateClue("cairn-island-coord", Items.CLUE_SCROLL_2843, ClueLevel.HARD, Location(2764, 2974, 0), "05 degrees 50 minutes south<br>10 degrees 05 minutes east"))
        register(CoordinateClue("bandit-camp-coord", Items.CLUE_SCROLL_2845, ClueLevel.HARD, Location(3139, 2969, 0), "06 degrees 00 minutes south<br>21 degrees 48 minutes east"))
        register(CoordinateClue("three-ponds-coord", Items.CLUE_SCROLL_2849, ClueLevel.MEDIUM, Location(2383, 3369, 0), "06 degrees 31 minutes north<br>01 degrees 46 minutes west"))
        register(CoordinateClue("draynor-manor-coord", Items.CLUE_SCROLL_2851, ClueLevel.MEDIUM, Location(3120, 3384, 0), "06 degrees 58 minutes north<br>21 degrees 16 minutes east"))
        register(CoordinateClue("nature-spirit-coord", Items.CLUE_SCROLL_2853, ClueLevel.MEDIUM, Location(3430, 3388, 0), "07 degrees 05 minutes north<br>30 degrees 56 minutes east"))
        register(CoordinateClue("southeast-taverly-coord", Items.CLUE_SCROLL_2855, ClueLevel.MEDIUM, Location(2920, 3404, 0), "07 degrees 33 minutes north<br>15 degrees 00 minutes east"))
        register(CoordinateClue("swamp-coord", Items.CLUE_SCROLL_2856, ClueLevel.HARD, Location(3441, 3419, 0), "08 degrees 03 minutes north<br>31 degrees 16 minutes east"))
        register(CoordinateClue("feldip-coord", Items.CLUE_SCROLL_2857, ClueLevel.MEDIUM, Location(2593, 2899, 0), "08 degrees 11 minutes south<br>4 degrees 48 minutes east"))
        register(CoordinateClue("gnome-terribirds-coord", Items.CLUE_SCROLL_2858, ClueLevel.MEDIUM, Location(2387, 3435, 0), "08 degrees 33 minutes north<br>01 degrees 39 minutes west"))
        register(CoordinateClue("north-gnome-terribirds-coord", Items.CLUE_SCROLL_3490, ClueLevel.MEDIUM, Location(2382, 3467, 0), "09 degrees 35 minutes north<br>01 degrees 50 minutes west"))
        register(CoordinateClue("ice-mountain-coord", Items.CLUE_SCROLL_3491, ClueLevel.MEDIUM, Location(3006, 3475, 0), "09 degrees 48 minutes north<br>17 degrees 39 minutes east"))
        register(CoordinateClue("coal-line-coord", Items.CLUE_SCROLL_3492, ClueLevel.MEDIUM, Location(2585, 3505, 0), "10 degrees 45 minutes north<br>04 degrees 31 minutes east"))
        register(CoordinateClue("slayer-tower-coord", Items.CLUE_SCROLL_3493, ClueLevel.MEDIUM, Location(3442, 3515, 0), "11 degrees 03 minutes north<br>31 degrees 20 minutes east"))
        register(CoordinateClue("gnome-stronghold-coord", Items.CLUE_SCROLL_3494, ClueLevel.MEDIUM, Location(2416, 3515, 0), "11 degrees 05 minutes north<br>00 degrees 45 minutes west"))
        register(CoordinateClue("eagles-peak-coord", Items.CLUE_SCROLL_3495, ClueLevel.MEDIUM, Location(2362, 3531, 0), "11 degrees 33 minutes north<br>02 degrees 24 minutes west"))
        register(CoordinateClue("burthorpe-coord", Items.CLUE_SCROLL_3496, ClueLevel.MEDIUM, Location(2920, 3534, 0), "11 degrees 41 minutes north<br>14 degrees 58 minutes east"))
        register(CoordinateClue("fenkenstrain-castle-coord", Items.CLUE_SCROLL_3497, ClueLevel.MEDIUM, Location(3548, 3559, 0), "12 degrees 28 minutes north<br>34 degrees 37 minutes east"))
        register(CoordinateClue("lvl-11-wildy-coord", Items.CLUE_SCROLL_3498, ClueLevel.HARD, Location(3113, 3602, 0), "13 degrees 46 minutes north<br>21 degrees 01 minutes east"))
        register(CoordinateClue("swaying-tree-coord", Items.CLUE_SCROLL_3499, ClueLevel.MEDIUM, Location(2735, 3639, 0), "14 degrees 54 minutes north<br>9 degrees 13 minutes east"))
        register(CoordinateClue("rellekka-coord", Items.CLUE_SCROLL_3500, ClueLevel.MEDIUM, Location(2680, 3652, 0), "15 degrees 22 minutes north<br>07 degrees 31 minutes east"))
        register(CoordinateClue("trollheim-coord", Items.CLUE_SCROLL_3501, ClueLevel.HARD, Location(2892, 3674, 0), "16 degrees 03 minutes north<br>14 degrees 07 minutes east"))
        register(CoordinateClue("graveyard-wildy-coord", Items.CLUE_SCROLL_3502, ClueLevel.HARD, Location(3229, 3687, 0), "16 degrees 07 minutes north<br>22 degrees 45 minutes east"))
        register(CoordinateClue("troll-stronghold-coord", Items.CLUE_SCROLL_3503, ClueLevel.HARD, Location(2853, 3689, 0), "16 degrees 31 minutes north<br>12 degrees 54 minutes east"))
        register(CoordinateClue("level-22-wildy-coord", Items.CLUE_SCROLL_3504, ClueLevel.HARD, Location(3304, 3692, 0), "16 degrees 35 minutes north<br>27 degrees 01 minutes east"))
        register(CoordinateClue("bandit-camp-coord", Items.CLUE_SCROLL_3505, ClueLevel.HARD, Location(3055, 3696, 0), "16 degrees 43 minutes north<br>19 degrees 13 minutes east"))
        register(CoordinateClue("level-23-wildy-coord", Items.CLUE_SCROLL_3506, ClueLevel.HARD, Location(3302, 3696, 0), "16 degrees 43 minutes north<br>26 degrees 56 minutes east"))
        register(CoordinateClue("northeast-rellekka-coord", Items.CLUE_SCROLL_3507, ClueLevel.HARD, Location(2711, 3732, 0), "17 degrees 50 minutes north<br>08 degrees 30 minutes east"))
        register(CoordinateClue("level-29-wildy-coord", Items.CLUE_SCROLL_3508, ClueLevel.HARD, Location(2970, 3749, 0), "18 degrees 22 minutes north<br>16 degrees 33 minutes east"))
        register(CoordinateClue("level-31-wildy-coord", Items.CLUE_SCROLL_3509, ClueLevel.HARD, Location(3034, 3804, 0), "18 degrees 50 minutes north<br>20 degrees 26 minutes east"))
        register(CoordinateClue("level-35-wildy-coord", Items.CLUE_SCROLL_3510, ClueLevel.HARD, Location(3244, 3792, 0), "19 degrees 43 minutes north<br>25 degrees 07 minutes east"))
        register(CoordinateClue("level-36-wildy-coord", Items.CLUE_SCROLL_3512, ClueLevel.HARD, Location(3139, 3803, 0), "20 degrees 05 minutes north<br>21 degrees 52 minutes east"))
        register(CoordinateClue("kbd-wildy-coord", Items.CLUE_SCROLL_3513, ClueLevel.HARD, Location(3014, 3847, 0), "21 degrees 24 minutes north<br>17 degrees 54 minutes east"))
        register(CoordinateClue("chaos-temple-wildy-coord", Items.CLUE_SCROLL_3514, ClueLevel.HARD, Location(2946, 3818, 0), "20 degrees 33 minutes north<br>15 degrees 48 minutes east"))
        register(CoordinateClue("rune-rocks-wildy-coord", Items.CLUE_SCROLL_3515, ClueLevel.HARD, Location(3059, 3884, 0), "22 degrees 35 minutes north<br>19 degrees 18 minutes east"))
        register(CoordinateClue("demonic-ruins-wildy-coord", Items.CLUE_SCROLL_3516, ClueLevel.HARD, Location(3290, 3889, 0), "22 degrees 45 minutes north<br>26 degrees 33 minutes east"))
        register(CoordinateClue("rogues-castle-coord", Items.CLUE_SCROLL_3518, ClueLevel.HARD, Location(3285, 3943, 0), "24 degrees 24 minutes north<br>26 degrees 24 minutes east"))
        register(CoordinateClue("wildy-lever-coord", Items.CLUE_SCROLL_3520, ClueLevel.HARD, Location(3158, 3958, 0), "24 degrees 56 minutes north<br>22 degrees 28 minutes east"))
        register(CoordinateClue("pirates-hideout-coord", Items.CLUE_SCROLL_3522, ClueLevel.HARD, Location(3040, 3961, 0), "24 degrees 58 minutes north<br>18 degrees 43 minutes east"))
        register(CoordinateClue("agility-wildy-coord", Items.CLUE_SCROLL_3524, ClueLevel.HARD, Location(2987, 3964, 0), "25 degrees 03 minutes north<br>17 degrees 05 minutes east"))
        register(CoordinateClue("axe-hut-coord", Items.CLUE_SCROLL_3525, ClueLevel.HARD, Location(3190, 3963, 0), "25 degrees 03 minutes north<br>23 degrees 24 minutes east"))
        return this
    }
}
