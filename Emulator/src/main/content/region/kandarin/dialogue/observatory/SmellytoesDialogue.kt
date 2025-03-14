package content.region.kandarin.dialogue.observatory

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SmellytoesDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hi there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Hey, ids me matesh!").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Sorry, have we met?").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Yeah! you wazsh wiv me in dat pub overy by hill!").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "I have no idea what you're going on about.").also { stage++ }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Glad yeeash remembers.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SmellytoesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SMELLYTOES_6128)
    }
}
