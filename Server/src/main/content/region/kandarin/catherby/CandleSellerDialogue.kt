package content.region.kandarin.catherby

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Candle Seller dialogue.
 */
@Initializable
class CandleSellerDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Do you want a lit candle for 1000 gold?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please.", "One thousand gold?!", "No thanks, I'd rather curse the darkness.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.HAPPY, "Yes please.").also { stage++ }
                2 -> player(FaceAnim.EXTREMELY_SHOCKED, "One thousand gold?!").also { stage = 5 }
                3 -> player(FaceAnim.EXTREMELY_SHOCKED, "No thanks, I'd rather curse the darkness.").also { stage = END_DIALOGUE }
            }
            2 -> {
                if (freeSlots(player) == 0) {
                    end()
                    sendMessage(player, "You don't have enough inventory space to buy a candle.")
                }
                if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                    end()
                    player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
                }
                addItem(player, Items.LIT_CANDLE_33, 1)
                npc(FaceAnim.HAPPY, "Here you go then.")
                stage++
            }
            3 -> npc(FaceAnim.NEUTRAL, "I should warn you, though, it can be dangerous to take", "a naked flame down there. You'd better off making", "a lantern.").also {  stage++ }
            4 -> player(FaceAnim.FRIENDLY, "Okay, thanks.").also {  stage = END_DIALOGUE }
            5 -> npc(FaceAnim.NEUTRAL, "Look, you're not going to be able to survive down that", "hole without a light source.").also { stage++ }
            6 -> npc(FaceAnim.NEUTRAL, "So you could go off to the candle shop to buy one", "more cheaply. You could even make your own lantern,", "which is a lot better.").also { stage++ }
            7 -> npc(FaceAnim.HAPPY, "But I bet you want to find out what's down there right", "now, don't you? And you can pay me 1000 gold for", "the privilege!").also { stage++ }
            8 -> options("All right, you win, I'll buy a candle.", "No way.", "How do you make lanterns?").also { stage++ }
            9 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "All right, you win, I'll buy a candle.").also { stage = 2 }
                2 -> player(FaceAnim.ANNOYED, "No way.").also { stage = END_DIALOGUE }
                3 -> player(FaceAnim.HALF_ASKING, "How do you make lanterns?").also { stage = 10 }
            }
            10 -> npc(FaceAnim.FRIENDLY, "Out of glass. The more advanced lanterns have a", "metal component as well.").also { stage++ }
            11 -> npc(FaceAnim.FRIENDLY, "Firstly you can make a simple candle lantern out of", "glass. It's just like a candle, but the flame isn't exposed,", "so it's safer.").also { stage++ }
            12 -> npc(FaceAnim.FRIENDLY, "Then you can make an oil lamp, which is brighter but", "has an exposed flame. But if you make an iron frame", "for it you can turn it into an oil lantern.").also { stage++ }
            13 -> npc(FaceAnim.FRIENDLY, "Finally there's a Bullseye lantern. You'll need to", "make a frame out of steel and add a glass lens.").also { stage++ }
            14 -> npc(FaceAnim.FRIENDLY, "Oce you've made your lamp or lantern, you'll need to", "make lamp oil for it. The chemist near Reimmington has", "a machine for that.").also { stage++ }
            15 -> npc(FaceAnim.FRIENDLY, "For any light source, you'll need a tinderbox to light it.", "Keep your tinderbox handy in case it goes out!").also { stage++ }
            16 -> npc(FaceAnim.HAPPY, "But if all that's to complicated, you can buy a candle", "right here for 1000 gold!").also { stage++ }
            17 -> options("All right, you win, I'll buy a candle.", "No thanks, I'd rather curse the darkness.").also { stage++ }
            18 -> when (buttonId) {
                1 -> {
                    if (freeSlots(player) == 0) {
                        end()
                        sendMessage(player, "You don't have enough inventory space to buy a candle.")
                    }
                    if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                        end()
                        player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
                    }
                    addItem(player, Items.LIT_CANDLE_33, 1)
                    npc(FaceAnim.HAPPY, "Here you go then.").also { stage = 3 }
                }
                2 -> player(FaceAnim.EXTREMELY_SHOCKED, "No thanks, I'd rather curse the darkness.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CandleSellerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CANDLE_SELLER_1834)
}
