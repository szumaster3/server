package content.global.skill.runecrafting

import core.game.node.scenery.Scenery
import core.game.world.map.Location

/**
 * Represents the various Mysterious Ruins in the game world.
 *
 * @property object The object ids that represent this ruin in the game world.
 * @property base The surface location of the ruin.
 * @property end The altar location the player is teleported to.
 * @property talisman The [Talisman] used to access this altar.
 * @property tiara The [Tiara] used to access this altar.
 */
enum class MysteriousRuins(
    val `object`: IntArray,
    val base: Location,
    @JvmField val end: Location,
    val talisman: Talisman,
    val tiara: Tiara
) {
    /**
     * Ruins leading to the Air altar.
     */
    AIR(intArrayOf(2452, 7103, 7104), Location.create(2983, 3292, 0), Location.create(2841, 4829, 0), Talisman.AIR, Tiara.AIR),

    /**
     * Ruins leading to the Mind altar.
     */
    MIND(intArrayOf(2453, 7105, 7106), Location.create(2980, 3514, 0), Location.create(2793, 4828, 0), Talisman.MIND, Tiara.MIND),

    /**
     * Ruins leading to the Water altar.
     */
    WATER(intArrayOf(2454, 7107, 7108), Location.create(3184, 3163, 0), Location.create(3494, 4832, 0), Talisman.WATER, Tiara.WATER),

    /**
     * Ruins leading to the Earth altar.
     */
    EARTH(intArrayOf(2455, 7109, 7110), Location.create(3304, 3475, 0), Location.create(2655, 4830, 0), Talisman.EARTH, Tiara.EARTH),

    /**
     * Ruins leading to the Fire altar.
     */
    FIRE(intArrayOf(2456, 7111, 7112), Location.create(3312, 3253, 0), Location.create(2577, 4846, 0), Talisman.FIRE, Tiara.FIRE),

    /**
     * Ruins leading to the Body altar.
     */
    BODY(intArrayOf(2457, 7113, 7114), Location.create(3052, 3443, 0), Location.create(2521, 4834, 0), Talisman.BODY, Tiara.BODY),

    /**
     * Ruins leading to the Cosmic altar.
     */
    COSMIC(intArrayOf(2458, 7115, 7116), Location.create(2407, 4375, 0), Location.create(2162, 4833, 0), Talisman.COSMIC, Tiara.COSMIC),

    /**
     * Ruins leading to the Chaos altar.
     */
    CHAOS(intArrayOf(2461, 7121, 7122), Location.create(3059, 3589, 0), Location.create(2281, 4837, 0), Talisman.CHAOS, Tiara.CHAOS),

    /**
     * Ruins leading to the Nature altar.
     */
    NATURE(intArrayOf(2460, 7119, 7120), Location.create(2869, 3021, 0), Location.create(2400, 4835, 0), Talisman.NATURE, Tiara.NATURE),

    /**
     * Ruins leading to the Law altar.
     */
    LAW(intArrayOf(2459, 7117, 7118), Location.create(2857, 3379, 0), Location.create(2464, 4818, 0), Talisman.LAW, Tiara.LAW),

    /**
     * Ruins leading to the Death altar.
     */
    DEATH(intArrayOf(2462, 7123, 7124), Location.create(1862, 4639, 0), Location.create(2208, 4830, 0), Talisman.DEATH, Tiara.DEATH),

    /**
     * Ruins leading to the Blood altar.
     */
    BLOOD(intArrayOf(2464, 30529, 30530), Location.create(3561, 9779, 0), Location.create(2467, 4889, 1), Talisman.BLOOD, Tiara.BLOOD);

    companion object {
        /**
         * Gets the corresponding [MysteriousRuins] for a given [Scenery] object.
         *
         * This is typically used when a player interacts with a ruin object in the game world.
         *
         * @param scenery The scenery object being interacted with.
         * @return The associated [MysteriousRuins], or `null` if no match is found.
         */
        fun forObject(scenery: Scenery): MysteriousRuins? {
            for (ruin in values()) {
                for (i in ruin.`object`) {
                    if (i == scenery.id) {
                        return ruin
                    }
                }
            }
            return null
        }

        /**
         * Gets the [MysteriousRuins] corresponding to a given [Talisman].
         *
         * Used to find which ruin a specific talisman grants access to.
         *
         * @param talisman The talisman being used.
         * @return The associated [MysteriousRuins], or `null` if no match is found.
         */
        fun forTalisman(talisman: Talisman): MysteriousRuins? {
            for (ruin in values()) {
                if (ruin.talisman == talisman) {
                    return ruin
                }
            }
            return null
        }
    }
}
