package content.region.morytania.handlers.taxidermist

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TaxidermistDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Oh, hello. Have you got something you want", "preserving?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please", "Not right now", "What?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes please.").also { stage = 5 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Not right now.").also { stage++ }
                    3 -> player(FaceAnim.ASKING, "What?").also { stage = 3 }
                }
            2 ->
                npc(FaceAnim.HALF_GUILTY, "Well, you go kill things so I can stuff them, eh?").also {
                    stage =
                        END_DIALOGUE
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you bring me a monster head or a very big fish, I can preserve it for you so you can mount it in your house. I hear there are all sorts of exotic creatures in the Slayer Tower.",
                ).also {
                    stage++
                }
            4 -> npc("I'd like a chance to stuff one of them!").also { stage = END_DIALOGUE }
            5 -> npc("Give it to me to look at then.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TaxidermistDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TAXIDERMIST_4246)
    }
}
