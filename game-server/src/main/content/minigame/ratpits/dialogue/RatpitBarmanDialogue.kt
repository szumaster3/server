package content.minigame.ratpits.dialogue

import core.api.addItem
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

/**
 * Represents the Barman at Ratpit dialogue.
 */
@Initializable
class RatpitBarmanDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (npc.id) {
            NPCs.RAUBORN_2991 -> npc(FaceAnim.OLD_DEFAULT, "Welcome to the ratpit bar, human traveller.")
            else -> npc(FaceAnim.FRIENDLY, "Welcome to the ratpit bar.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val faceAnim = if (npc.id == 2991) FaceAnim.OLD_DEFAULT else FaceAnim.FRIENDLY
        when (stage) {
            0 -> options("A beer please.", "I'd like some food please.", "I have to go").also { stage++ }
            1 -> when (buttonId) {
                1 -> npc(faceAnim, "That'll be 2 gold coins.").also { stage++ }
                2 -> npc(faceAnim, "I can make you a stew for 20 gold coins.").also { stage = 4 }
                3 -> player("I have to go.").also { stage = END_DIALOGUE }
            }
            2 -> options("Pay.", "Don't pay.").also { stage++ }
            3 -> when (buttonId) {
                1 -> if (!removeItem(player, Item(Items.COINS_995, 2))) {
                    player("Sorry, I don't have 2 coins on me.").also { stage = END_DIALOGUE }
                } else {
                    addItem(player, Items.BEER_1917)
                    npc(faceAnim, "Thanks for your custom.")
                }
                2 -> player(FaceAnim.HALF_GUILTY, "Sorry, I changed my mind.").also { stage = END_DIALOGUE }
            }
            4 -> options("Pay.", "Don't pay.").also { stage++ }
            5 -> when (buttonId) {
                1 -> if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    player(FaceAnim.HALF_GUILTY, "Sorry, I don't have 20 coins on me.").also {
                        stage = END_DIALOGUE
                    }
                } else {
                    addItem(player, Items.STEW_2003)
                    npc(faceAnim, "Thanks for your custom.")
                }
                2 -> player(FaceAnim.HALF_GUILTY, "Sorry, I changed my mind.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RatpitBarmanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.RAUBORN_2991, NPCs.BARMAN_3000)
}
