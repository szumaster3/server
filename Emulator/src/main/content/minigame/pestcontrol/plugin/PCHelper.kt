package content.minigame.pestcontrol.plugin

import content.minigame.pestcontrol.plugin.bots.PestControlIntermediateBot
import content.minigame.pestcontrol.plugin.bots.PestControlNoviceBot
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Scenery
import kotlin.random.Random

/**
 * Utility helper object for [PestControlSession] logic and data handling.
 */
object PCHelper {

    /**
     * List of gate object IDs used in Pest Control.
     */
    var GATE_ENTRIES = listOf(Scenery.GATE_14233, Scenery.GATE_14234, Scenery.GATE_14235)

    /**
     * Portal NPC IDs that can be attacked in the novice lander.
     */
    var Portals_AttackableN = listOf(6142, 6143, 6144, 6145).toMutableList()

    /**
     * Portal NPC IDs that can be attacked in the intermediate lander.
     */
    var Portals_AttackableI = listOf(6150, 6151, 6152, 6153).toMutableList()

    /**
     * Portal NPC IDs that can be attacked in the veteran lander.
     */
    var Portals_AttackableV = listOf(7551, 7552, 7553, 7554).toMutableList()

    /**
     * Full list of all portal NPC IDs in Pest Control.
     */
    val portalIds = arrayOf(6142, 6143, 6144, 6145, 6146, 6147, 6148, 6149, 6150, 6151, 6152, 6153, 6154, 6155, 6156, 6157, 7551, 7552, 7553, 7554, 7555, 7556, 7557, 7558)

    /**
     * List of portal entries used for validations or targeting.
     */
    var PORTAL_ENTRIES = listOf(*portalIds)

    /**
     * Location of the intermediate Pest Control lander.
     */
    val PestControlLanderIntermediate = Location.create(2644, 2646, 0)

    /**
     * Location of the novice Pest Control lander.
     */
    val PestControlLanderNovice = Location.create(2657, 2642, 0)

    /**
     * Location on the Pest Control island.
     */
    val PestControlIslandLocation = Location.create(2659, 2676, 0)

    /**
     * Duplicate location for Pest Control island (likely used for safety).
     */
    val PestControlIslandLocation2 = Location.create(2659, 2676, 0)

    /**
     * Randomized location near the Pest Control island.
     * Selected during helper initialization for dynamic positioning.
     */
    var pclocations = if (Random.nextBoolean()) {
        Location.create(2667, 2653, 0)
    } else if (Random.nextBoolean()) {
        Location.create(2658, 2654, 0)
    } else if (Random.nextBoolean()) {
        Location.create(2651, 2659, 0)
    } else if (Random.nextBoolean()) {
        Location.create(2650, 2664, 0)
    } else {
        Location.create(2665, 2660, 0)
    }

    /**
     * Checks whether the given player is currently inside a Pest Control instance.
     *
     * @param p The player to check.
     * @return True if the player is in an instance, false otherwise.
     */
    fun isInPestControlInstance(p: Player): Boolean = p.getAttribute<Any?>("pc_zeal") != null

    /**
     * Information about the different Pest Control boats.
     *
     * @property boatBorder The borders defining the playable lander area.
     * @property outsideBoatBorder The borders defining the area outside the gangplank.
     * @property ladderId The object ID of the ladder used to leave the boat.
     */
    enum class BoatInfo(
        val boatBorder: ZoneBorders,
        val outsideBoatBorder: ZoneBorders,
        val ladderId: Int,
    ) {
        /**
         * Novice lander boat info.
         */
        NOVICE(ZoneBorders(2660, 2638, 2663, 2643), ZoneBorders(2658, 2635, 2656, 2646), 14315),

        /**
         * Intermediate lander boat info.
         */
        INTERMEDIATE(ZoneBorders(2638, 2642, 2641, 2647), ZoneBorders(2645, 2639, 2643, 2652), 25631),

        /**
         * Veteran lander boat info.
         */
        VETERAN(ZoneBorders(2632, 2649, 2635, 2654), ZoneBorders(2638, 2652, 2638, 2655), 25632),
    }

    /**
     * Checks if the given location is inside any lander's playable area.
     *
     * @param l The location to check.
     * @return True if inside a lander, false otherwise.
     */
    fun landerContainsLoc(l: Location?): Boolean {
        for (i in BoatInfo.values()) if (i.boatBorder.insideBorder(l)) return true
        return false
    }

    /**
     * Checks if the given location is outside the gangplank area.
     *
     * @param l The location to check.
     * @return True if outside a lander gangplank, false otherwise.
     */
    fun outsideGangplankContainsLoc(l: Location?): Boolean {
        for (i in BoatInfo.values()) if (i.outsideBoatBorder.insideBorder(l)) return true
        return false
    }

    /**
     * Duplicate function to check if a location is inside any lander's playable area.
     * (Possible legacy or alternative usage.)
     *
     * @param l The location to check.
     * @return True if inside a lander, false otherwise.
     */
    fun landerContainsLoc2(l: Location?): Boolean {
        for (n in BoatInfo.values()) if (n.boatBorder.insideBorder(l)) return true
        return false
    }

    /**
     * Duplicate function to check if a location is outside the gangplank area.
     * (Possible legacy or alternative usage.)
     *
     * @param l The location to check.
     * @return True if outside a lander gangplank, false otherwise.
     */
    fun outsideGangplankContainsLoc2(l: Location?): Boolean {
        for (n in BoatInfo.values()) if (n.outsideBoatBorder.insideBorder(l)) return true
        return false
    }

    /**
     * Retrieves the Pest Control session for a [PestControlNoviceBot].
     *
     * @param p The novice bot instance.
     * @return The corresponding [PestControlSession], or null if not found.
     */
    fun getMyPestControlSession1(p: PestControlNoviceBot): PestControlSession? =
        p.getExtension(PestControlSession::class.java)

    /**
     * Retrieves the Pest Control session for a [PestControlIntermediateBot].
     *
     * @param p The intermediate bot instance.
     * @return The corresponding [PestControlSession], or null if not found.
     */
    fun getMyPestControlSession2(p: PestControlIntermediateBot): PestControlSession? =
        p.getExtension(PestControlSession::class.java)
}
