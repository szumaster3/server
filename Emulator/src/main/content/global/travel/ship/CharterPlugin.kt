package content.global.travel.ship

import core.api.*
import core.api.quest.isQuestComplete
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class CharterPlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        on(SAILORS, IntType.NPC, "charter") { player, _ ->
            CharterUtils.open(player)
            return@on true
        }

        /*
         * Handles ladder on Dragon slayer ship.
         */

        on(Scenery.LADDER_2592, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.USE_LADDER_828), Location(3048, 3208, 1))
            return@on true
        }

        /*
         * Handles ladder on Dragon slayer ship.
         */

        on(Scenery.LADDER_2590, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(3048, 9640, 1))
            return@on true
        }

        on(Scenery.GANGPLANK_69, IntType.SCENERY, "cross") { player, _ ->
            player.dialogueInterpreter.open(NPCs.ARHEIN_563, findNPC(NPCs.ARHEIN_563), true)
            return@on true
        }

        on(Scenery.GANGPLANK_2593, IntType.SCENERY, "cross") { player, _ ->
            if (isQuestComplete(player, Quests.DRAGON_SLAYER)) {
                player.dialogueInterpreter.open(NPCs.KLARENSE_744, findNPC(NPCs.KLARENSE_744), true)
                return@on true
            }
            if (!player.getSavedData().questData.getDragonSlayerAttribute("ship")) {
                openDialogue(player, NPCs.KLARENSE_744, findNPC(NPCs.KLARENSE_744)!!, true)
            } else {
                sendMessage(player, "You board the ship.")
                cross(player, Location(3047, 3207, 1))
            }
            return@on true
        }

        on(Scenery.GANGPLANK_11209, IntType.SCENERY, "cross") { player, _ ->
            sendDialogueLines(player, "I don't think that whoever owns this ship will be happy", "with me wandering all over it.")
            return@on true
        }

        on(GANGPLANKS, IntType.SCENERY, "cross") { player, node ->
            handleGangplankCrossing(player, node)
            return@on true
        }

    }

    companion object {
        /**
         * Represents the gangplank scenery objects.
         */
        private val GANGPLANKS = intArrayOf(Scenery.GANGPLANK_2081, Scenery.GANGPLANK_2082, Scenery.GANGPLANK_2083, Scenery.GANGPLANK_2084, Scenery.GANGPLANK_2085, Scenery.GANGPLANK_2086, Scenery.GANGPLANK_2087, Scenery.GANGPLANK_2088, Scenery.GANGPLANK_2412, Scenery.GANGPLANK_2413, Scenery.GANGPLANK_2414, Scenery.GANGPLANK_2415, Scenery.GANGPLANK_2594, Scenery.GANGPLANK_11211, Scenery.GANGPLANK_11212, Scenery.GANGPLANK_14304, Scenery.GANGPLANK_14305, Scenery.GANGPLANK_14306, Scenery.GANGPLANK_14307, Scenery.GANGPLANK_17392, Scenery.GANGPLANK_17393, Scenery.GANGPLANK_17394, Scenery.GANGPLANK_17395, Scenery.GANGPLANK_17398, Scenery.GANGPLANK_17399, Scenery.GANGPLANK_17400, Scenery.GANGPLANK_17401, Scenery.GANGPLANK_17402, Scenery.GANGPLANK_17403, Scenery.GANGPLANK_17404, Scenery.GANGPLANK_17405, Scenery.GANGPLANK_17406, Scenery.GANGPLANK_17407, Scenery.GANGPLANK_17408, Scenery.GANGPLANK_17409, Scenery.GANGPLANK_29168, Scenery.GANGPLANK_29169)

        /**
         * Represents the sailors.
         */
        private val SAILORS = intArrayOf(NPCs.TRADER_STAN_4650, NPCs.TRADER_CREWMEMBER_4651, NPCs.TRADER_CREWMEMBER_4652, NPCs.TRADER_CREWMEMBER_4653, NPCs.TRADER_CREWMEMBER_4654, NPCs.TRADER_CREWMEMBER_4655, NPCs.TRADER_CREWMEMBER_4656)

        /**
         * Handles the player crossing a gangplank.
         */
        private fun handleGangplankCrossing(player: Player, node: Node) {
            lock(player, 1)
            forceMove(player, player.location, node.location, 0, 60, null)

            val data = Gangplank.forId(node.id)
            if (data != null) {
                data.message?.let { sendMessage(player, it) }
                cross(player, data.destination)
            } else {
                sendDialogueLines(player, "I don't think that whoever owns this ship will be happy", "with me wandering all over it.")
            }
        }

        /**
         * Teleports the player.
         */
        private fun cross(player: Player, location: Location?, ) {
            queueScript(player, 2, QueueStrength.STRONG) {
                player.properties.teleportLocation = location
                return@queueScript stopExecuting(player)
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.SAILING_TRANSPORT_WORLD_MAP_95) { player, _, _, buttonID, _, _ ->
            CharterUtils.handle(player, buttonID)
            return@on true
        }
    }
}

