package content.global.travel

import content.data.GameAttributes
import core.api.*
import core.api.quest.isQuestComplete
import core.api.ui.closeDialogue
import core.api.ui.setMinimapState
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

/**
 * Handles Keldagrim minecart travel system interactions.
 */
class KeldagrimMinecartListener : InteractionListener {

    private val optionsWithQuest = arrayOf("To the Grand Exchange", "To Ice Mountain", "To White Wolf Mountain", "Stay here")
    private val optionsWithoutQuest = arrayOf("To the Grand Exchange", "To Ice Mountain", "Stay here")

    private val sceneryId = intArrayOf(Scenery.HIDDEN_TRAPDOOR_28094, Scenery.TRAIN_CART_7028, Scenery.TRAIN_CART_7029, Scenery.TRAIN_CART_7030)
    private val destinations = mapOf(
        "GE" to Location.create(3140, 3507, 0),
        "Mine" to Location.create(2997, 9837, 0),
        "Sindarpos" to Location.create(2875, 9871, 0)
    )

    override fun defineListeners() {
        on(sceneryId, IntType.SCENERY, "open", "ride") { player, node ->
            val questDone = isQuestComplete(player, Quests.FISHING_CONTEST)
            val options = if (questDone) optionsWithQuest else optionsWithoutQuest

            if (!getAttribute(player, GameAttributes.MINECART_TRAVEL_UNLOCK, false)) {
                sendMessage(player, "You must visit Keldagrim to use this shortcut.")
                return@on true
            }

            dialogue(player) {
                if (node.id == Scenery.HIDDEN_TRAPDOOR_28094) {
                    message("This trapdoor leads to a small dwarven mine cart station. The mine", "cart will take you to Keldagrim.")
                }
                if (node.id == Scenery.TRAIN_CART_7028) {
                    options("What would you like to do?", *options) { choice ->
                        when {
                            choice == options.size -> closeDialogue(player)
                            choice == 1 -> startTravelFromKeldagrim(player, destinations.getValue("GE"))
                            choice == 2 -> startTravelFromKeldagrim(player, destinations.getValue("Mine"))
                            questDone && choice == 3 -> startTravelFromKeldagrim(player, destinations.getValue("Sindarpos"))
                            else -> closeDialogue(player)
                        }
                    }
                } else {
                    options("What would you like to do?", "Travel to Keldagrim", "Stay here") { choice ->
                        if (choice == 1) startTravelToKeldagrim(player) else closeDialogue(player)
                    }
                }
            }
            return@on true
        }

    }

    companion object {
        private fun startTravelToKeldagrim(player: Player) {
            if (core.api.quest.hasRequirement(player, Quests.THE_GIANT_DWARF)) {
                submitWorldPulse(TravelToKeldagrimPulse(player))
            }
        }

        private fun startTravelFromKeldagrim(player: Player, dest: Location) {
            if (core.api.quest.hasRequirement(player, Quests.THE_GIANT_DWARF)) {
                submitWorldPulse(TravelFromKeldagrimPulse(player, dest))
            }
        }

        private class TravelFromKeldagrimPulse(
            val player: Player,
            val dest: Location
        ) : Pulse() {
            private var counter = 0

            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> startTravel()
                    4 -> player.teleportWithCart(Location.create(2911, 10171, 0), true)
                    5 -> player.moveCartTo(2936, 10171)
                    6 -> fadeToNormal()
                    14 -> fadeToBlack()
                    21 -> player.teleportWithCart(dest, false)
                    23 -> fadeToNormal()
                    25 -> return finishTravel()
                }
                return false
            }

            private fun startTravel() {
                lock(player, 25)
                openInterface(player, Components.FADE_TO_BLACK_120)
                setMinimapState(player, 2)
            }

            private fun fadeToNormal() {
                closeInterface(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
            }

            private fun fadeToBlack() {
                openInterface(player, Components.FADE_TO_BLACK_120)
            }

            private fun finishTravel(): Boolean {
                unlock(player)
                setMinimapState(player, 0)
                closeInterface(player)
                return true
            }
        }

        private class TravelToKeldagrimPulse(val player: Player) : Pulse() {
            private var counter = 0
            private val cartNPC = NPC(NPCs.MINE_CART_1544)

            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> startTravel()
                    6 -> player.teleportWithCart(Location.create(2943, 10170, 0), true)
                    7 -> player.moveCartTo(2939, 10173)
                    8 -> player.moveCartTo(2914, 10173)
                    10 -> fadeToNormal()
                    23 -> finalizeTravel()
                    33 -> {
                        cartNPC.clear()
                        return true
                    }
                }
                return false
            }

            private fun startTravel() {
                lock(player, 20)
                openInterface(player, Components.FADE_TO_BLACK_115)
                setMinimapState(player, 2)
            }

            private fun fadeToNormal() {
                closeInterface(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
            }

            private fun finalizeTravel() {
                closeInterface(player)
                setMinimapState(player, 0)
                unlock(player)
                player.appearance.rideCart(false)
                cartNPC.location = player.location
                cartNPC.direction = Direction.WEST
                cartNPC.init()
                player.properties.teleportLocation = player.location.transform(0, 1, 0)
            }
        }

        private fun Player.teleportWithCart(location: Location, ride: Boolean) {
            properties.teleportLocation = location
            appearance.rideCart(ride)
        }

        private fun Player.moveCartTo(x: Int, y: Int) {
            walkingQueue.reset()
            walkingQueue.addPath(x, y)
        }
    }
}