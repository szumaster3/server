package content.region.other.keldagrim.plugin

import content.data.GameAttributes
import content.minigame.blastfurnace.dialogue.BlastFusionHammerDialogue
import core.api.*
import core.game.dialogue.SequenceDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.*

class KeldagrimPlugin : InteractionListener {

    init {
        /*
         * Adds minecarts at the Keldagrim station.
         */
        addScenery(Scenery.TRAIN_CART_7028, Location.create(2914, 10173, 0), 0, 10)
        addScenery(Scenery.TRAIN_CART_7028, Location.create(2916, 10175, 0), 0, 10)
        addScenery(Scenery.TRAIN_CART_7028, Location.create(2918, 10175, 0), 0, 10)
        addScenery(Scenery.TRAIN_CART_7028, Location.create(2920, 10175, 0), 0, 10)
        addScenery(Scenery.TRAIN_CART_7028, Location.create(2922, 10175, 0), 0, 10)
        addScenery(Scenery.TRAIN_CART_7028, Location.create(2924, 10175, 0), 0, 10)
    }

    companion object {
        // Scenery.
        private val ENTRANCE = intArrayOf(Scenery.CAVE_ENTRANCE_5973, Scenery.ENTRANCE_5998)
        private const val DOORWAY_1 = Scenery.DOORWAY_23286
        private const val DOORWAY_2 = Scenery.DOORWAY_23287
        private const val REINALD = NPCs.REINALD_2194
        private const val FUSION_HAMMER = Items.BLAST_FUSION_HAMMER_14478
        private const val FOREMAN = NPCs.BLAST_FURNACE_FOREMAN_2553
        private const val TUNNEL = Scenery.TUNNEL_5014
        private const val INN_KEEPER = NPCs.INN_KEEPER_2176

        // Cart travel system.
        private val optionsWithQuest = arrayOf("To the Grand Exchange", "To Ice Mountain", "To White Wolf Mountain", "Stay here")
        private val optionsWithoutQuest = arrayOf("To the Grand Exchange", "To Ice Mountain", "Stay here")

        private val CART_SCENERY = intArrayOf(
            Scenery.HIDDEN_TRAPDOOR_28094,
            Scenery.TRAIN_CART_7028,
            Scenery.TRAIN_CART_7029,
            Scenery.TRAIN_CART_7030
        )
        private val destinations = arrayOf(
            Location.create(3140, 3507, 0), // Grand Exchange
            Location.create(2997, 9837, 0), // Ice Mountain
            Location.create(2875, 9871, 0)  // White Wolf Mountain
        )

        private fun startTravelToKeldagrim(player: Player) {
            if (hasRequirement(player, Quests.THE_GIANT_DWARF)) {
                submitWorldPulse(TravelToKeldagrimPulse(player))
            }
        }

        private fun startTravelFromKeldagrim(player: Player, dest: Location) {
            if (hasRequirement(player, Quests.THE_GIANT_DWARF)) {
                submitWorldPulse(TravelFromKeldagrimPulse(player, dest))
            }
        }

        private class TravelFromKeldagrimPulse(val player: Player, val dest: Location) : Pulse() {
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

    override fun defineListeners() {
        on(CART_SCENERY, IntType.SCENERY, "open", "ride") { player, node ->
            val questDone = isQuestComplete(player, Quests.FISHING_CONTEST)
            val options = if (questDone) optionsWithQuest else optionsWithoutQuest

            if (!getAttribute(player, GameAttributes.MINECART_TRAVEL_UNLOCK, false)) {
                sendMessage(player, "You must visit Keldagrim to use this shortcut.")
                return@on true
            }

            SequenceDialogue.dialogue(player) {
                if (node.id == Scenery.HIDDEN_TRAPDOOR_28094) {
                    message(
                        "This trapdoor leads to a small dwarven mine cart station. The mine",
                        "cart will take you to Keldagrim."
                    )
                }
                if (node.id == Scenery.TRAIN_CART_7028) {
                    options("What would you like to do?", *options) { choice ->
                        when {
                            choice == options.size -> closeDialogue(player)
                            choice == 1 -> startTravelFromKeldagrim(player, destinations[0])
                            choice == 2 -> startTravelFromKeldagrim(player, destinations[1])
                            questDone && choice == 3 -> startTravelFromKeldagrim(player, destinations[2])
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

        /*
         * Handles entering through doorway.
         */

        on(DOORWAY_1, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2941, 10179, 0))
            return@on true
        }
        on(DOORWAY_2, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2435, 5535, 0))
            return@on true
        }

        /*
         * Handles changing armguards.
         */

        on(REINALD, IntType.NPC, "change-armguards") { player, _ ->
            openInterface(player, Components.REINALD_SMITHING_EMPORIUM_593)
            return@on true
        }

        /*
         * Handles using Fusion Hammer on Foreman NPC.
         */

        onUseWith(IntType.NPC, FUSION_HAMMER, FOREMAN) { player, _, npc ->
            openDialogue(player, BlastFusionHammerDialogue(), npc)
            return@onUseWith true
        }

        /*
         * Handles entering a cave entrance.
         */

        on(ENTRANCE, IntType.SCENERY, "go-through") { player, node ->
            teleport(
                player,
                if (node.id == Scenery.CAVE_ENTRANCE_5973) {
                    Location(2838, 10125)
                } else {
                    Location(2780, 10161)
                },
                TeleportManager.TeleportType.INSTANT,
                1,
            )
            sendMessage(player, "You're just about able to squeeze through.")
            return@on true
        }

        /*
         * Handles searching a bookcase.
         */

        on(Scenery.BOOKCASE_6091, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the books...")
            when {
                inInventory(player, Items.EXPLORERS_NOTES_11677) -> {
                    sendMessage(player, "You find nothing of interest to you.", 1)
                }
                freeSlots(player) == 0 -> {
                    sendMessage(player, "You need at least one free inventory space to take from the shelves.")
                }
                else -> {
                    sendMessage(player, "...and find a book named 'Explorer's Notes'.")
                    addItemOrDrop(player, Items.EXPLORERS_NOTES_11677)
                }
            }
            return@on true
        }

        /*
         * Handles entering the tunnel.
         */

        on(TUNNEL, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2730, 3713, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        /*
         * Overrides the destination for InnKeeper NPC.
         */

        setDest(IntType.NPC, intArrayOf(INN_KEEPER), "talk-to") { _, _ ->
            return@setDest Location.create(2843, 10193, 1)
        }
    }
}