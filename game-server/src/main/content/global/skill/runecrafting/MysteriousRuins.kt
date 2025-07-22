package content.global.skill.runecrafting

import core.game.node.scenery.Scenery
import core.game.world.map.Location

/**
 * Represents the various Mysterious Ruins
 */
enum class MysteriousRuins(val `object`: IntArray, val base: Location, @JvmField val end: Location, val talisman: Talisman, val tiara: Tiara) {
    AIR(intArrayOf(2452, 7103, 7104), Location.create(2983, 3292, 0), Location.create(2841, 4829, 0), Talisman.AIR, Tiara.AIR),
    MIND(intArrayOf(2453, 7105, 7106), Location.create(2980, 3514, 0), Location.create(2793, 4828, 0), Talisman.MIND, Tiara.MIND),
    WATER(intArrayOf(2454, 7107, 7108), Location.create(3184, 3163, 0), Location.create(3494, 4832, 0), Talisman.WATER, Tiara.WATER),
    EARTH(intArrayOf(2455, 7109, 7110), Location.create(3304, 3475, 0), Location.create(2655, 4830, 0), Talisman.EARTH, Tiara.EARTH),
    FIRE(intArrayOf(2456, 7111, 7112), Location.create(3312, 3253, 0), Location.create(2577, 4846, 0), Talisman.FIRE, Tiara.FIRE),
    BODY(intArrayOf(2457, 7113, 7114), Location.create(3052, 3443, 0), Location.create(2521, 4834, 0), Talisman.BODY, Tiara.BODY),
    COSMIC(intArrayOf(2458, 7115, 7116), Location.create(2407, 4375, 0), Location.create(2162, 4833, 0), Talisman.COSMIC, Tiara.COSMIC),
    CHAOS(intArrayOf(2461, 7121, 7122), Location.create(3059, 3589, 0), Location.create(2281, 4837, 0), Talisman.CHAOS, Tiara.CHAOS),
    NATURE(intArrayOf(2460, 7119, 7120), Location.create(2869, 3021, 0), Location.create(2400, 4835, 0), Talisman.NATURE, Tiara.NATURE),
    LAW(intArrayOf(2459, 7117, 7118), Location.create(2857, 3379, 0), Location.create(2464, 4818, 0), Talisman.LAW, Tiara.LAW),
    DEATH(intArrayOf(2462, 7123, 7124), Location.create(1862, 4639, 0), Location.create(2208, 4830, 0), Talisman.DEATH, Tiara.DEATH),
    BLOOD(intArrayOf(2464, 30529, 30530), Location.create(3561, 9779, 0), Location.create(2467, 4889, 1), Talisman.BLOOD, Tiara.BLOOD);

    companion object {
        /**
         * Returns [MysteriousRuins] matching the scenery object's id, or null if none.
         */
        fun forObject(scenery: Scenery): MysteriousRuins? =
            values().find { ruin -> scenery.id in ruin.`object` }

        /**
         * Returns [MysteriousRuins] linked to the given talisman, or null if none.
         */
        fun forTalisman(talisman: Talisman): MysteriousRuins? =
            values().find { it.talisman == talisman }
    }
}
