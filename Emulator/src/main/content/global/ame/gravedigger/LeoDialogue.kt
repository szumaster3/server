package content.global.ame.gravedigger

import content.data.RandomEvent
import core.api.*
import core.api.ui.setMinimapState
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

class LeoDialogue : DialogueFile() {
    private fun graveFaq(player: Player) {
        val graveFAQ =
            arrayOf(
                "<br><col=FFF900>You need to:</br></col=FFF900>",
                "Pick up the coffins.",
                "Check the body inside.",
                "Find out where they need to be buried.",
                "Put all give coffins in the correct graves.",
                "Then talk to Leo to get a reward.",
                "You can store items in the mausoleum",
                "if you need more inventory space.",
            )
        sendString(player, graveFAQ.joinToString("<br><col=FFF900>"), Components.BLANK_SCROLL_222, 3)
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.LEO_3508)
        val correctTask = getAttribute(player!!, GraveUtils.LEO_TASK, 0)
        val totalPoints = getAttribute(player!!, GraveUtils.LEO_COFFIN_POINTS, 0)
        if (correctTask == 1) {
            when (stage) {
                0 -> {
                    lock(player!!, 8)
                    npcl(
                        "You have to help me dig these graves ${player!!.username}, I don't know who else to turn to!",
                    ).also { stage++ }
                }

                1 -> playerl("What do you mean?").also { stage++ }
                2 -> npcl("I've put the wrong coffins into these graves.").also { stage++ }
                3 ->
                    npcl(
                        "I saw you burying bones and it struck me that you seemed to know what you were doing.",
                    ).also { stage++ }
                4 -> npcl("I need you to pick up the coffins and put them in the right graves.").also { stage++ }
                5 ->
                    npcl(
                        "If you need to free up some space in your inventory then just store things in the mausoleum, and I'll take them to the bank for you.",
                    ).also {
                        stage++
                    }
                6 -> npcl("I have a reward for you if you get it right.").also { stage++ }
                7 -> playerl("Ok, I'll get right on it.").also { stage++ }
                8 -> {
                    end()
                    setAttribute(player!!, GraveUtils.LEO_TASK, 2)
                    setAttribute(player!!, GraveUtils.LEO_COFFIN_POINTS, 0)
                    openInterface(player!!, Components.BLANK_SCROLL_222).also { graveFaq(player!!) }
                }
            }
        } else if (totalPoints == 4 || totalPoints == 3 || totalPoints == 2 || totalPoints == 1) {
            when (stage) {
                0 -> options("There, finished!", "I want to leave.").also { stage = 1 }
                1 ->
                    when (buttonID) {
                        1 -> sendDialogue(player!!, "Ok, let's have a look.").also { stage = 2 }
                        2 -> playerl("I want to leave.").also { stage = 6 }
                    }

                2 -> {
                    lock(player!!, 3)
                    runTask(player!!, 3) {
                        Component
                            .setUnclosable(
                                player!!,
                                player!!.dialogueInterpreter.sendDialogues(
                                    NPC(3508),
                                    FaceAnim.FRIENDLY,
                                    "Well, that's a good attempt, but it's just not right.",
                                ),
                            ).also {
                                stage =
                                    3
                            }
                    }
                }
                3 ->
                    npcl(
                        "Try looking in the coffins to get a better idea of who is in them, and then read the gravestones to find who needs to be in there.",
                    ).also {
                        stage =
                            4
                    }
                4 -> playerl("All right, I'll give it another shot.").also { stage = 5 }
                5 ->
                    npcl(
                        "Don't forget to store any items that you don't need in the mausoleum. I'll take them to the bank while you work.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                6 -> npcl("In that case, I'll take you back to where I found you.").also { stage = 7 }
                7 -> {
                    end()
                    GraveUtils.cleanup(player!!)
                }
            }
        } else if (totalPoints == 5) {
            when (stage) {
                0 -> options("There, finished!", "I want to leave.").also { stage = 1 }
                1 ->
                    when (buttonID) {
                        1 -> sendDialogue(player!!, "Ok, let's have a look.").also { stage = 3 }
                        2 -> playerl("I want to leave.").also { stage = 6 }
                    }

                3 -> {
                    lock(player!!, 3)
                    runTask(player!!, 3) {
                        Component
                            .setUnclosable(
                                player!!,
                                player!!.dialogueInterpreter.sendDialogues(
                                    NPC(3508),
                                    FaceAnim.FRIENDLY,
                                    "Wonderful! That's taken care of all of them.",
                                ),
                            ).also {
                                stage =
                                    4
                            }
                    }
                }

                4 -> npcl("Here, I'll take you back to where I found you, and give you your reward.").also { stage = 5 }
                5 -> {
                    end()
                    GraveUtils.cleanup(player!!)
                    player!!.pulseManager.run(
                        object : Pulse(2) {
                            override fun pulse(): Boolean {
                                GraveUtils.reward(player!!)
                                return true
                            }
                        },
                    )
                }

                6 -> npcl("In that case, I'll take you back to where I found you.").also { stage = 7 }
                7 -> {
                    end()
                    GraveUtils.cleanup(player!!)
                }
            }
        } else if (getAttribute(player!!, GraveUtils.LEO_TASK, 0) == 2) {
            when (stage) {
                0 -> sendMessage(player!!, "They aren't interested in talking to you.")
            }
        } else {
            when (stage) {
                0 ->
                    npcl(
                        "Can you come and help me, ${player!!.username}? I've got a problem with some graves.",
                    ).also { stage++ }
                1 -> options("Okay, I'll help with your graves.", "Sorry, I'm busy.").also { stage++ }
                2 ->
                    when (buttonID) {
                        1 -> {
                            end()
                            setAttribute(player!!, RandomEvent.save(), player!!.location)
                            registerLogoutListener(player!!, RandomEvent.logout()) { p ->
                                p.location = getAttribute(p, RandomEvent.save(), player!!.location)
                            }
                            setMinimapState(player!!, 2)
                            player!!.properties.teleportLocation = Location.create(1928, 5002, 0)
                            openDialogue(player!!, LeoDialogue())
                            setAttribute(player!!, GraveUtils.LEO_TASK, 1)
                            player!!.faceLocation(Location(1928, 5003, 0))
                            AntiMacro.terminateEventNpc(player!!)
                            stage = END_DIALOGUE
                        }

                        2 -> {
                            end()
                            AntiMacro.terminateEventNpc(player!!)
                            stage = END_DIALOGUE
                        }
                    }
            }
        }
    }
}
