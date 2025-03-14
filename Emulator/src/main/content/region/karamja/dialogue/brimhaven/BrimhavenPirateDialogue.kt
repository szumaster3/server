package content.region.karamja.dialogue.brimhaven

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BrimhavenPirateDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Man overboard!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BrimhavenPirateDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.PIRATE_183,
            NPCs.PIRATE_6349,
            NPCs.PIRATE_6350,
            NPCs.PIRATE_6346,
            NPCs.PIRATE_6347,
            NPCs.PIRATE_6348,
            NPCs.PIRATE_GUARD_799,
        )
    }
}
