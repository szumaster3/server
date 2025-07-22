package content.global.activity.ttrail

import core.api.LoginListener
import core.api.PersistPlayer
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs.consts.Items

/**
 * Handles the treasure trail of a player.
 * @author Vexia
 */
class TreasureTrailManager :
    LoginListener,
    PersistPlayer {
    val player: Player?
    var clueId = 0
    var level: ClueLevel? = null
    var trailStage = 0
    var trailLength = 0
    val completedClues: IntArray = IntArray(3)

    /**
     * Constructs a [TreasureTrailManager] for the player.
     *
     * @param player the player to associate with this manager.
     */
    constructor(player: Player?) {
        this.player = player
    }

    /**
     * Constructs a TreasureTrailManager without an associated player.
     */
    constructor() {
        this.player = null
    }

    /**
     * Called when the player logs in.
     *
     * Creates and assigns a new TreasureTrailManager instance to the player's attributes.
     *
     * @param player the player who logged in.
     */
    override fun login(player: Player) {
        val instance = TreasureTrailManager(player)
        player.setAttribute("tt-manager", instance)
    }

    /**
     * Parses and loads treasure trail data from the player's saved JSON data.
     *
     * @param player the player whose data is being parsed.
     * @param data the JSON object containing saved player data.
     */
    override fun parsePlayer(
        player: Player,
        data: JSONObject,
    ) {
        val instance = getInstance(player)
        val ttData = data["treasureTrails"] as JSONObject? ?: return
        val cc = ttData["completedClues"] as JSONArray
        for (i in cc.indices) {
            instance.completedClues[i] = cc[i].toString().toInt()
        }
        if (ttData.containsKey("trail")) {
            val trail = ttData["trail"] as JSONObject
            instance.clueId = trail["clueId"].toString().toInt()
            instance.trailLength = trail["length"].toString().toInt()
            instance.trailStage = trail["stage"].toString().toInt()
        }
    }

    /**
     * Saves the current treasure trail state into the player data.
     *
     * @param player the player whose data is being saved.
     * @param save the JSON object to write the data into.
     */
    override fun savePlayer(
        player: Player,
        save: JSONObject,
    ) {
        val instance = getInstance(player)
        val treasureTrailManager = JSONObject()
        if (instance.hasTrail()) {
            val trail = JSONObject()
            trail["clueId"] = instance.clueId.toString()
            trail["length"] = instance.trailLength.toString()
            trail["stage"] = instance.trailStage.toString()
            treasureTrailManager["trail"] = trail
        }
        val completedClues = JSONArray()
        for (clue in instance.completedClues) {
            completedClues.add(clue.toString())
        }
        treasureTrailManager["completedClues"] = completedClues
        save["treasureTrails"] = treasureTrailManager
    }

    /**
     * Parses treasure trail data from a generic JSON object.
     *
     * @param data the JSON object containing treasure trail data.
     */
    fun parse(data: JSONObject) {
        val cc = data["completedClues"] as JSONArray
        for (i in cc.indices) {
            completedClues[i] = cc[i].toString().toInt()
        }

        if (data.containsKey("trail")) {
            val trail = data["trail"] as JSONObject
            clueId = trail["clueId"].toString().toInt()
            trailLength = trail["length"].toString().toInt()
            trailStage = trail["stage"].toString().toInt()
        }
    }

    /**
     * Starts a new treasure trail based on the provided clue scroll.
     *
     * @param clue the clue scroll plugin representing the new trail.
     */
    fun startTrail(clue: ClueScroll) {
        setClue(clue)
        trailLength = RandomFunction.random(clue.level!!.minSteps, clue.level!!.maxSteps)
        trailStage = 0
    }

    /**
     * Clears the current treasure trail state.
     *
     * Resets clue id, difficulty level, trail length, and stage.
     */
    fun clearTrail() {
        clueId = 0
        level = null
        trailLength = 0
        trailStage = 0
    }

    /**
     * Sets the current clue scroll for this trail manager.
     *
     * @param clue the clue scroll plugin to set.
     */
    fun setClue(clue: ClueScroll) {
        clueId = clue.clueId
        level = clue.level
    }

    /**
     * Checks if the player currently has any clue scroll or related trail item.
     *
     * @return true if the player has a clue or trail item; false otherwise.
     */
    fun hasClue(): Boolean {
        if (player == null) {
            return true
        }
        if (player.hasItem(ClueLevel.EASY.getCasket()) ||
            player.hasItem(ClueLevel.MEDIUM.getCasket()) ||
            player.hasItem(ClueLevel.HARD.getCasket())
        ) {
            return true
        }
        for (id in clueScrollIds) {
            if (player.inventory.contains(id, 1) || player.bank.contains(id, 1)) {
                return true
            }
        }
        return false
    }

    /**
     * Increments the current trail stage by one.
     */
    fun incrementStage() {
        trailStage += 1
    }

    /**
     * Checks if the treasure trail is completed.
     *
     * Returns true if the trail has started and the current stage is at least the trail length.
     */
    val isCompleted: Boolean
        get() = trailStage != 0 && trailLength != 0 && trailStage >= trailLength

    /**
     * Checks if there is an active treasure trail.
     *
     * Returns true if clue id, level, trail length, and stage are all set and non-zero.
     */
    fun hasTrail(): Boolean = clueId != 0 && (level != null) and (trailLength != 0) && trailStage != 0

    /**
     * Increments the count of completed clues for the specified difficulty level.
     *
     * @param level the difficulty level for which to increment completed clues.
     */
    fun incrementClues(level: ClueLevel) {
        completedClues[level.ordinal]++
    }

    /**
     * Gets the number of completed clues for the specified difficulty level.
     *
     * @param level the difficulty level to query.
     * @return the count of completed clues for that level.
     */
    fun getCompletedClues(level: ClueLevel): Int = completedClues[level.ordinal]

    companion object {
        val idMap = HashMap<Int, Int>()

        /**
         * Represents the clue scroll ids.
         */
        val clueScrollIds: IntArray = intArrayOf(
            Items.CLUE_SCROLL_2677, Items.CLUE_SCROLL_2678, Items.CLUE_SCROLL_2679, Items.CLUE_SCROLL_2680,
            Items.CLUE_SCROLL_2682, Items.CLUE_SCROLL_2683, Items.CLUE_SCROLL_2684, Items.CLUE_SCROLL_2685,
            Items.CLUE_SCROLL_2686, Items.CLUE_SCROLL_2687, Items.CLUE_SCROLL_2688, Items.CLUE_SCROLL_2689,
            Items.CLUE_SCROLL_2690, Items.CLUE_SCROLL_2691, Items.CLUE_SCROLL_2692, Items.CLUE_SCROLL_2693,
            Items.CLUE_SCROLL_2694, Items.CLUE_SCROLL_2695, Items.CLUE_SCROLL_2696, Items.CLUE_SCROLL_2697,
            Items.CLUE_SCROLL_2698, Items.CLUE_SCROLL_2699, Items.CLUE_SCROLL_2700, Items.CLUE_SCROLL_2701,
            Items.CLUE_SCROLL_2702, Items.CLUE_SCROLL_2703, Items.CLUE_SCROLL_2704, Items.CLUE_SCROLL_2705,
            Items.CLUE_SCROLL_2706, Items.CLUE_SCROLL_2707, Items.CLUE_SCROLL_2708, Items.CLUE_SCROLL_2709,
            Items.CLUE_SCROLL_2710, Items.CLUE_SCROLL_2711, Items.CLUE_SCROLL_2712, Items.CLUE_SCROLL_2713,
            Items.CLUE_SCROLL_2716, Items.CLUE_SCROLL_2719, Items.CLUE_SCROLL_2722, Items.CLUE_SCROLL_2723,
            Items.CLUE_SCROLL_2725, Items.CLUE_SCROLL_2727, Items.CLUE_SCROLL_2729, Items.CLUE_SCROLL_2731,
            Items.CLUE_SCROLL_2733, Items.CLUE_SCROLL_2735, Items.CLUE_SCROLL_2737, Items.CLUE_SCROLL_2739,
            Items.CLUE_SCROLL_2741, Items.CLUE_SCROLL_2743, Items.CLUE_SCROLL_2745, Items.CLUE_SCROLL_2747,
            Items.CLUE_SCROLL_2773, Items.CLUE_SCROLL_2774, Items.CLUE_SCROLL_2776, Items.CLUE_SCROLL_2778,
            Items.CLUE_SCROLL_2780, Items.CLUE_SCROLL_2782, Items.CLUE_SCROLL_2783, Items.CLUE_SCROLL_2785,
            Items.CLUE_SCROLL_2786, Items.CLUE_SCROLL_2788, Items.CLUE_SCROLL_2790, Items.CLUE_SCROLL_2792,
            Items.CLUE_SCROLL_2793, Items.CLUE_SCROLL_2794, Items.CLUE_SCROLL_2796, Items.CLUE_SCROLL_2797,
            Items.CLUE_SCROLL_2799, Items.CLUE_SCROLL_2801, Items.CLUE_SCROLL_2803, Items.CLUE_SCROLL_2805,
            Items.CLUE_SCROLL_2807, Items.CLUE_SCROLL_2809, Items.CLUE_SCROLL_2811, Items.CLUE_SCROLL_2813,
            Items.CLUE_SCROLL_2815, Items.CLUE_SCROLL_2817, Items.CLUE_SCROLL_2819, Items.CLUE_SCROLL_2821,
            Items.CLUE_SCROLL_2823, Items.CLUE_SCROLL_2825, Items.CLUE_SCROLL_2827, Items.CLUE_SCROLL_2829,
            Items.CLUE_SCROLL_2831, Items.CLUE_SCROLL_2833, Items.CLUE_SCROLL_2835, Items.CLUE_SCROLL_2837,
            Items.CLUE_SCROLL_2839, Items.CLUE_SCROLL_2841, Items.CHALLENGE_SCROLL_2842, Items.CLUE_SCROLL_2843,
            Items.CHALLENGE_SCROLL_2844, Items.CLUE_SCROLL_2845, Items.CHALLENGE_SCROLL_2846, Items.CLUE_SCROLL_2847,
            Items.CLUE_SCROLL_2848, Items.CLUE_SCROLL_2849, Items.CHALLENGE_SCROLL_2850, Items.CLUE_SCROLL_2851,
            Items.CHALLENGE_SCROLL_2852, Items.CLUE_SCROLL_2853, Items.CHALLENGE_SCROLL_2854,
            Items.CLUE_SCROLL_2855, Items.CLUE_SCROLL_2856, Items.CLUE_SCROLL_2857, Items.CLUE_SCROLL_2858,
            Items.CLUE_SCROLL_3490, Items.CLUE_SCROLL_3491, Items.CLUE_SCROLL_3492, Items.CLUE_SCROLL_3493,
            Items.CLUE_SCROLL_3494, Items.CLUE_SCROLL_3495, Items.CLUE_SCROLL_3496, Items.CLUE_SCROLL_3497,
            Items.CLUE_SCROLL_3498, Items.CLUE_SCROLL_3499, Items.CLUE_SCROLL_3500, Items.CLUE_SCROLL_3501,
            Items.CLUE_SCROLL_3502, Items.CLUE_SCROLL_3503, Items.CLUE_SCROLL_3504, Items.CLUE_SCROLL_3505,
            Items.CLUE_SCROLL_3506, Items.CLUE_SCROLL_3507, Items.CLUE_SCROLL_3508, Items.CLUE_SCROLL_3509,
            Items.CLUE_SCROLL_3510, Items.CLUE_SCROLL_3512, Items.CLUE_SCROLL_3513, Items.CLUE_SCROLL_3514,
            Items.CLUE_SCROLL_3515, Items.CLUE_SCROLL_3516, Items.CLUE_SCROLL_3518, Items.CLUE_SCROLL_3520,
            Items.CLUE_SCROLL_3522, Items.CLUE_SCROLL_3524, Items.CLUE_SCROLL_3525, Items.CLUE_SCROLL_3526,
            Items.CLUE_SCROLL_3528, Items.CLUE_SCROLL_3530, Items.CLUE_SCROLL_3532, Items.CLUE_SCROLL_3534,
            Items.CLUE_SCROLL_3536, Items.CLUE_SCROLL_3538, Items.CLUE_SCROLL_3540, Items.CLUE_SCROLL_3542,
            Items.CLUE_SCROLL_3544, Items.CLUE_SCROLL_3546, Items.CLUE_SCROLL_3548, Items.CLUE_SCROLL_3550,
            Items.CLUE_SCROLL_3552, Items.CLUE_SCROLL_3554, Items.CLUE_SCROLL_3556, Items.CLUE_SCROLL_3558,
            Items.CLUE_SCROLL_3560, Items.CLUE_SCROLL_3562, Items.CLUE_SCROLL_3564, Items.CLUE_SCROLL_3566,
            Items.CLUE_SCROLL_3568, Items.CLUE_SCROLL_3570, Items.CLUE_SCROLL_3572, Items.CLUE_SCROLL_3573,
            Items.CLUE_SCROLL_3574, Items.CLUE_SCROLL_3575, Items.CLUE_SCROLL_3577, Items.CLUE_SCROLL_3579,
            Items.CLUE_SCROLL_3580, Items.CLUE_SCROLL_3582, Items.CLUE_SCROLL_3584, Items.CLUE_SCROLL_3586,
            Items.CLUE_SCROLL_3588, Items.CLUE_SCROLL_3590, Items.CLUE_SCROLL_3592, Items.CLUE_SCROLL_3594,
            Items.CLUE_SCROLL_3596, Items.CLUE_SCROLL_3598, Items.CLUE_SCROLL_3599, Items.CLUE_SCROLL_3601,
            Items.CLUE_SCROLL_3602, Items.CLUE_SCROLL_3604, Items.CLUE_SCROLL_3605, Items.CLUE_SCROLL_3607,
            Items.CLUE_SCROLL_3609, Items.CLUE_SCROLL_3610, Items.CLUE_SCROLL_3611, Items.CLUE_SCROLL_3612,
            Items.CLUE_SCROLL_3613, Items.CLUE_SCROLL_3614, Items.CLUE_SCROLL_3615, Items.CLUE_SCROLL_3616,
            Items.CLUE_SCROLL_3617, Items.CLUE_SCROLL_3618, Items.CLUE_SCROLL_7236, Items.CLUE_SCROLL_7238,
            Items.CLUE_SCROLL_7239, Items.CLUE_SCROLL_7241, Items.CLUE_SCROLL_7243, Items.CLUE_SCROLL_7245,
            Items.CLUE_SCROLL_7247, Items.CLUE_SCROLL_7248, Items.CLUE_SCROLL_7249, Items.CLUE_SCROLL_7250,
            Items.CLUE_SCROLL_7251, Items.CLUE_SCROLL_7252, Items.CLUE_SCROLL_7253, Items.CLUE_SCROLL_7254,
            Items.CLUE_SCROLL_7255, Items.CLUE_SCROLL_7256, Items.CLUE_SCROLL_7258, Items.CLUE_SCROLL_7260,
            Items.CLUE_SCROLL_7262, Items.CLUE_SCROLL_7264, Items.CLUE_SCROLL_7266, Items.CLUE_SCROLL_7268,
            Items.CHALLENGE_SCROLL_7269, Items.CLUE_SCROLL_7270, Items.CHALLENGE_SCROLL_7271, Items.CLUE_SCROLL_7272,
            Items.CHALLENGE_SCROLL_7273, Items.CLUE_SCROLL_7274, Items.CHALLENGE_SCROLL_7275, Items.CLUE_SCROLL_7276,
            Items.CHALLENGE_SCROLL_7277, Items.CLUE_SCROLL_7278, Items.CHALLENGE_SCROLL_7279, Items.CLUE_SCROLL_7280,
            Items.CHALLENGE_SCROLL_7281, Items.CLUE_SCROLL_7282, Items.CHALLENGE_SCROLL_7283, Items.CLUE_SCROLL_7284,
            Items.CHALLENGE_SCROLL_7285, Items.CLUE_SCROLL_7286, Items.CLUE_SCROLL_7288, Items.CLUE_SCROLL_7290,
            Items.CLUE_SCROLL_7292, Items.CLUE_SCROLL_7294, Items.CLUE_SCROLL_7296, Items.CLUE_SCROLL_7298,
            Items.CLUE_SCROLL_7300, Items.CLUE_SCROLL_7301, Items.CLUE_SCROLL_7303, Items.CLUE_SCROLL_7304,
            Items.CLUE_SCROLL_7305, Items.CLUE_SCROLL_7307, Items.CLUE_SCROLL_7309, Items.CLUE_SCROLL_7311,
            Items.CLUE_SCROLL_7313, Items.CLUE_SCROLL_7315, Items.CLUE_SCROLL_7317, Items.CLUE_SCROLL_10180,
            Items.CLUE_SCROLL_10182, Items.CLUE_SCROLL_10184, Items.CLUE_SCROLL_10186, Items.CLUE_SCROLL_10188,
            Items.CLUE_SCROLL_10190, Items.CLUE_SCROLL_10192, Items.CLUE_SCROLL_10194, Items.CLUE_SCROLL_10196,
            Items.CLUE_SCROLL_10198, Items.CLUE_SCROLL_10200, Items.CLUE_SCROLL_10202, Items.CLUE_SCROLL_10204,
            Items.CLUE_SCROLL_10206, Items.CLUE_SCROLL_10208, Items.CLUE_SCROLL_10210, Items.CLUE_SCROLL_10212,
            Items.CLUE_SCROLL_10214, Items.CLUE_SCROLL_10216, Items.CLUE_SCROLL_10218, Items.CLUE_SCROLL_10220,
            Items.CLUE_SCROLL_10222, Items.CLUE_SCROLL_10224, Items.CLUE_SCROLL_10226, Items.CLUE_SCROLL_10228,
            Items.CLUE_SCROLL_10230, Items.CLUE_SCROLL_10232, Items.CLUE_SCROLL_10234, Items.CLUE_SCROLL_10236,
            Items.CLUE_SCROLL_10238, Items.CLUE_SCROLL_10240, Items.CLUE_SCROLL_10242, Items.CLUE_SCROLL_10244,
            Items.CLUE_SCROLL_10246, Items.CLUE_SCROLL_10248, Items.CLUE_SCROLL_10250, Items.CLUE_SCROLL_10252,
            Items.CLUE_SCROLL_10254, Items.CLUE_SCROLL_10256, Items.CLUE_SCROLL_10258, Items.CLUE_SCROLL_10260,
            Items.CLUE_SCROLL_10262, Items.CLUE_SCROLL_10264, Items.CLUE_SCROLL_10266, Items.CLUE_SCROLL_10268,
            Items.CLUE_SCROLL_10270, Items.CLUE_SCROLL_10272, Items.CLUE_SCROLL_10274, Items.CLUE_SCROLL_10276,
            Items.CLUE_SCROLL_10278, Items.CLUE_SCROLL_13010, Items.CLUE_SCROLL_13012, Items.CLUE_SCROLL_13014,
            Items.CLUE_SCROLL_13016, Items.CLUE_SCROLL_13018, Items.CLUE_SCROLL_13020, Items.CLUE_SCROLL_13022,
            Items.CLUE_SCROLL_13024, Items.CLUE_SCROLL_13026, Items.CLUE_SCROLL_13028, Items.CLUE_SCROLL_13030,
            Items.CLUE_SCROLL_13032, Items.CLUE_SCROLL_13034, Items.CLUE_SCROLL_13036, Items.CLUE_SCROLL_13038,
            Items.CLUE_SCROLL_13040, Items.CLUE_SCROLL_13041, Items.CLUE_SCROLL_13042, Items.CLUE_SCROLL_13044,
            Items.CLUE_SCROLL_13046, Items.CLUE_SCROLL_13048, Items.CLUE_SCROLL_13049, Items.CLUE_SCROLL_13050,
            Items.CLUE_SCROLL_13051, Items.CLUE_SCROLL_13053, Items.CLUE_SCROLL_13055, Items.CLUE_SCROLL_13056,
            Items.CLUE_SCROLL_13058, Items.CLUE_SCROLL_13060, Items.CLUE_SCROLL_13061, Items.CLUE_SCROLL_13063,
            Items.CLUE_SCROLL_13065, Items.CLUE_SCROLL_13067, Items.CLUE_SCROLL_13068, Items.CLUE_SCROLL_13069,
            Items.CLUE_SCROLL_13070, Items.CLUE_SCROLL_13071, Items.CLUE_SCROLL_13072, Items.CLUE_SCROLL_13074,
            Items.CLUE_SCROLL_13075, Items.CLUE_SCROLL_13076, Items.CLUE_SCROLL_13078, Items.CLUE_SCROLL_13079,
            Items.CLUE_SCROLL_13080
        )
        init {
            for (id in clueScrollIds) {
                idMap[id] = 0
            }
        }

        /**
         * Gets the instance of [TreasureTrailManager] associated with the player.
         *
         * @param player The player for whom to get the treasure trail manager.
         * @return The [TreasureTrailManager] instance tied to the player.
         */
        @JvmStatic
        fun getInstance(player: Player): TreasureTrailManager = player.getAttribute("tt-manager", TreasureTrailManager())
    }
}
