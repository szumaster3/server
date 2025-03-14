package content.region.misc.dialogue.zanaris

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
class LunderwinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.OLD_CALM_TALK1,
            "Buying cabbage am I, not have such thing where I from.",
            "Will pay money much handsome for wondrous object,",
            "cabbage you called. Say I 100 gold coins each fair price",
            "to be giving yes?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inInventory(player, Items.CABBAGE_1965)) {
                    player("Alas, I have no cabbages either...").also { stage++ }
                } else {
                    options("Yes, I'll sell you all my cabbages.", "No, I will keep my cabbbages.").also { stage = 2 }
                }

            1 -> npc(FaceAnim.OLD_CALM_TALK1, "Pity be that, I want badly do.").also { stage = END_DIALOGUE }
            2 ->
                when (buttonId) {
                    1 -> player("Yes, I'll sell you all my cabbages.").also { stage++ }
                    2 -> player("No, I will keep my cabbbages.").also { stage = END_DIALOGUE }
                }

            3 -> {
                val invAmount = amountInInventory(player!!, Items.CABBAGE_1965)
                if (removeItem(player, Item(Items.CABBAGE_1965, invAmount), Container.INVENTORY)) {
                    addItem(player, Items.COINS_995, invAmount * 100)
                    npcl(FaceAnim.OLD_CALM_TALK1, "Business good doing with you! Please, again come, buying always.")
                } else {
                    end()
                }
                stage = END_DIALOGUE
            }

            4 -> npcl(FaceAnim.OLD_SAD, "Pity be that, I want badly do.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return LunderwinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUNDERWIN_565)
    }
}
