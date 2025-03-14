package content.region.kandarin.quest.grandtree.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GloughGTDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GLOUGH_671)
        when (getQuestStage(player!!, Quests.THE_GRAND_TREE)) {
            40 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                    1 -> sendDialogue(player!!, "The gnome is munching on a worm hole.").also { stage++ }
                    2 -> npcl(FaceAnim.OLD_DEFAULT, "Can I help human? Can't you see I'm eating?!").also { stage++ }
                    3 -> sendDialogue(player!!, "The gnome continues to eat.").also { stage++ }
                    4 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "The King asked me to inform you that the Daconia rocks have been taken!",
                        ).also {
                            stage++
                        }

                    5 -> npcl(FaceAnim.OLD_DEFAULT, "Surely not!").also { stage++ }
                    6 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Apparently a human took them from Hazelmere. Hazelmere believed him; he had the King's seal!",
                        ).also {
                            stage++
                        }

                    7 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "I should've known! The humans are going to invade!",
                        ).also { stage++ }

                    8 -> playerl(FaceAnim.FRIENDLY, "Never!").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "Your type can't be trusted! I'll take care of this! Go back to the King.",
                        ).also {
                            setQuestStage(player!!, Quests.THE_GRAND_TREE, 45)
                            stage = END_DIALOGUE
                        }
                }
            }

            47 -> {
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Glough! I don't know what you're up to but I know you paid Charlie to get those rocks!",
                        ).also {
                            stage++
                        }
                    1 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "You're a fool human! You have no idea what's going on.",
                        ).also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I know the Grand Tree's dying! And I think you're part of the reason.",
                        ).also {
                            stage++
                        }
                    3 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "How dare you accuse me! I'm the head tree guardian! Guards! Guards!",
                        ).also { stage++ }
                    4 -> {
                        GameWorld.Pulser
                            .submit(
                                object : Pulse(0) {
                                    var count = 0
                                    val npc = NPC.create(163, Location.create(2477, 3462, 1), null)
                                    val ladderClimbAnimation = Animation(828)
                                    val cell = Location.create(2464, 3496, 3)

                                    override fun pulse(): Boolean {
                                        when (count) {
                                            0 -> {
                                                lock(player!!, 10)
                                                npc.init()
                                                forceWalk(npc, player!!.location.transform(Direction.WEST, 2), "SMART")
                                                player!!.dialogueInterpreter.sendDialogues(
                                                    npc,
                                                    FaceAnim.OLD_ANGRY1,
                                                    "Come with me!",
                                                )
                                            }
                                            2 -> forceWalk(npc, Location.create(2476, 3462, 1), "SMART")
                                            4 -> {
                                                unlock(player!!)
                                                forceWalk(player!!, Location.create(2477, 3463, 1), "SMART")
                                            }

                                            6 -> player!!.faceLocation(Location.create(2476, 3463, 1))
                                            7 -> animate(player!!, ladderClimbAnimation)
                                            8 -> {
                                                npc.clear()
                                                setQuestStage(player!!, Quests.THE_GRAND_TREE, 50)
                                                teleport(player!!, cell)
                                                player!!.unlock()
                                                return true
                                            }
                                        }
                                        count++
                                        return false
                                    }
                                },
                            ).also { stage = END_DIALOGUE }
                    }
                }
            }

            55 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "I know what you're up to Glough!").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "You have no idea human!").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "You may be able to make a fleet but the tree gnomes will never follow you into battle against humans.",
                        ).also {
                            stage++
                        }
                    3 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "So, you know more than I thought! The gnomes fear humanity more than any other race. I just need to give them a push in the right direction. There's nothing you can do traveller! Leave before it's too late!",
                        ).also {
                            stage++
                        }
                    4 -> playerl(FaceAnim.FRIENDLY, "King Narnode won't allow it!").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.OLD_DEFAULT,
                            "The King's a fool and a coward! He'll bow to me! You'll soon be back in that cage!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            60 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "I'm going to stop you, Glough!").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "You're becoming quite annoying traveller!").also { stage++ }
                    2 -> npcl(FaceAnim.OLD_DEFAULT, "Glough is searching his pockets.").also { stage++ }
                    3 -> npcl(FaceAnim.OLD_DEFAULT, "Where are that darn key?").also { stage++ }
                    4 ->
                        npcl(FaceAnim.OLD_DEFAULT, "Leave human, before I have you put in the cage!").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            else -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello there!").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_DEFAULT, "You shouldn't be here human!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "What do you mean?").also { stage++ }
                    3 -> npcl(FaceAnim.OLD_DEFAULT, "The Gnome Stronghold is for gnomes alone!").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Surely not!").also { stage++ }
                    5 -> npcl(FaceAnim.OLD_DEFAULT, "We don't need your sort round here!").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
