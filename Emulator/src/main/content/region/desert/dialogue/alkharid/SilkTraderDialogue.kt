package content.region.desert.dialogue.alkharid

import core.api.*
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
class SilkTraderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Do you want to buy any fine silks?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("How much are they?", "No, silk doesn't suit me.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "How much are they?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "No, silk doesn't suit me.").also { stage = END_DIALOGUE }
                }

            2 -> npc(FaceAnim.NEUTRAL, "3gp.").also { stage++ }
            3 -> options("No, that's too much for me.", "Okay, that sounds good.").also { stage++ }
            4 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "No, that's too much for me.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Okay, that sounds good.").also { stage = 10 }
                }
            5 -> npc(FaceAnim.HALF_GUILTY, "2 gold coins and that's as low as I'll go.").also { stage++ }
            6 ->
                npc(
                    FaceAnim.ANNOYED,
                    "I'm not selling it for any less. You'll only go and sell it",
                    "in Varrock for a profit.",
                ).also {
                    stage++
                }
            7 -> options("2 gold coins sounds good.", "No really, I don't want it.").also { stage++ }
            8 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "2 gold coins sounds good.").also { stage++ }
                    2 -> end()
                }
            9 -> {
                end()
                if (freeSlots(player) == 0) {
                    player(FaceAnim.HALF_GUILTY, "I don't have enough room, sorry.")
                } else if (amountInInventory(player, Items.COINS_995) < 2) {
                    sendDialogue(player, "You need 2 gold coins to buy silk.")
                } else {
                    removeItem(player, Item(Items.COINS_995, 2))
                    addItem(player, Items.SILK_950, 1)
                    sendDialogue(player, "You buy some silk for 2 gold coins.")
                }
            }

            10 -> {
                end()
                if (freeSlots(player) == 0) {
                    player(FaceAnim.HALF_GUILTY, "I don't have enough room, sorry.")
                } else if (amountInInventory(player, Items.COINS_995) < 3) {
                    sendDialogue(player, "You need 3 gold coins to buy silk.")
                } else {
                    removeItem(player, Item(Items.COINS_995, 3))
                    addItem(player, Items.SILK_950, 1)
                    sendDialogue(player, "You buy some silk for 3 gold coins.")
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SilkTraderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SILK_TRADER_539)
    }
}
