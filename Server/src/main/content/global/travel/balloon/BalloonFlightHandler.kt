package content.global.travel.balloon

import content.data.GameAttributes
import content.global.travel.balloon.dialogue.AssistantDialogue
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.info.Rights
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

/** Handles the balloon travel system. */
class BalloonFlightHandler : InterfaceListener, InteractionListener {

    companion object {
        /**
         * Represents the assistant npc.
         */
        private val BALLOON_SERVICE_NPC_IDS = intArrayOf(NPCs.AUGUSTE_5050, NPCs.ASSISTANT_SERF_5053, NPCs.ASSISTANT_BROCK_5054, NPCs.ASSISTANT_MARROW_5055, NPCs.ASSISTANT_LE_SMITH_5056, NPCs.ASSISTANT_STAN_5057, 5063, 5065)

        /**
         * Represents the basket ids.
         */
        private val BASKET_OBJECT_IDS = intArrayOf(Scenery.BASKET_19128, Scenery.BASKET_19129)

        /**
         * Represents assistant npcs and their spawn locations.
         */
        private val assistants = mapOf(
            NPCs.ASSISTANT_SERF_5053 to Location.create(3298, 3484, 0),
            NPCs.ASSISTANT_LE_SMITH_5056 to Location.create(2480, 3458, 0),
            NPCs.ASSISTANT_STAN_5057 to Location.create(2938, 3424, 0)
        )
    }

    init {
        assistants.forEach { (npcId, location) ->
            NPC(npcId, location).apply {
                init()
                isWalks = true
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.ZEP_BALLOON_MAP_469) { player, _, _, buttonID, _, _ ->
            val destination = BalloonDefinition.fromButtonId(buttonID) ?: return@on true
            val isAdmin = player.rights == Rights.ADMINISTRATOR
            val origin = player.getAttribute<BalloonDefinition>(GameAttributes.BALLOON_ORIGIN)

            if (!hasLevelStat(player, Skills.FIREMAKING, destination.requiredLevel)) {
                sendDialogue(player, "You require a Firemaking level of ${destination.requiredLevel} to travel to ${destination.destName}.")
                return@on true
            }

            if (origin == destination) {
                sendDialogue(player, "You can't fly to the same location.")
                return@on true
            }

            if (player.familiarManager.hasFamiliar() || player.familiarManager.hasPet()) {
                sendMessage(player, "You can't take a follower or pet on a ride.")
                return@on true
            }

            if (player.settings.weight > 40.0) {
                sendDialogue(player, "You're carrying too much weight to fly. Try reducing your weight below 40 kg.")
                return@on true
            }

            if (destination == BalloonDefinition.ENTRANA && !isAdmin) {
                if (!ItemDefinition.canEnterEntrana(player)) {
                    sendDialogue(player, "You can't take flight with weapons and armour to Entrana.")
                    return@on true
                }
                sendMessage(player, "You are quickly searched.")
            }

            if (isAdmin) {
                BalloonUtils.startFlight(player, destination)
                return@on true
            }

            val routeId = when (destination) {
                BalloonDefinition.TAVERLEY -> 1
                BalloonDefinition.CRAFT_GUILD -> 2
                BalloonDefinition.VARROCK -> 3
                BalloonDefinition.CASTLE_WARS -> 4
                BalloonDefinition.GRAND_TREE -> 5
                BalloonDefinition.ENTRANA -> -1
            }

            val isUnlocked = getVarbit(player, destination.varbitId) == 1
            if (!isUnlocked && destination != BalloonDefinition.ENTRANA) {
                closeInterface(player)
                setAttribute(player, "zep_current_route", routeId)
                setAttribute(player, "zep_current_step_$routeId", 1)
                openInterface(player, Components.ZEP_INTERFACE_470)
                return@on true
            }

            if (!removeItem(player, Item(destination.logId, 1))) {
                val requiredItem = getItemName(destination.logId).lowercase().removeSuffix("s").trim()
                sendDialogue(player, "You need at least one $requiredItem.")
                return@on true
            }

            BalloonUtils.startFlight(player, destination)
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
                BalloonUtils.openBalloonFlightMap(player, location)
            }
            return@on true
        }

        /*
         * Handles talking to service NPCs.
         */

        on(BALLOON_SERVICE_NPC_IDS, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, AssistantDialogue(), node)
            return@on true
        }

        /*
         * Handles fly option for service NPCs.
         */

        on(BALLOON_SERVICE_NPC_IDS + 5049, IntType.NPC, "Fly") { player, node ->
            if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY)) {
                sendMessage(player, "You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.")
                return@on true
            }

            val location = BalloonDefinition.fromNpcId(node.id)
            if (location != null) {
                BalloonUtils.openBalloonFlightMap(player, location)
            }
            return@on true
        }
    }
}
