package content.region.misc.dialogue.keldagrim.politics

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class WhiteChiselDirectorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        sendDialogue("White Chisel Director is too important to talk to you.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return WhiteChiselDirectorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WHITE_CHISEL_DIRECTOR_2104)
    }
}
