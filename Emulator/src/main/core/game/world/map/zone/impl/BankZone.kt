package core.game.world.map.zone.impl

import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class BankZone : MapZone("bank", true) {
    override fun configure() {
        register(BANK_ZONE_0)
        register(BANK_ZONE_1)
        register(BANK_ZONE_2)
        register(BANK_ZONE_3)
        register(BANK_ZONE_4)
        register(BANK_ZONE_5)
        register(BANK_ZONE_6)
        register(BANK_ZONE_7)
        register(BANK_ZONE_8)
        register(BANK_ZONE_9)
        register(BANK_ZONE_10)
        register(BANK_ZONE_11)
        register(BANK_ZONE_12)
        register(BANK_ZONE_13)
        register(BANK_ZONE_14)
        register(BANK_ZONE_15)
        register(BANK_ZONE_16)
        register(BANK_ZONE_17)
        register(BANK_ZONE_18)
        register(BANK_ZONE_19)
        register(BANK_ZONE_20)
        register(BANK_ZONE_21)
        register(BANK_ZONE_22)
        register(BANK_ZONE_23)
        register(BANK_ZONE_24)
        register(BANK_ZONE_25)
        register(BANK_ZONE_26)
        register(BANK_ZONE_27)
        register(BANK_ZONE_28)
        register(BANK_ZONE_29)
        register(BANK_ZONE_30)
        register(BANK_ZONE_31)
        register(BANK_ZONE_32)
        register(BANK_ZONE_33)
        register(BANK_ZONE_34)
        register(BANK_ZONE_35)
        register(BANK_ZONE_36)
    }

    companion object {
        @JvmStatic val instance = BankZone()

        @JvmField var BANK_ZONE_0 = ZoneBorders(3179, 3432, 3194, 3446)

        @JvmField var BANK_ZONE_1 = ZoneBorders(3250, 3416, 3257, 3423)

        @JvmField var BANK_ZONE_2 = ZoneBorders(3009, 3355, 3018, 3358)

        @JvmField var BANK_ZONE_3 = ZoneBorders(2943, 3368, 2947, 3372)

        @JvmField var BANK_ZONE_4 = ZoneBorders(2948, 3368, 2949, 3369)

        @JvmField var BANK_ZONE_5 = ZoneBorders(3092, 3240, 3097, 3246)

        @JvmField var BANK_ZONE_6 = ZoneBorders(3207, 3215, 3210, 3222, 2, true)

        @JvmField var BANK_ZONE_7 = ZoneBorders(3269, 3161, 3272, 3173)

        @JvmField var BANK_ZONE_8 = ZoneBorders(3427, 2889, 3430, 2894)

        @JvmField var BANK_ZONE_9 = ZoneBorders(2794, 5159, 2805, 5172)

        @JvmField var BANK_ZONE_10 = ZoneBorders(2843, 2952, 2861, 2956)

        @JvmField var BANK_ZONE_11 = ZoneBorders(2555, 2836, 2559, 2841)

        @JvmField var BANK_ZONE_12 = ZoneBorders(2437, 3081, 2447, 3098)

        @JvmField var BANK_ZONE_13 = ZoneBorders(2350, 3161, 2356, 3166)

        @JvmField var BANK_ZONE_14 = ZoneBorders(2609, 3088, 2614, 3097)

        @JvmField var BANK_ZONE_15 = ZoneBorders(2612, 3330, 2621, 3335)

        @JvmField var BANK_ZONE_16 = ZoneBorders(2585, 3418, 2589, 3422)

        @JvmField var BANK_ZONE_17 = ZoneBorders(2431, 3478, 2487, 3521, 1, true)

        @JvmField var BANK_ZONE_18 = ZoneBorders(2327, 3686, 2332, 3693)

        @JvmField var BANK_ZONE_19 = ZoneBorders(2334, 3805, 2339, 3808)

        @JvmField var BANK_ZONE_20 = ZoneBorders(2414, 3801, 2419, 3803)

        @JvmField var BANK_ZONE_21 = ZoneBorders(2618, 3893, 2621, 3896)

        @JvmField var BANK_ZONE_22 = ZoneBorders(3509, 3474, 3516, 3483)

        @JvmField var BANK_ZONE_23 = ZoneBorders(3686, 3461, 3699, 3471)

        @JvmField var BANK_ZONE_24 = ZoneBorders(3091, 3488, 3098, 3499)

        @JvmField var BANK_ZONE_25 = ZoneBorders(3494, 3210, 3500, 3213)

        @JvmField var BANK_ZONE_26 = ZoneBorders(2721, 3490, 2730, 3493)

        @JvmField var BANK_ZONE_27 = ZoneBorders(2724, 3487, 2727, 3489)

        @JvmField var BANK_ZONE_28 = ZoneBorders(2649, 3280, 2656, 3287)

        @JvmField var BANK_ZONE_29 = ZoneBorders(2665, 2651, 2669, 2655)

        @JvmField var BANK_ZONE_30 = ZoneBorders(3679, 2980, 3680, 2984)

        @JvmField var BANK_ZONE_31 = ZoneBorders(2834, 10206, 2841, 10215)

        @JvmField var BANK_ZONE_32 = ZoneBorders(2843, 3537, 2848, 3545)

        @JvmField var BANK_ZONE_33 = ZoneBorders(3144, 3450, 3148, 3453)

        @JvmField var BANK_ZONE_34 = ZoneBorders(3117, 3119, 3126, 3120)

        @JvmField var BANK_ZONE_35 = ZoneBorders(3119, 3121, 3124, 3122)

        @JvmField var BANK_ZONE_36 = ZoneBorders(3118, 3123, 3124, 3125)
    }

    fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.FIRES, ZoneRestriction.CANNON)
    }
}
