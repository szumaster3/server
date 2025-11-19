package content.region.wilderness.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.*

class WildernessPlugin : InteractionListener {

    companion object {
        val MUDDY_CHEST_LOOT = arrayOf(Item(Items.UNCUT_RUBY_1619), Item(Items.MITHRIL_BAR_2359), Item(Items.MITHRIL_DAGGER_1209), Item(Items.ANCHOVY_PIZZA_2297), Item(Items.LAW_RUNE_563, 2), Item(Items.DEATH_RUNE_560, 2), Item(Items.CHAOS_RUNE_562, 10), Item(Items.COINS_995, 50))
        private val WILDERNESS_LADDER = intArrayOf(Scenery.LEVER_1814, Scenery.LEVER_1815, Scenery.LEVER_5959, Scenery.LEVER_5960, Scenery.LEVER_9706, Scenery.LEVER_9707)
        private val KBD_LADDER = intArrayOf(Scenery.LEVER_1816, Scenery.LEVER_1817)
    }

    private fun teleport(player: Player, location: Location) {
        player.teleporter.send(location, TeleportType.NORMAL, TeleportManager.WILDERNESS_TELEPORT)
    }

    override fun defineListeners() {
        on(Scenery.CLOSED_CHEST_170, IntType.SCENERY, "open") { player, node ->
            val key = Item(Items.MUDDY_KEY_991)
            if (!removeItem(player, key)) {
                sendMessage(player, "This chest is locked and needs some sort of key.")
            } else {
                animate(player, Animations.HUMAN_OPEN_CHEST_536)
                replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_171, 3, node.direction, node.location)
                for (item in MUDDY_CHEST_LOOT) {
                    if (!addItem(player, item.id)) GroundItemManager.create(item, player)
                }
            }
            return@on true
        }

        on(Scenery.LADDER_1765, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location.create(3069, 10257, 0))
            return@on true
        }

        on(Scenery.LADDER_1767, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location.create(3017, 10250, 0))
            return@on true
        }


        /*
         * Handles interaction with wilderness ladders.
         */

        on(WILDERNESS_LADDER, IntType.SCENERY, "pull") { player, node ->
            when(node.id) {
                Scenery.LEVER_1814 -> {
                    openDialogue(player, WildernessLeverDialogue())
                }

                /*
                 * Other levers: mage bank, arena, etc.
                 */
                Scenery.LEVER_1815, Scenery.LEVER_5959, Scenery.LEVER_5960, Scenery.LEVER_9706, Scenery.LEVER_9707 -> {
                    if (!player.savedData.activityData.hasKilledKolodion() && node.id == Scenery.LEVER_9706) {
                        sendNPCDialogue(player, NPCs.KOLODION_905, "You're not allowed in there. Come downstairs if you want to enter my arena.")
                        return@on false
                    }

                    lock(player, 2)
                    animate(player, Animations.PULL_DOWN_LEVER_2140)
                    playAudio(player, Sounds.PULL_LEVER_GENERAL_2400)
                    sendMessage(player, "You pull the lever...")

                    submitWorldPulse(object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            if (!hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                                val destination = when (node.id) {
                                    Scenery.LEVER_1815 -> Location(2562, 3311, 0)
                                    Scenery.LEVER_5959 -> Location(2539, 4712, 0)
                                    Scenery.LEVER_5960 -> Location(3090, 3956, 0)
                                    Scenery.LEVER_9706 -> Location(3105, 3951, 0)
                                    else -> Location(3105, 3956, 0)
                                }
                                teleport(player, destination)
                                val message = when (node.id) {
                                    Scenery.LEVER_1815 -> "out of the wilderness."
                                    Scenery.LEVER_5959 -> "into the mage's cave."
                                    Scenery.LEVER_5960 -> "out of the mage's cave."
                                    Scenery.LEVER_9706 -> "out of the arena."
                                    else -> "into the arena."
                                }
                                sendMessage(player, "...And teleport $message")
                            } else {
                                sendMessage(player, "A magical force has stopped you from teleporting.")
                            }
                            return true
                        }
                    })
                }

                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles interaction with kbd ladders.
         */

        on(KBD_LADDER, IntType.SCENERY, "pull") { player, node ->
            if (!finishedMoving(player)) {
                return@on false
            }

            sendMessage(player, "You pull the lever...")
            animate(player, Animations.PULL_DOWN_LEVER_2140, false)
            playAudio(player, Sounds.PULL_LEVER_GENERAL_2400)

            when (node.id) {
                Scenery.LEVER_1816 -> {
                    if (node.location == Location.create(3067, 10252, 0)) {
                        if (player.zoneMonitor.isInZone("Wilderness") && hasTimerActive(
                                player, GameAttributes.TELEBLOCK_TIMER
                            )
                        ) {
                            sendMessage(player, "A magical force has stopped you from teleporting.")
                        } else {
                            sendMessage(player, "... and teleport into the lair of the King Black Dragon!", 5)
                            teleport(player, Location.create(2272, 4680, 0))
                        }
                    }
                }

                Scenery.LEVER_1817 -> {
                    teleport(player, Location.create(3067, 10253, 0))
                    sendMessage(player, "... and teleport out of the lair of the King Black Dragon!", 5)
                }
            }

            return@on true
        }


        on(Scenery.ENTRANCE_37749, IntType.SCENERY, "go-through") { player, _ ->
            teleport(player, Location.create(2885, 4372, 2), TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.EXIT_37928, IntType.SCENERY, "go-through") { player, _ ->
            teleport(player, Location.create(3214, 3782, 0), TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.TRAPDOOR_39188, IntType.SCENERY, "open") { player, _ ->
            if (!hasRequirement(player, Quests.DEFENDER_OF_VARROCK)) return@on false
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_DOWN,
                Location.create(3241, 9991, 0),
                "You descend into the cavern below.",
            )
            return@on true
        }

        on(Scenery.LADDER_39191, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_UP,
                Location.create(3239, 3606, 0),
                "You climb up the ladder to the surface.",
            )
            return@on true
        }

        on(Scenery.DOOR_39200, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door doesn't open.")
            return@on true
        }
    }

    inner class WildernessLeverDialogue : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogue(player!!, "Warning! Pulling the lever will teleport you deep into the wilderness.")
                    stage = 1
                }
                1 -> {
                    setTitle(player!!, 2)
                    options("Yes I'm brave.", "Eep! The wilderness... No thank you.")
                    stage = 2
                }
                2 -> {
                    when(buttonID) {
                        1 -> {
                            end()
                            lock(player!!, 2)
                            animate(player!!, Animations.PULL_DOWN_LEVER_2140)
                            playAudio(player!!, Sounds.PULL_LEVER_GENERAL_2400)
                            sendMessage(player!!, "You pull the lever...")
                            queueScript(player!!, 2, QueueStrength.WEAK) {
                                if (!hasTimerActive(player!!, GameAttributes.TELEBLOCK_TIMER)) {
                                    teleport(player!!, Location.create(3154, 3923, 0))
                                    sendMessage(player!!, "...And teleport into the wilderness.")
                                } else {
                                    sendMessage(player!!, "A magical force has stopped you from teleporting.")
                                }
                                return@queueScript stopExecuting(player!!)
                            }
                        }
                        2 -> end()
                    }
                }
            }
        }
    }
}