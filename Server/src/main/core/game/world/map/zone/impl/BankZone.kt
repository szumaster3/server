package core.game.world.map.zone.impl

import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

/**
 * Represents a bank zone.
 * @author Vexia
 */
class BankZone : MapZone("bank", true, ZoneRestriction.CANNON, ZoneRestriction.FIRES) {

    override fun configure() {
        BANK_ZONES.forEach { register(it) }
    }

    companion object {
        @JvmStatic
        val instance = BankZone()

        private val BANK_ZONES = arrayOf(
            ZoneBorders(3179, 3432, 3194, 3446),
            ZoneBorders(3250, 3416, 3257, 3423),
            ZoneBorders(3009, 3355, 3018, 3358),
            ZoneBorders(2943, 3368, 2947, 3372),
            ZoneBorders(2948, 3368, 2949, 3369),
            ZoneBorders(3092, 3240, 3097, 3246),
            ZoneBorders(3207, 3215, 3210, 3222, 2, true),
            ZoneBorders(3269, 3161, 3272, 3173),
            ZoneBorders(3427, 2889, 3430, 2894),
            ZoneBorders(2794, 5159, 2805, 5172),
            ZoneBorders(2843, 2952, 2861, 2956),
            ZoneBorders(2555, 2836, 2559, 2841),
            ZoneBorders(2437, 3081, 2447, 3098),
            ZoneBorders(2350, 3161, 2356, 3166),
            ZoneBorders(2609, 3088, 2614, 3097),
            ZoneBorders(2612, 3330, 2621, 3335),
            ZoneBorders(2585, 3418, 2589, 3422),
            ZoneBorders(2431, 3478, 2487, 3521, 1, true),
            ZoneBorders(2327, 3686, 2332, 3693),
            ZoneBorders(2334, 3805, 2339, 3808),
            ZoneBorders(2414, 3801, 2419, 3803),
            ZoneBorders(2618, 3893, 2621, 3896),
            ZoneBorders(3509, 3474, 3516, 3483),
            ZoneBorders(3686, 3461, 3699, 3471),
            ZoneBorders(3091, 3488, 3098, 3499),
            ZoneBorders(3494, 3210, 3500, 3213),
            ZoneBorders(2721, 3490, 2730, 3493),
            ZoneBorders(2724, 3487, 2727, 3489),
            ZoneBorders(2649, 3280, 2656, 3287),
            ZoneBorders(2665, 2651, 2669, 2655),
            ZoneBorders(3679, 2980, 3680, 2984),
            ZoneBorders(2834, 10206, 2841, 10215),
            ZoneBorders(2843, 3537, 2848, 3545),
            ZoneBorders(3144, 3450, 3148, 3453),
            ZoneBorders(3117, 3119, 3126, 3120),
            ZoneBorders(3119, 3121, 3124, 3122),
            ZoneBorders(3118, 3123, 3124, 3125),
            ZoneBorders(2806, 3438, 2812, 3442)
        )
    }
}
