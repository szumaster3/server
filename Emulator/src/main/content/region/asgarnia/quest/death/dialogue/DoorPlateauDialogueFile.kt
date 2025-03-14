package content.region.asgarnia.quest.death.dialogue

import core.api.getScenery
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class DoorPlateauDialogueFile(
    val door: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (door == 1) {
            npc = NPC(NPCs.HAROLD_1078)
            when (stage) {
                0 ->
                    if (player!!.location.y < 3543) {
                        end()
                        DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2906, 3543, 1)!!)
                    } else {
                        sendDialogue(player!!, "You knock on the door.").also { stage++ }
                    }

                1 -> npcl(FaceAnim.FRIENDLY, "Come on in!").also { stage++ }
                2 -> {
                    end()
                    DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2906, 3543, 1)!!)
                }
            }
        }
        if (door == 2) {
            npc = NPC(NPCs.TENZING_1071)

            when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
                in 0..19 -> {
                    when (stage) {
                        0 -> {
                            if (player!!.location.x < 2823) {
                                end()
                                DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2823, 3555, 0)!!)
                            } else {
                                sendDialogue(player!!, "You knock on the door.").also { stage++ }
                            }
                        }

                        1 -> npcl(FaceAnim.FRIENDLY, "No milk today! Thank you!").also { stage = END_DIALOGUE }
                    }
                }

                20 -> {
                    when (stage) {
                        0 -> {
                            if (player!!.location.x < 2823) {
                                end()
                                DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2823, 3555, 0)!!)
                            } else {
                                sendDialogue(player!!, "You knock on the door.")
                                stage++
                            }
                        }

                        1 -> npcl(FaceAnim.FRIENDLY, "No milk today! Thank you!").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "I'm not the milkman, I need your help!").also { stage++ }
                        3 -> npcl(FaceAnim.FRIENDLY, "Oh...OK. You'd better come in then.").also { stage++ }
                        4 -> {
                            end()
                            DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2823, 3555, 0)!!)
                        }
                    }
                }

                in 21..100 -> {
                    end()
                    DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2823, 3555, 0)!!)
                }
            }
        }
        if (door == 3) {
            npc = NPC(NPCs.TENZING_1071)
            when (getQuestStage(player!!, Quests.DEATH_PLATEAU)) {
                in 0..24 -> {
                    when (stage) {
                        0 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Where do you think you're going? This is private property!",
                            ).also { stage = END_DIALOGUE }
                    }
                }

                in 25..100 -> {
                    end()
                    DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2820, 3558, 0)!!)
                }
            }
        }
    }
}
