package content.region.desert.dialogue.nardah

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GhaslorTheElderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Good day young ${if (player.isMale) "gentleman" else "lady"}.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GhaslorTheElderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GHASLOR_THE_ELDER_3029)
    }
}
