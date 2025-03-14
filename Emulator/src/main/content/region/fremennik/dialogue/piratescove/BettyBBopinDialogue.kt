package content.region.fremennik.dialogue.piratescove

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BettyBBopinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Dude!", "Duuuuuuude!", "Dude!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BettyBBopinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BETTY_BBOPPIN_4553)
    }
}
