package content.region.misthalin.dialogue.varrock

import core.api.addItem
import core.api.freeSlots
import core.api.removeItem
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
class VarrockBartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_ASKING,
                    "Good day to you, brave adventurer. Can I get you a",
                    "refreshing beer?",
                ).also {
                    stage++
                }

            1 -> options("Yes please!", "No thanks.", "How much?").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "Yes please!").also { stage = 10 }
                    2 -> player(FaceAnim.NEUTRAL, "No thanks.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_ASKING, "How much?").also { stage = 30 }
                }

            10 -> npc(FaceAnim.FRIENDLY, "Ok then, that's two gold coins please.").also { stage++ }
            11 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendMessage(player, "You don't have enough inventory space.")
                    return true
                }
                if (!removeItem(player, Item(995, 2))) {
                    sendMessage(player, "You need two gold coins to buy a beer.")
                } else {
                    addItem(player, Items.BEER_1917, 1)
                    npc(FaceAnim.HAPPY, "Cheers!")
                }
            }

            12 -> player(FaceAnim.HAPPY, "Cheers!").also { stage = END_DIALOGUE }
            20 -> npc(FaceAnim.FRIENDLY, "Let me know if you change your mind.").also { stage = END_DIALOGUE }
            30 -> npc(FaceAnim.FRIENDLY, "Two gold pieces a pint. So, what do you say?").also { stage++ }
            31 -> options("Yes please!", "No thanks.").also { stage++ }
            32 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "Yes please!").also { stage = 10 }
                    2 -> player(FaceAnim.NEUTRAL, "No thanks.").also { stage = 20 }
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VarrockBartenderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_732, NPCs.BARTENDER_731)
    }
}
