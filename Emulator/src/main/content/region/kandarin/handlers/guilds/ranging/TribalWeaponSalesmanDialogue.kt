package content.region.kandarin.handlers.guilds.ranging

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Tribal weapon salesman dialogue.
 */
@Initializable
class TribalWeaponSalesmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Greetings, traveller. Are you interested in any throwing", "weapons?").also { stage++ }
            1 -> options("Yes I am.", "Not really.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Yes I am.").also { stage++ }
                    2 -> player("Not really.").also { stage += 3 }
                }
            3 -> npc("That is a good thing.").also { stage++ }
            4 -> end().also { openNpcShop(player, npc.id) }
            5 -> npc("No bother to me.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRIBAL_WEAPON_SALESMAN_692)
    }
}
