package content.region.asgarnia.dialogue.falador

import core.api.removeItem
import core.api.sendItemDialogue
import core.api.sendMessage
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
class LucyPartyRoomDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hi! I'm Lucy. Welcome to the Party Room!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hi.").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "Would you like to buy a beer?").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "How much do they cost?").also { stage++ }
            3 -> npc(FaceAnim.HALF_GUILTY, "Just 2 gold pieces.").also { stage++ }
            4 -> options("Yes please!", " No thanks, I can't afford that.").also { stage++ }
            5 ->
                when (interfaceId) {
                    228 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HALF_GUILTY, "Yes please!").also { stage = 10 }
                            2 -> player(FaceAnim.HALF_GUILTY, "No thanks, I can't afford that.").also { stage = 69 }
                        }
                }
            10 -> npc(FaceAnim.HALF_GUILTY, "Coming right up sir!").also { stage++ }
            11 -> {
                if (!removeItem(player, COINS)) {
                    end()
                    sendMessage(player, "You don't have enough coins.")
                } else {
                    sendItemDialogue(player, Items.BEER_1917, "Lucy has given you a beer.")
                    player.inventory.add(BEER)
                    stage = END_DIALOGUE
                }
            }
            69 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I see. Well, come and see me if you change your mind. YOu",
                    "know where I am!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUCY_662)
    }

    companion object {
        private val COINS = Item(995, 2)
        private val BEER = Item(1917, 1)
    }
}
