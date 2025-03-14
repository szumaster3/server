package content.region.asgarnia.dialogue.portsarim

import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class JackSeagullDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Arrr, matey!")
        // sendNPCDialogue(player, NPCs.LONGBOW_BEN_2691, "")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "What are you doing here?",
                    "Have you got any quests I could do?",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What are you doing here?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Have you got any quests I could do?").also { stage = 4 }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "Drinking.").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "Fair enough.").also { stage = END_DIALOGUE }
            4 -> npc(FaceAnim.HALF_GUILTY, "Nay, I've nothing for ye to do.").also { stage++ }
            5 -> player(FaceAnim.HALF_GUILTY, "Thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JACK_SEAGULL_2690)
    }
}
