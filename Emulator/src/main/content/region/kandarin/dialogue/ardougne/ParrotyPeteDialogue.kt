package content.region.kandarin.dialogue.ardougne

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ParrotyPeteDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Good day, good day. Come to admire the new parrot aviary have we?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("It's very nice.", "When did you add it?", "What do you feed them?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "It's very nice.").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "When did you add it?").also { stage = 3 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "What do you feed them?").also { stage = 4 }
                }
            2 -> npcl(FaceAnim.FRIENDLY, "Isn't it just?").also { stage = END_DIALOGUE }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Just recently. It would have been sooner, but some wretch thought it would be amusing to replace their drinking water with vodka. The vet had to nurse them back to health for weeks!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, fruit and grain mostly. I try to give them a balanced diet, but their favourite treat is pineapple chunks.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ParrotyPeteDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PARROTY_PETE_1216)
    }
}
