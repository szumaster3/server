package content.global.activity.ttrail

import core.api.LoginListener
import core.api.PersistPlayer
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class TreasureTrailManager :
    LoginListener,
    PersistPlayer {
    val player: Player?
    var clueId = 0
    var level: ClueLevel? = null
    private var trailStage = 0
    private var trailLength = 0
    val completedClues: IntArray = IntArray(3)

    constructor(player: Player?) {
        this.player = player
    }

    constructor() {
        this.player = null
    }

    override fun login(player: Player) {
        val instance = TreasureTrailManager(player)
        player.setAttribute("tt-manager", instance)
    }

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

    fun startTrail(clue: ClueScrollPlugin) {
        setClue(clue)
        trailLength = RandomFunction.random(clue.level.minSteps, clue.level.maxSteps)
        trailStage = 0
    }

    fun clearTrail() {
        clueId = 0
        level = null
        trailLength = 0
        trailStage = 0
    }

    fun setClue(clue: ClueScrollPlugin) {
        clueId = clue.clueId
        level = clue.level
    }

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
        for (id in IDS) {
            if (player.inventory.contains(id, 1) || player.bank.contains(id, 1)) {
                return true
            }
        }
        return false
    }

    fun incrementStage() {
        trailStage += 1
    }

    val isCompleted: Boolean
        get() = trailStage != 0 && trailLength != 0 && trailStage >= trailLength

    fun hasTrail(): Boolean {
        return clueId != 0 && (level != null) and (trailLength != 0) && trailStage != 0
    }

    fun incrementClues(level: ClueLevel) {
        completedClues[level.ordinal]++
    }

    fun getCompletedClues(level: ClueLevel): Int {
        return completedClues[level.ordinal]
    }

    fun getTrailLength(): Int {
        return trailLength
    }

    fun getTrailStage(): Int {
        return trailStage
    }

    companion object {
        private val IDS =
            intArrayOf(
                2677,
                2678,
                2679,
                2680,
                2682,
                2683,
                2684,
                2685,
                2686,
                2687,
                2688,
                2689,
                2690,
                2691,
                2692,
                2693,
                2694,
                2695,
                2696,
                2697,
                2698,
                2699,
                2700,
                2701,
                2702,
                2703,
                2704,
                2705,
                2706,
                2707,
                2708,
                2709,
                2710,
                2711,
                2712,
                2713,
                2716,
                2719,
                2722,
                2723,
                2725,
                2727,
                2729,
                2731,
                2733,
                2735,
                2737,
                2739,
                2741,
                2743,
                2745,
                2747,
                2773,
                2774,
                2776,
                2778,
                2780,
                2782,
                2783,
                2785,
                2786,
                2788,
                2790,
                2792,
                2793,
                2794,
                2796,
                2797,
                2799,
                2801,
                2803,
                2805,
                2807,
                2809,
                2811,
                2813,
                2815,
                2817,
                2819,
                2821,
                2823,
                2825,
                2827,
                2829,
                2831,
                2833,
                2835,
                2837,
                2839,
                2841,
                2843,
                2845,
                2847,
                2848,
                2849,
                2851,
                2853,
                2855,
                2856,
                2857,
                2858,
                3490,
                3491,
                3492,
                3493,
                3494,
                3495,
                3496,
                3497,
                3498,
                3499,
                3500,
                3501,
                3502,
                3503,
                3504,
                3505,
                3506,
                3507,
                3508,
                3509,
                3510,
                3512,
                3513,
                3514,
                3515,
                3516,
                3518,
                3520,
                3522,
                3524,
                3525,
                3526,
                3528,
                3530,
                3532,
                3534,
                3536,
                3538,
                3540,
                3542,
                3544,
                3546,
                3548,
                3550,
                3552,
                3554,
                3556,
                3558,
                3560,
                3562,
                3564,
                3566,
                3568,
                3570,
                3572,
                3573,
                3574,
                3575,
                3577,
                3579,
                3580,
                3582,
                3584,
                3586,
                3588,
                3590,
                3592,
                3594,
                3596,
                3598,
                3599,
                3601,
                3602,
                3604,
                3605,
                3607,
                3609,
                3610,
                3611,
                3612,
                3613,
                3614,
                3615,
                3616,
                3617,
                3618,
                7236,
                7238,
                7239,
                7241,
                7243,
                7245,
                7247,
                7248,
                7249,
                7250,
                7251,
                7252,
                7253,
                7254,
                7255,
                7256,
                7258,
                7260,
                7262,
                7264,
                7266,
                7268,
                7270,
                7272,
                7274,
                7276,
                7278,
                7280,
                7282,
                7284,
                7286,
                7288,
                7290,
                7292,
                7294,
                7296,
                7298,
                7300,
                7301,
                7303,
                7304,
                7305,
                7307,
                7309,
                7311,
                7313,
                7315,
                7317,
                10180,
                10182,
                10184,
                10186,
                10188,
                10190,
                10192,
                10194,
                10196,
                10198,
                10200,
                10202,
                10204,
                10206,
                10208,
                10210,
                10212,
                10214,
                10216,
                10218,
                10220,
                10222,
                10224,
                10226,
                10228,
                10230,
                10232,
                10234,
                10236,
                10238,
                10240,
                10242,
                10244,
                10246,
                10248,
                10250,
                10252,
                10254,
                10256,
                10258,
                10260,
                10262,
                10264,
                10266,
                10268,
                10270,
                10272,
                10274,
                10276,
                10278,
                13010,
                13012,
                13014,
                13016,
                13018,
                13020,
                13022,
                13024,
                13026,
                13028,
                13030,
                13032,
                13034,
                13036,
                13038,
                13040,
                13041,
                13042,
                13044,
                13046,
                13048,
                13049,
                13050,
                13051,
                13053,
                13055,
                13056,
                13058,
                13060,
                13061,
                13063,
                13065,
                13067,
                13068,
                13069,
                13070,
                13071,
                13072,
                13074,
                13075,
                13076,
                13078,
                13079,
                13080,
            )

        fun getInstance(player: Player): TreasureTrailManager {
            return player.getAttribute("tt-manager", TreasureTrailManager())
        }
    }
}
