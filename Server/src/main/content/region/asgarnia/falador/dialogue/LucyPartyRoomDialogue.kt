package content.region.asgarnia.falador.dialogue

import core.api.addItem
import core.api.removeItem
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import shared.consts.Items

/**
 * Represents the Falador party room employee dialogue.
 */
class LucyPartyRoomDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Hi! I'm Lucy. Welcome to the Party Room!").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Hi.").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "Would you like to buy a beer?").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "How much do they cost?").also { stage++ }
            4 -> npc(FaceAnim.HALF_GUILTY, "Just 2 gold pieces.").also { stage++ }
            5 -> showTopics(
                Topic("Yes please!", 6),
                Topic("No thanks, I can't afford that.", 8)
            )
            6 -> npc(FaceAnim.HALF_GUILTY, "Coming right up sir!").also { stage++ }
            7 -> {
                end()
                if (player == null) return
                if (!removeItem(player!!, Item(Items.COINS_995, 2))) {
                    sendMessage(player!!, "You don't have enough coins.")
                } else {
                    sendItemDialogue(player!!, Items.BEER_1917, "Lucy has given you a beer.")
                    addItem(player!!, Items.BEER_1917)
                }
                stage = END_DIALOGUE
            }
            8 -> npc(FaceAnim.HALF_GUILTY, "I see. Well, come and see me if you change your mind. You", "know where I am!").also { stage = END_DIALOGUE }
        }
    }
}
