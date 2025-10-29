package content.region.kandarin.ardougne.quest.drunkmonk.dialogue

import core.api.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import shared.consts.Quests

/**
 * Represents the Monastery Monk dialogue.
 */
class MonasteryMonkDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        if (!isQuestComplete(player!!, Quests.MONKS_FRIEND)) {
            npcl(FaceAnim.FRIENDLY, "*yawn*").also { stage = END_DIALOGUE }
        } else {
            npcl(FaceAnim.HAPPY, "Can't wait for the party!").also { stage = END_DIALOGUE }
        }
    }
}
