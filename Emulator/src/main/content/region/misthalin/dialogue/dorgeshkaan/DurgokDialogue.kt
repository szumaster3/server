package content.region.misthalin.dialogue.dorgeshkaan

import core.api.addItemOrDrop
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class DurgokDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_NORMAL, "Frogburger! There's nothing like grilled frog in a bun. Do you want one? Only 10gp!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Yes please!").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }

            2 -> {
                if (!removeItem(player, Item(Items.COINS_995, 10))) {
                    end()
                    npc(FaceAnim.OLD_NORMAL, "I'm sorry, but you need 10gp for that.")
                } else {
                    end()
                    npc(FaceAnim.OLD_NORMAL, "There you go.")
                    addItemOrDrop(player, Items.FROGBURGER_10962, 1)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DurgokDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DURGOK_5794)
    }
}
