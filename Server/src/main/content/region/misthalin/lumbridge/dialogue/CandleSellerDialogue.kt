package content.region.misthalin.lumbridge.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
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
            0 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Yes please.", 1),
                Topic(FaceAnim.EXTREMELY_SHOCKED, "One thousand gold?!", 4),
                Topic(FaceAnim.NEUTRAL, "No thanks, I'd rather curse the darkness.", END_DIALOGUE)
            )

            1 -> handlePurchase()

            2 -> npc(FaceAnim.NEUTRAL, "I should warn you, though, it can be dangerous to take", "a naked flame down there. You'd better off making", "a lantern.").also {  stage++ }
            3 -> player(FaceAnim.FRIENDLY, "Okay, thanks.").also {  stage = END_DIALOGUE }
            4 -> npc(FaceAnim.NEUTRAL, "Look, you're not going to be able to survive down that", "hole without a light source.").also { stage++ }
            5 -> npc(FaceAnim.NEUTRAL, "So you could go off to the candle shop to buy one", "more cheaply. You could even make your own lantern,", "which is a lot better.").also { stage++ }
            6 -> npc(FaceAnim.HAPPY, "But I bet you want to find out what's down there right", "now, don't you? And you can pay me 1000 gold for", "the privilege!").also { stage++ }

            7 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "All right, you win, I'll buy a candle.", 1),
                Topic(FaceAnim.WORRIED, "No way.", END_DIALOGUE),
                Topic(FaceAnim.HALF_ASKING, "How do you make lanterns?", 8)
            )

            8 -> npc(FaceAnim.FRIENDLY, "Out of glass. The more advanced lanterns have a", "metal component as well.").also { stage++ }
            9 -> npc(FaceAnim.FRIENDLY, "Firstly you can make a simple candle lantern out of", "glass. It's just like a candle, but the flame isn't exposed,", "so it's safer.").also { stage++ }
            10 -> npc(FaceAnim.FRIENDLY, "Then you can make an oil lamp, which is brighter but", "has an exposed flame. But if you make an iron frame", "for it you can turn it into an oil lantern.").also { stage++ }
            11 -> npc(FaceAnim.FRIENDLY, "Finally there's a Bullseye lantern. You'll need to", "make a frame out of steel and add a glass lens.").also { stage++ }
            12 -> npc(FaceAnim.FRIENDLY, "Oce you've made your lamp or lantern, you'll need to", "make lamp oil for it. The chemist near Rimmington has", "a machine for that.").also { stage++ }
            13 -> npc(FaceAnim.FRIENDLY, "For any light source, you'll need a tinderbox to light it.", "Keep your tinderbox handy in case it goes out!").also { stage++ }
            14 -> npc(FaceAnim.HAPPY, "But if all that's to complicated, you can buy a candle", "right here for 1000 gold!").also { stage++ }

            15 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "All right, you win, I'll buy a candle.", 1),
                Topic(FaceAnim.WORRIED, "No thanks, I'd rather curse the darkness.", END_DIALOGUE),
            )
        }
        return true
    }

    /**
     * handles purchase lit candle.
     */
    private fun handlePurchase() {
        if (freeSlots(player) == 0) {
            end()
            sendMessage(player, "You don't have enough inventory space to buy a candle.")
            return
        }

        val cost = Item(Items.COINS_995, 1000)
        if (!removeItem(player, cost, Container.INVENTORY)) {
            end()
            player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
            return
        }

        addItem(player, Items.LIT_CANDLE_33, 1)
        npc(FaceAnim.HAPPY, "Here you go then.")
        stage = 2
    }

    override fun newInstance(player: Player?): Dialogue = CandleSellerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CANDLE_SELLER_1834)
}
