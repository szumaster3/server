package content.region.karamja.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.shops.Shops
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the shop assistant at Musa point.
 */
@Initializable
class ShopAssistant(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Can I help you at all?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please. What are you selling?", "No, thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("Yes, please. I'd like to see your stock.").also { stage++ }
                2 -> player("No thanks, I must be going now.").also { stage = END_DIALOGUE }
            }
            2 -> {
                end()
                Shops.openId(player, 19)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ShopAssistant(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SHOP_ASSISTANT_533)
}
