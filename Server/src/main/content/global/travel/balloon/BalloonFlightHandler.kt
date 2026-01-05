package content.global.travel.balloon

import content.data.GameAttributes
import content.global.travel.balloon.dialogue.AssistantDialogue
import content.global.travel.balloon.utils.FlightUtils
import core.api.isQuestComplete
import core.api.openDialogue
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import shared.consts.Components
import shared.consts.Quests
import shared.consts.Scenery

/**
 * Handles the balloon travel system.
 */
class BalloonFlightHandler : InterfaceListener, InteractionListener {

    companion object {
        /**
         * Represents the assistant npc.
         */
        private val ASSISTANT_NPC_IDS = intArrayOf(
            5062,
            5063,
            5064,
            5065,
            5066
        )

        /**
         * Represents the basket ids.
         */
        private val BASKET_OBJECT_IDS = intArrayOf(Scenery.BASKET_19128, Scenery.BASKET_19129)

    }

    override fun defineInterfaceListeners() {
        on(Components.ZEP_BALLOON_MAP_469) { player, _, _, buttonID, _, _ ->

            val destination = BalloonDefinition.fromButtonId(buttonID)
                ?: return@on true

            val origin = player.getAttribute<BalloonDefinition>(
                GameAttributes.BALLOON_ORIGIN
            )

            if (!FlightUtils.canFly(player, origin, destination)) {
                return@on true
            }

            if (FlightUtils.unlockNewLocation(player, destination)) {
                return@on true
            }

            FlightUtils.payForFlight(player, origin, destination) {
                FlightUtils.startFlight(player, destination)
            }

            return@on true
        }
    }

    override fun defineListeners() {

        /*
         * Handles interaction with basket scenery.
         */

        on(BASKET_OBJECT_IDS, IntType.SCENERY, "use") { player, node ->
            val sceneryId = node.asScenery().wrapper.id
            val location = BalloonDefinition.fromSceneryId(sceneryId)
            if (location != null) {
                FlightUtils.openFlightMap(player, location)
            }
            return@on true
        }

        /*
         * Handles talking to service NPCs.
         */

        on(ASSISTANT_NPC_IDS, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, AssistantDialogue(), node)
            return@on true
        }

        /*
         * Handles fly option for service NPCs.
         */

        on(ASSISTANT_NPC_IDS, IntType.NPC, "Fly") { player, node ->
            if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY)) {
                sendMessage(player, "You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.")
                return@on true
            }

            val location = BalloonDefinition.fromNpcId(node.id)
            if (location != null) {
                FlightUtils.openFlightMap(player, location)
            }
            return@on true
        }
    }
}
