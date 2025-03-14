package content.region.asgarnia.dialogue.entrana

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MazionDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..3).random()) {
            1 -> npc(FaceAnim.FRIENDLY, "Nice weather we're having today!").also { stage = END_DIALOGUE }
            2 -> npc(FaceAnim.FRIENDLY, "Hello " + player.name + ", fine day today!").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.ANNOYED, "Please leave me alone, a parrot stole my banana.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MazionDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAZION_3114)
    }
}
