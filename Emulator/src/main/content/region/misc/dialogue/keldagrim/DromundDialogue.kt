package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DromundDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.OLD_NORMAL,
            "Someone stole my beautiful boots...",
            "I had to buy some crummy replicas,",
            "the real boots were one of a kind.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                player(FaceAnim.FRIENDLY, "Don't worry, if I find out who did it", "I'll take care of them.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DromundDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DROMUND_2169)
    }
}
