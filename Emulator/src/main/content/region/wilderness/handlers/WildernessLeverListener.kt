package content.region.wilderness.handlers

import content.data.GameAttributes
import core.api.*
import core.api.movement.finishedMoving
import core.api.ui.closeDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class WildernessLeverListener : InteractionListener {

    private val leverIDs = intArrayOf(
        Scenery.LEVER_1814,
        Scenery.LEVER_1815,
        Scenery.LEVER_5959,
        Scenery.LEVER_5960,
        Scenery.LEVER_9706,
        Scenery.LEVER_9707
    )

    private val kbdleverIDs = intArrayOf(Scenery.LEVER_1816, Scenery.LEVER_1817)

    private fun teleport(player: Player, location: Location) {
        player.teleporter.send(location, TeleportType.NORMAL, TeleportManager.WILDERNESS_TELEPORT)
    }

    override fun defineListeners() {

        /*
         * Handles interaction with wilderness ladders.
         */

        on(leverIDs, IntType.SCENERY, "pull") { player, node ->
            when (node.id) {
                Scenery.LEVER_1814 -> {
                    sendDialogue(player, "Warning! Pulling the lever will teleport you deep into the wilderness.")
                    addDialogueAction(player) { _, _ ->
                        setTitle(player, 2)
                        sendDialogueOptions(player, "Select an option", "Yes I'm brave.", "Eep! The wilderness... No thank you.")
                        addDialogueAction(player) { player, button ->
                            if (button == 2) {
                                lock(player, 2)
                                animate(player, Animations.PULL_DOWN_LEVER_2140)
                                playAudio(player, Sounds.LEVER_2400)
                                sendMessage(player, "You pull the lever...")
                                queueScript(player, 2, QueueStrength.WEAK) {
                                    if (!hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                                        teleport(player, Location.create(3154, 3923, 0))
                                        sendMessage(player, "...And teleport into the wilderness.")
                                    } else {
                                        sendMessage(player, "A magical force has stopped you from teleporting.")
                                    }
                                    stopExecuting(player)
                                }
                            } else {
                                closeDialogue(player)
                            }
                        }
                    }
                }

                /*
                 * Handles mage bank & mage arena & wilderness levers.
                 */

                Scenery.LEVER_1815, Scenery.LEVER_5959, Scenery.LEVER_5960, Scenery.LEVER_9706, Scenery.LEVER_9707 -> {
                    if (!player.savedData.activityData.hasKilledKolodion() && node.id == Scenery.LEVER_9706) {
                        sendNPCDialogue(player, NPCs.KOLODION_905, "You're not allowed in there. Come downstairs if you want to enter my arena.")
                        return@on false
                    }

                    lock(player, 2)
                    animate(player, Animations.PULL_DOWN_LEVER_2140)
                    playAudio(player, Sounds.LEVER_2400)
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

        on(kbdleverIDs, IntType.SCENERY, "pull") { player, node ->
            if (!finishedMoving(player)) {
                return@on false
            }

            sendMessage(player, "You pull the lever...")
            animate(player, Animations.PULL_DOWN_LEVER_2140, false)
            playAudio(player, Sounds.LEVER_2400)

            when (node.id) {
                Scenery.LEVER_1816 -> {
                    if (node.location == Location.create(3067, 10252, 0)) {
                        if (player.zoneMonitor.isInZone("Wilderness") && hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                            sendMessage(player, "A magical force has stopped you from teleporting.")
                        } else {
                            sendMessageWithDelay(player, "... and teleport into the lair of the King Black Dragon!", 5)
                            teleport(player, Location.create(2272, 4680, 0))
                        }
                    }
                }

                Scenery.LEVER_1817 -> {
                    teleport(player, Location.create(3067, 10253, 0))
                    sendMessageWithDelay(player, "... and teleport out of the lair of the King Black Dragon!", 5)
                }
            }

            return@on true
        }
    }
}
