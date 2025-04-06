package content.region.misc.dialogue.tutorial

import content.data.GameAttributes
import content.region.misc.handlers.tutorial.*
import core.ServerConstants
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.info.Rights
import core.game.system.task.Pulse
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.worker.ManagementEvents
import org.rs.consts.Items
import org.rs.consts.NPCs
import proto.management.JoinClanRequest

class SkippyTutorialDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SKIPPY_2796)
        when (stage) {
            0 -> npcl(FaceAnim.HALF_ASKING, "Do you wanna skip the Tutorial?").also { stage++ }
            1 -> {
                setTitle(player!!, 4)
                sendDialogueOptions(
                    player!!,
                    "What would you like to say?",
                    "Yes, please.",
                    "Who are you?",
                    "Can I decide later?",
                    "I'll stay here for the Tutorial.",
                )
                stage++
            }

            2 ->
                when (buttonID) {
                    1 -> npc(FaceAnim.HAPPY, "Prepare yourself!").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING, "Who are you?").also { stage++ }
                    3 -> player(FaceAnim.HALF_ASKING, "Can I decide later?").also { stage = 6 }
                    4 -> player("I'll stay here for the Tutorial.").also { stage = 7 }
                }

            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "My name's Skippy. Normally I live down by Rimmington. You should come and see me when you're passing.",
                ).also {
                    stage++
                }

            4 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Just lately the Council wants to let people skip the Tutorial, so - ha ha ha - they dump the job on Skippy. Bah!",
                ).also {
                    stage++
                }

            5 -> npcl(FaceAnim.HALF_ASKING, "So, anyway, do you want to skip the Tutorial?").also { stage = 8 }
            6 -> npcl(FaceAnim.NOD_NO, "Unfortunately, so far there is no such possibility.").also { stage = 5 }
            7 -> npcl(FaceAnim.NOD_YES, "Good choice.").also { stage = END_DIALOGUE }
            8 ->
                sendDialogueOptions(
                    player!!,
                    "What would you like to say?",
                    "Yes, please.",
                    "Can I decide later?",
                    "I'll stay here for the Tutorial.",
                ).also {
                    stage++
                }

            9 ->
                when (buttonID) {
                    1 -> npc(FaceAnim.HAPPY, "Prepare yourself!").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING, "Can I decide later?").also { stage = 6 }
                    3 -> player("I'll stay here for the Tutorial.").also { stage = 7 }
                }

            10 -> {
                end()
                if (player?.rights != Rights.ADMINISTRATOR) {
                    submitIndividualPulse(
                        player!!,
                        object : Pulse(1) {
                            override fun pulse(): Boolean {
                                setAttribute(player!!, "/save:${GameAttributes.TUTORIAL_STAGE}", 71)
                                TutorialStage.load(player!!, 71)
                                teleport(player!!, Location.create(3141, 3089, 0))
                                return true
                            }
                        },
                    )
                } else {
                    lock(player!!, 4)
                    player!!.dialogueInterpreter.sendPlainMessage(true, "Setting up world.", "Please wait...")
                    submitWorldPulse(
                        object : Pulse() {
                            var counter = 0

                            override fun pulse(): Boolean {
                                when (counter++) {
                                    0 -> {
                                        setVarbit(player!!, 3756, 0)
                                        setVarp(player!!, 281, 1000, true)
                                        setAttribute(player!!, "/save:tutorial:complete", true)
                                        player!!.unhook(TutorialCompletionReceiver)
                                        player!!.unhook(TutorialKillReceiver)
                                        player!!.unhook(TutorialFireReceiver)
                                        player!!.unhook(TutorialResourceReceiver)
                                        player!!.unhook(TutorialUseWithReceiver)
                                        player!!.unhook(TutorialInteractionReceiver)
                                        player!!.unhook(TutorialButtonReceiver)
                                        player!!.setAttribute(GameAttributes.TUTORIAL_STAGE, 73)
                                        if (settings!!.enable_default_clan) {
                                            player!!.communication.currentClan = ServerConstants.SERVER_NAME
                                            val clanJoin = JoinClanRequest.newBuilder()
                                            clanJoin.clanName = ServerConstants.SERVER_NAME
                                            clanJoin.username = player!!.name
                                            ManagementEvents.publish(clanJoin.build())
                                        }
                                    }

                                    3 -> {
                                        closeOverlay(player!!)
                                        addItem(player!!, Items.ROTTEN_POTATO_5733)
                                        player!!.teleporter.send(Location.create(3233, 3230, 0))
                                        openDialogue(player!!, WelcomeMessage())
                                        setAttribute(player!!, "close_c_", true)
                                        player!!.interfaceManager.restoreTabs()
                                        return true
                                    }
                                }
                                return false
                            }
                        },
                    )
                }
            }
        }
    }
}
