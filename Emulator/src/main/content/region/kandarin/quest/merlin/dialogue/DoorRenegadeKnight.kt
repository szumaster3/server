package content.region.kandarin.quest.merlin.dialogue

import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class DoorRenegadeKnight : DialogueFile() {
    var STAGE_NO = 10

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.RENEGADE_KNIGHT_237)

        when (stage) {
            0 -> {
                if (player!!.location.x >= 2764) {
                    playerl(
                        FaceAnim.THINKING,
                        "Uh... I don't think anyone outside will answer me if I knock on the door...",
                    )
                    stage = END_DIALOGUE
                } else {
                    sendDialogue(player!!, "You knock at the door. You hear a voice from inside...")
                    stage++
                }
            }

            1 -> {
                npcl(FaceAnim.ANGRY, "Yes? What do you want?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.NEUTRAL, "Um...")
                stage++
            }

            3 -> {
                showTopics(
                    Topic(FaceAnim.ANGRY, "Pizza Delivery!", 5),
                    Topic("Have you ever thought about letting Saradomin into your life?", STAGE_NO),
                    Topic("Can I interest you in some double glazing?", 15),
                    Topic("Would you like to buy some lucky heather?", STAGE_NO),
                )
            }

            5 -> {
                npcl(FaceAnim.NEUTRAL, "We didn't order any Pizza. Get lost!")
                stage = END_DIALOGUE
            }

            10 -> {
                npcl(FaceAnim.ANGRY, "No. Go away.")
                stage = END_DIALOGUE
            }

            15 -> {
                npc(FaceAnim.ANGRY, "No. Get out of here before I run you through.")
                stage++
            }

            16 -> {
                sendDialogue(player!!, "It looks like you'll have to find another way in...")
                stage = END_DIALOGUE
            }
        }
    }
}
