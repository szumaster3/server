package content.region.karamja.shilo_village.plugin

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.*

class ShiloVillagePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles drop of notes.
         */

        on(NOTES, IntType.ITEM, "drop") { player, node ->
            removeItem(player, node.asItem())
            sendMessage(player, "As you drop the delicate scrolls onto the floor, they disintegrate immediately.")
            return@on true
        }

        /*
         * Handles furnace doors.
         */

        on(BLACKSMITH_DOOR, IntType.SCENERY, "open") { player, node ->
            if (!getAttribute(player, "shilo-village:blacksmith-doors", false)) {
                sendNPCDialogue(
                    player,
                    NPCs.YOHNUS_513,
                    "Sorry but the blacksmiths is closed. But I can let you use the furnace at the cost of 20 gold pieces."
                )
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        /*
         * Handles enter the shilo village.
         */

        on(Scenery.BROKEN_CART_2216, IntType.SCENERY, "look-at") { player, _ ->
            if (!hasRequirement(player, Quests.SHILO_VILLAGE)) return@on false

            val playerX = player.location.x
            val location = if (playerX > 2878) Location(2876, 2952) else Location(2880, 2952)

            lock(player, 1)
            sendMessage(player, "You climb up onto the cart.")
            sendMessage(player, "You nimbly jump from one side of the cart...")
            player.impactHandler.disabledTicks = 4
            queueScript(player, 1, QueueStrength.WEAK) {
                unlock(player)
                teleport(player, location)
                sendMessage(player, "...to the other and climb down again.")
                resetAnimator(player)
                return@queueScript stopExecuting(player)

            }
            return@on true
        }

        /*
         * Handles travel between Shilo Village & Brimhaven.
         */

        on(Scenery.TRAVEL_CART_2230, IntType.SCENERY, "pay-fare") { player, _ ->
            if (!hasRequirement(player, Quests.SHILO_VILLAGE)) return@on false
            quickTravel(player, NPCs.HAJEDY_510)
            return@on true
        }

        on(Scenery.TRAVEL_CART_2230, IntType.SCENERY, "board") { player, _ ->
            val npc = findNPC(NPCs.HAJEDY_510)
            openDialogue(player, NPCs.HAJEDY_510)
            face(player, npc!!, 3)
            return@on true
        }

        on(NPCs.HAJEDY_510, IntType.NPC, "pay-fare") { player, _ ->
            if (!hasRequirement(player, Quests.SHILO_VILLAGE)) return@on false
            quickTravel(player, NPCs.HAJEDY_510)
            return@on true
        }

        on(Scenery.TRAVEL_CART_2265, IntType.SCENERY, "pay-fare") { player, _ ->
            if (!hasRequirement(player, Quests.SHILO_VILLAGE)) return@on false
            quickTravel(player, NPCs.VIGROY_511)
            return@on true
        }

        on(Scenery.TRAVEL_CART_2265, IntType.SCENERY, "board") { player, _ ->
            val npc = findNPC(NPCs.VIGROY_511)
            openDialogue(player, NPCs.VIGROY_511)
            face(player, npc!!, 3)
            return@on true
        }

        on(NPCs.VIGROY_511, IntType.NPC, "pay-fare") { player, npc ->
            if (!hasRequirement(player, Quests.SHILO_VILLAGE)) return@on false
            quickTravel(player, npc.id)
            return@on true
        }

        /*
         * Handles opening the wooden gate when entering or leaving Shilo Village.
         */

        on(WOODEN_GATE, IntType.SCENERY, "open") { player, node ->
            val nextDoorId = if (player.location.y == 2952) node.id - 1 else node.id + 1
            DoorActionHandler.autowalkFence(player, node.asScenery(), node.id, nextDoorId)
            if (player.location.x == 2867) sendMessage(player, "You make your way out of Shilo Village.")
            return@on true
        }

        /*
         * Handles opening the metal gate near the Shilo Village exit.
         */

        on(METAL_GATE, IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            if (player.location.x == 2874) {
                sendMessage(player, "You open the gates and make your way back out of the village.")
            }
            return@on true
        }
    }

    companion object {
        private const val NOTES = Items.BERVIRIUS_NOTES_624
        private const val BLACKSMITH_DOOR = Scenery.BLACKSMITH_S_DOOR_2266
        private val WOODEN_GATE = intArrayOf(Scenery.WOODEN_GATE_2261, Scenery.WOODEN_GATE_2262)
        private val METAL_GATE = intArrayOf(Scenery.METAL_GATE_2259, Scenery.METAL_GATE_2260)

        /**
         * Handles quick travel between shilo village and brimhaven.
         *
         * @param player The player who is traveling.
         * @param npc The NPC id that the player is interacting with.
         */
        @JvmStatic
        fun quickTravel(player: Player, npc: Int) {
            if (!hasRequirement(player, Quests.SHILO_VILLAGE)) return
            val isShilo = npc == NPCs.HAJEDY_510
            val npcName = getNPCName(npc)

            if (!inInventory(player, Items.COINS_995, 10)) {
                sendMessage(player, "You don't have enough coins.")
                return
            }

            lock(player, 6)
            closeDialogue(player)
            sendDialogueLines(player, "You pay the fare and hand 10 gold coins to $npcName.")
            addDialogueAction(player) { _, _ ->
                removeItem(player, Item(Items.COINS_995, 10))
                openOverlay(player, Components.FADE_TO_BLACK_120)
                queueScript(player, 5, QueueStrength.SOFT) {
                    val destination = if (isShilo) Location.create(2834, 2951, 0) else Location.create(2780, 3212, 0)
                    teleport(player, destination)
                    closeOverlay(player)
                    openOverlay(player, Components.FADE_FROM_BLACK_170)
                    sendDialogueLines(player, "You feel tired from the journey, but at least you didn't have to walk", "all that distance.")
                    return@queueScript stopExecuting(player)
                }
            }
        }
    }
}