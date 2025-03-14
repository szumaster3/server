package content.region.desert.dialogue.nardah

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AwusahTheMayorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        sendDialogue(player, "The mayor doesn't seem interested in talking to you right now.").also {
            stage =
                END_DIALOGUE
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
        return AwusahTheMayorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AWUSAH_THE_MAYOR_3040)
    }
}
