package content.region.kandarin.quest.drunkmonk.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import org.rs.consts.Quests

class MonasteryMonkDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        var questStage = getQuestStage(player!!, Quests.MONKS_FRIEND)
        if (questStage < 100) {
            when (stage) {
                0 -> npcl(FaceAnim.FRIENDLY, "*yawn*").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.HAPPY, "Can't wait for the party!").also { stage = END_DIALOGUE }
            }
        }
    }
}
