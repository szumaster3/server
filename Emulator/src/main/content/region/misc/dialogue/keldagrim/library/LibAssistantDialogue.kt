package content.region.misc.dialogue.keldagrim.library

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LibAssistantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when ((0..3).random()) {
            0 ->
                when (stage) {
                    0 -> npc(FaceAnim.OLD_NORMAL, "Ssshhhh...").also { stage = END_DIALOGUE }
                }

            1 ->
                when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "What can I help you with?").also { stage++ }
                    1 -> playerl(FaceAnim.HALF_ASKING, "Help? What kind of help do you mean?").also { stage++ }
                    2 -> npcl(FaceAnim.OLD_NORMAL, "Looking for a book? A manuscript? Knowledge?").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "Oh, nothing in specific really.").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "I'll leave you to your browsing, then.",
                        ).also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "There is a lot of knowledge stored in this library, you know.",
                        ).also { stage++ }
                    1 -> playerl(FaceAnim.FRIENDLY, "Oh, I'm sure.").also { stage = END_DIALOGUE }
                }

            3 ->
                when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "And where do you come from, traveller?").also { stage++ }
                    1 -> playerl(FaceAnim.FRIENDLY, "From... out of town.").also { stage++ }
                    2 -> npcl(FaceAnim.OLD_NORMAL, "That I could've guessed.").also { stage++ }
                    3 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "We've been having more human visitors in the city recently, there is another one over there. Perhaps you'll want to talk to her.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LibAssistantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ASSISTANT_2166)
    }
}
