package content.region.misthalin.dialogue.varrock

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class VarrockSwordShopDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Hello, adventurer. Can I interest you in some swords?").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, I'm okay for swords right now.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "Yes, please.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "No, I'm okay for swords right now.").also { stage += 2 }
                }
            2 -> end().also { openNpcShop(player, npc.id) }
            3 -> npc(FaceAnim.FRIENDLY, "Come back if you need any.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VarrockSwordShopDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOPKEEPER_551, NPCs.SHOP_ASSISTANT_552)
    }
}
