package content.region.asgarnia.port_sarim.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE

/**
 * Represents the Jack Seagull dialogue.
 */
class JackSeagullDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Arrr, matey!").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.HALF_ASKING, "What are you doing here?", 2),
                Topic(FaceAnim.HALF_ASKING, "Have you got any quests I could do?", 4),
                title = "What would you like to say?"
            )
            2 -> npc(FaceAnim.NEUTRAL, "Drinking.").also { stage++ }
            3 -> player(FaceAnim.NEUTRAL, "Fair enough.").also { stage = END_DIALOGUE }
            4 -> npc(FaceAnim.HALF_GUILTY, "Nay, I've nothing for ye to do.").also { stage++ }
            5 -> player(FaceAnim.HALF_GUILTY, "Thanks.").also { stage = END_DIALOGUE }
        }
    }
}
