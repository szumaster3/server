package content.global.travel.charter

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

class CharterListener : InteractionListener, InterfaceListener {

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
