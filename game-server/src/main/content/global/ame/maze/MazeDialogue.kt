package content.global.ame.maze

import core.api.sendDialogueOptions
import core.api.setTitle
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the mysterious old man dialogue (Maze random event).
 * TODO find full dialogue.
 * @author szu
 */
class MazeDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.MYSTERIOUS_OLD_MAN_410)
        when(stage) {
            0 -> npc(FaceAnim.ASKING, "Are you lost? ${player?.username} Or would you like to deposit", "items into your bank account?").also { stage++ }
            1 -> {
                setTitle(player!!, 4)
                sendDialogueOptions(player!!, "What would you like to say?", "What I meant to be doing?", "I want to bank my stuff.", "I give up! Can I leave now?", "No, I'm alright.")
                stage = 2
            }
            2 -> when(buttonID) {
                1 -> player("What I meant to be doing?").also { stage++ }
                2 -> end()//.also { openDepositBox(player!!) } // TODO: Need good implementation.
                3 -> player("I give up! Can I leave now?").also { stage++ }
                4 -> player("No, I'm alright.").also { stage = END_DIALOGUE }
            }
            3 -> npcl(FaceAnim.FRIENDLY, "You need to reach the maze center, then you'll be returned to where you were.")
        }
    }
}