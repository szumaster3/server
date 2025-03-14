package content.region.misthalin.dialogue.dorgeshkaan

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GundikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_NORMAL, "You want some Bat shish? Just 10gp.").also { stage++ }
            1 -> options("Yes please.", "No thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "No thanks.").also { stage = 4 }
                }

            3 -> {
                end()
                if (freeSlots(player) == 0) {
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "Looks like your hands are full. You'll have to free up some inventory space before I sell you anything.",
                    )
                } else if (amountInInventory(player, Items.COINS_995) < 10) {
                    player("But I don't have enough money on me.")
                } else {
                    npcl(FaceAnim.OLD_NORMAL, "There you go.")
                    removeItem(player, Item(Items.COINS_995, 10), Container.INVENTORY)
                    addItem(player, Items.BAT_SHISH_10964)
                }
            }

            4 -> npcl(FaceAnim.OLD_NORMAL, "Have a good day!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GundikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUNDIK_5796)
    }
}
