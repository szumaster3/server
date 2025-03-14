package content.region.kandarin.handlers.guilds.ranging

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Bow arrow salesman dialogue.
 */
@Initializable
class BowArrowSalesmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello.").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("A fair day, traveller. Would you like to see my wares?").also { stage++ }
            1 -> options("Yes please.", "No thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, npc.id) }
                    2 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOW_AND_ARROW_SALESMAN_683)
    }
}