/**
 * Represents gangplank objects used for boarding
 * and disembarking ships in various locations.
 */
private enum class Gangplank(val objectId: Int, val destination: Location, val message: String? = null) {
    ARDOUGNE_1(Scenery.GANGPLANK_2085, Location(2683, 3268, 1), "You must speak to Captain Barnaby before it will set sail."), ARDOUGNE_2(Scenery.GANGPLANK_2086, Location(2683, 3271, 0)),
    BRIMHAVEN_1(Scenery.GANGPLANK_2087, Location(2775, 3234, 1), "You must speak to the Customs Officer before it will set sail."), BRIMHAVEN_2(Scenery.GANGPLANK_2088, Location(2772, 3234, 0)), BRIMHAVEN_3(Scenery.GANGPLANK_17400, Location(2763, 3238, 1)), BRIMHAVEN_4(Scenery.GANGPLANK_17401, Location(2760, 3238, 0)),
    CATHERBY_1(Scenery.GANGPLANK_17394, Location(2792, 3417, 1)), CATHERBY_2(Scenery.GANGPLANK_17395, Location(2792, 3414, 0)),
    ENTRANA_1(Scenery.GANGPLANK_2414, Location(2834, 3331, 1)), ENTRANA_2(Scenery.GANGPLANK_2415, Location(2834, 3335, 0)),
    KARAMJA_1(Scenery.GANGPLANK_2081, Location(2956, 3143, 1)), KARAMJA_2(Scenery.GANGPLANK_2082, Location(2956, 3146, 0)), KARAMJA_3(Scenery.GANGPLANK_17398, Location(2957, 3158, 1), "You must speak to the Customs Officer before it will set sail."), KARAMJA_4(Scenery.GANGPLANK_17399, Location(2954, 3158, 0), "You must speak to the Customs Officer before it will set sail."),
    MOS_LE_HARMLESS_1(Scenery.GANGPLANK_17406, Location(3668, 2931, 1)), MOS_LE_HARMLESS_2(Scenery.GANGPLANK_17407, Location(3671, 2931, 0)),
    MOSS_1(Scenery.GANGPLANK_11211, Location(3684, 2950, 1)), MOSS_2(Scenery.GANGPLANK_11212, Location(3684, 2953, 0)),
    OO_GLOG_1(Scenery.GANGPLANK_29168, Location(2626, 2857, 1)), OO_GLOG_2(Scenery.GANGPLANK_29169, Location(2623, 2857, 0)),
    PEST_CONTROL_1(Scenery.GANGPLANK_14306, Location(2662, 2676, 1), "You board the ship."), PEST_CONTROL_2(Scenery.GANGPLANK_14307, Location(2659, 2676, 0), "You disembark the ship."),
    PORT_KHAZARD_1(Scenery.GANGPLANK_17402, Location(2674, 3141, 1)), PORT_KHAZARD_2(Scenery.GANGPLANK_17403, Location(2674, 3144, 0)),
    PORT_PHASMATYS_1(Scenery.GANGPLANK_17392, Location(3705, 3503, 1)), PORT_PHASMATYS_2(Scenery.GANGPLANK_17393, Location(3702, 3503, 0)),
    PORT_SARIM_1(Scenery.GANGPLANK_2083, Location(3032, 3217, 1)), PORT_SARIM_2(Scenery.GANGPLANK_2084, Location(3029, 3217, 0)), PORT_SARIM_3(Scenery.GANGPLANK_2412, Location(3048, 3231, 1)), PORT_SARIM_4(Scenery.GANGPLANK_2413, Location(3048, 3234, 0)), PORT_SARIM_5(Scenery.GANGPLANK_2594, Location(3047, 3204, 0)), PORT_SARIM_6(Scenery.GANGPLANK_14304, Location(3041, 3199, 1), "You board the ship."), PORT_SARIM_7(Scenery.GANGPLANK_14305, Location(3041, 3202, 0), "You disembark the ship."), PORT_SARIM_8(Scenery.GANGPLANK_17404, Location(3038, 3189, 1)), PORT_SARIM_9(Scenery.GANGPLANK_17405, Location(3038, 3192, 0)),
    SHIPYARD_1(Scenery.GANGPLANK_17396, Location(2998, 3032, 1)), SHIPYARD_2(Scenery.GANGPLANK_17397, Location(3000, 3032, 0)),
    TYRAS_CAMP_1(Scenery.GANGPLANK_17408, Location(2142, 3125, 1)), TYRAS_CAMP_2(Scenery.GANGPLANK_17409, Location(2142, 3122, 0)),
    ;

    companion object {
        private val map = values().associateBy { it.objectId }
        fun forId(id: Int): Gangplank? = map[id]
    }
}