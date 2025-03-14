package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class IffieDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc(
            "Sorry, dearie, if I stop to chat I'll lose count.",
            "Talk to my sister instead; she likes to chat.",
            "You'll find her upstairs in the Varrock Church.",
        ).also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return IffieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IFFIE_5914)
    }
}
