package content.region.wilderness.handlers

import core.api.*
import core.game.dialogue.DialogueFile
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
    private val leverIDs =
        intArrayOf(
            Scenery.LEVER_1814,
            Scenery.LEVER_1815,
            Scenery.LEVER_5959,
            Scenery.LEVER_5960,
            Scenery.LEVER_9706,
            Scenery.LEVER_9707,
        )
    private val kbdleverIDs = intArrayOf(Scenery.LEVER_1816, Scenery.LEVER_1817)

    private fun teleport(
        player: Player,
        location: Location?,
    ) {
        player.teleporter.send(Location.create(2272, 4680, 0), TeleportType.NORMAL, TeleportManager.WILDY_TELEPORT)
    }

    override fun defineListeners() {
        on(leverIDs, IntType.SCENERY, "pull") { player, node ->
            when (node.id) {
                1814 ->
                    openDialogue(
                        player,
                        object : DialogueFile() {
                            override fun handle(
                                componentID: Int,
                                buttonID: Int,
                            ) {
                                when (stage) {
                                    0 ->
                                        sendDialogue(
                                            player,
                                            "Warning! Pulling the lever will teleport you deep into the wilderness.",
                                        ).also { stage++ }

                                    1 ->
                                        options(
                                            "Yes I'm brave.",
                                            "Eep! The wilderness... No thank you.",
                                        ).also { stage++ }
                                    2 ->
                                        when (buttonID) {
                                            1 -> {
                                                end()
                                                lock(player, 2)
                                                animate(player, Animations.PULL_DOWN_LEVER_2140)
                                                playAudio(player, Sounds.LEVER_2400)
                                                sendMessage(player, "You pull the lever...")
                                                queueScript(player, 2, QueueStrength.WEAK) {
                                                    if (player.timers.getTimer("teleblock") == null) {
                                                        teleport(player, Location(3154, 3923, 0))
                                                        sendMessage(player, "...And teleport into the wilderness.")
                                                    } else {
                                                        sendMessage(
                                                            player,
                                                            "A magical force has stopped you from teleporting.",
                                                        )
                                                    }
                                                    return@queueScript stopExecuting(player)
                                                }
                                            }

                                            2 -> end()
                                        }
                                }
                            }
                        },
                    )

                Scenery.LEVER_1815, Scenery.LEVER_5959, Scenery.LEVER_5960, Scenery.LEVER_9706, Scenery.LEVER_9707 -> {
                    if (!player.savedData.activityData.hasKilledKolodion() && node.id == Scenery.LEVER_9706) {
                        sendNPCDialogue(
                            player,
                            NPCs.KOLODION_905,
                            "You're not allowed in there. Come downstairs if you want to enter my arena.",
                        )
                        return@on false
                    }
                    lock(player, 2)
                    animate(player, Animations.PULL_DOWN_LEVER_2140)
                    playAudio(player, Sounds.LEVER_2400)
                    sendMessage(player, "You pull the lever...")
                    submitWorldPulse(
                        object : Pulse(2, player) {
                            override fun pulse(): Boolean {
                                if (player.timers.getTimer("teleblock") == null) {
                                    teleport(
                                        player,
                                        when (node.id) {
                                            Scenery.LEVER_1815 -> Location(2561, 3311, 0)
                                            Scenery.LEVER_5959 -> Location(2539, 4712, 0)
                                            Scenery.LEVER_5960 -> Location(3090, 3956, 0)
                                            Scenery.LEVER_9706 -> Location(3105, 3951, 0)
                                            else -> Location(3105, 3956, 0)
                                        },
                                    )
                                    sendMessage(
                                        player,
                                        "...And teleport ${
                                            when (node.id) {
                                                Scenery.LEVER_1815 -> "out of the wilderness."
                                                Scenery.LEVER_5959 -> "into the mage's cave."
                                                Scenery.LEVER_5960 -> "out of the mage's cave."
                                                Scenery.LEVER_9706 -> "out of the arena."
                                                else -> "into the arena."
                                            }
                                        }",
                                    )
                                } else {
                                    sendMessage(player, "A magical force has stopped you from teleporting.")
                                }
                                return true
                            }
                        },
                    )
                }

                else -> {
                    sendMessage(player, "nothing interesting happens.")
                }
            }

            return@on true
        }

        on(kbdleverIDs, IntType.SCENERY, "pull") { player, node ->
            sendMessage(player, "You pull the lever...")
            animate(player, Animations.PULL_DOWN_LEVER_2140, false)
            playAudio(player, Sounds.LEVER_2400)
            when (node.id) {
                Scenery.LEVER_1816 ->
                    if (node.location == Location.create(3067, 10252, 0)) {
                        if (player.zoneMonitor.isInZone("Wilderness") && hasTimerActive(player, "teleblock")) {
                            sendMessage(player, "A magical force has stopped you from teleporting.")
                        } else {
                            sendMessageWithDelay(player, "... and teleport into the lair of the King Black Dragon!", 5)
                            player.teleporter.send(
                                Location.create(2272, 4680, 0),
                                TeleportType.NORMAL,
                                TeleportManager.WILDY_TELEPORT,
                            )
                        }
                    }

                Scenery.LEVER_1817 -> {
                    player.teleporter.send(Location.create(3067, 10253, 0), TeleportType.NORMAL)
                    sendMessageWithDelay(player, "... and teleport out of the lair of the King Black Dragon!", 5)
                }
            }
            return@on true
        }
    }
}
