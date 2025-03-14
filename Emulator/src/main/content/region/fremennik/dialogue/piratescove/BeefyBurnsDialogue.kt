package content.region.fremennik.dialogue.piratescove

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BeefyBurnsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_ASKING, "What are you cooking?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.LAUGH, "My speciality! What else could I be cooking?").also { stage++ }
            1 -> player(FaceAnim.THINKING, "Ok, and your speciality is...?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Boiled shark guts with a hint of rosemary and a dash of squid ink.",
                ).also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "I think I'll stick to making my own food.").also { stage++ }
            4 -> npc(FaceAnim.FRIENDLY, "Your loss!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BeefyBurnsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BEEFY_BURNS_4541)
    }
}
