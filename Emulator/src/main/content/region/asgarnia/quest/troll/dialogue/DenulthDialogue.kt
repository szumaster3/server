package content.region.asgarnia.quest.troll.dialogue

import core.api.hasAnItem
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class DenulthDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.TROLL_STRONGHOLD)) {
            in 1..7 -> {
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "How are you getting on with rescuing Godric?",
                        ).also { stage++ }
                    1 -> {
                        if (hasAnItem(player!!, Items.CLIMBING_BOOTS_3105).exists()) {
                            playerl(FaceAnim.FRIENDLY, " I've got some climbing boots.").also { stage = 2 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "I haven't found a way to climb up yet.").also { stage = 3 }
                        }
                    }

                    2 ->
                        npcl(FaceAnim.FRIENDLY, "Then hurry, friend! What are you still doing here?").also {
                            stage = END_DIALOGUE
                        }

                    3 ->
                        npcl(FaceAnim.FRIENDLY, "Hurry, friend! Who knows what they'll do with Godric?").also {
                            stage = END_DIALOGUE
                        }
                }
            }

            in 8..10 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Welcome back friend!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I've found my way into the prison.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "...and?").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "That's all.").also { stage++ }
                    5 ->
                        npcl(FaceAnim.FRIENDLY, "Hurry, friend. Find a way to free Godric!").also {
                            stage = END_DIALOGUE
                        }
                }
            }

            11 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Welcome back friend!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I have freed Godric!").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Oh, what great news! You should hurry to tell Dunstan, he will be overjoyed!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }
        }
    }
}
