package core.game.worldevents.events.halloween

import core.ServerStore
import core.ServerStore.Companion.getInt
import core.ServerStore.Companion.getString
import core.api.*
import core.api.ui.closeDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Graphics
import org.rs.consts.Items

class TrickOrTreatHandler : InteractionListener {
    override fun defineListeners() {
        on(IntType.NPC, "trick-or-treat") { player, node ->
            val hasDone5 = getDailyTrickOrTreats(player) == 5
            val hasDoneMe = getTrickOrTreatedNPCs(player).contains(node.name.lowercase())

            if (hasDone5) {
                sendNPCDialogue(
                    player,
                    node.id,
                    "My informants tell me you've already collected candy from 5 people today.",
                    FaceAnim.FRIENDLY,
                )
                return@on true
            }

            if (hasDoneMe) {
                sendNPCDialogue(
                    player,
                    node.id,
                    "You've already asked me today! Don't get greedy, now.",
                    FaceAnim.ANNOYED,
                )
                return@on true
            }

            player.dialogueInterpreter.open(
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                playerl(FaceAnim.FRIENDLY, "Trick or treat!").also {
                                    if (RandomFunction.roll(20)) {
                                        stage =
                                            10
                                    } else {
                                        stage++
                                    }
                                }

                            1 -> npcl(FaceAnim.FRIENDLY, "Very well, then, here you are my friend.").also { stage++ }

                            2 -> {
                                val rand = RandomFunction.random(0, 2)
                                when (rand) {
                                    0 -> {
                                        sendItemDialogue(
                                            player,
                                            Items.POPCORN_BALL_14082,
                                            "They hand you a popcorn ball.",
                                        )
                                        addItemOrDrop(player, Items.POPCORN_BALL_14082, 1)
                                    }
                                    1 -> {
                                        sendItemDialogue(
                                            player,
                                            Items.CHOCOLATE_DROP_14083,
                                            "They hand you a chocolate drop.",
                                        )
                                        addItemOrDrop(player, Items.CHOCOLATE_DROP_14083, 1)
                                    }
                                    2 -> {
                                        sendItemDialogue(
                                            player,
                                            Items.WRAPPED_CANDY_14084,
                                            "They hand you a nicely-wrapped candy.",
                                        )
                                        addItemOrDrop(player, Items.WRAPPED_CANDY_14084, 1)
                                    }
                                }
                                registerNpc(player, npc!!)
                                incrementDailyToT(player)
                                stage = END_DIALOGUE
                            }

                            10 ->
                                npcl(FaceAnim.EVIL_LAUGH, "I CHOOSE TRICK!").also {
                                    player.lock()
                                    Pulser.submit(
                                        object : Pulse() {
                                            var counter = 0

                                            override fun pulse(): Boolean {
                                                when (counter++) {
                                                    0 ->
                                                        visualize(
                                                            npc!!,
                                                            Animations.CAST_SPELL_1979,
                                                            Graphics.DEATH_BLACK_SKULL_SWIRL_1898,
                                                        ).also {
                                                            faceLocation(npc!!, player.location)
                                                        }

                                                    2 -> closeDialogue(player)
                                                    5 -> openOverlay(player, Components.FADE_TO_BLACK_120)
                                                    8 -> teleport(player, Location.create(3106, 3382, 0))
                                                    12 -> {
                                                        closeOverlay(player)
                                                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                                                        registerNpc(player, npc!!)
                                                    }

                                                    15 -> closeOverlay(player).also { unlock(player) }
                                                    16 -> return true
                                                }
                                                return false
                                            }
                                        },
                                    )
                                }
                        }
                    }
                },
                node.asNpc(),
            )
            return@on true
        }
    }

    fun incrementDailyToT(player: Player) {
        ServerStore.getArchive("daily-tot-total")[player.username.lowercase()] = getDailyTrickOrTreats(player) + 1
    }

    fun getDailyTrickOrTreats(player: Player): Int {
        return ServerStore.getArchive("daily-tot-total").getInt(player.username.lowercase())
    }

    fun getTrickOrTreatedNPCs(player: Player): String {
        return ServerStore.getArchive("daily-tot-npcs").getString(player.username.lowercase())
    }

    fun registerNpc(
        player: Player,
        npc: NPC,
    ) {
        var soFar = getTrickOrTreatedNPCs(player)
        soFar += ":" + npc.name.lowercase() + ":"
        ServerStore.getArchive("daily-tot-npcs")[player.username.lowercase()] = soFar
    }
}
