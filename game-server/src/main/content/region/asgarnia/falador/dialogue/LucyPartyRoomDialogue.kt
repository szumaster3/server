package content.region.asgarnia.falador.dialogue

import core.api.removeItem
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Lucy (Falador party room) dialogue.
 */
@Initializable
class LucyPartyRoomDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hi! I'm Lucy. Welcome to the Party Room!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hi.").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "Would you like to buy a beer?").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "How much do they cost?").also { stage++ }
            3 -> npc(FaceAnim.HALF_GUILTY, "Just 2 gold pieces.").also { stage++ }
            4 -> options("Yes please!", " No thanks, I can't afford that.").also { stage++ }
            5 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "Yes please!").also { stage = 6 }
                2 -> player(FaceAnim.HALF_GUILTY, "No thanks, I can't afford that.").also { stage = 8 }
            }
            6 -> npc(FaceAnim.HALF_GUILTY, "Coming right up sir!").also { stage++ }
            7 -> {
                if (!removeItem(player, COINS)) {
                    sendMessage(player, "You don't have enough coins.")
                } else {
                    sendItemDialogue(player, Items.BEER_1917, "Lucy has given you a beer.")
                    player.inventory.add(BEER)
                }
                stage = 9
            }
            8 -> npc(FaceAnim.HALF_GUILTY, "I see. Well, come and see me if you change your mind. YOu", "know where I am!").also { stage++ }
            9 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LUCY_662, NPCs.MEGAN_661)

    companion object {
        private val COINS = Item(Items.COINS_995, 2)
        private val BEER = Item(Items.BEER_1917, 1)
    }
}
