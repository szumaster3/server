package content.region.karamja.dialogue.shilovillage

import core.api.removeItem
import core.api.setAttribute
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
class YohnusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "Sorry but the blacksmiths is closed. But I can let you",
                    "use the furnace at the cost of 20 gold pieces.",
                ).also {
                    stage++
                }
            1 -> options("Use furnace - 20 gold coins", "No thanks!").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> {
                        if (!removeItem(player, Item(Items.COINS_995, 20))) {
                            player("Err, I seem to not have enough gold.").also { stage = END_DIALOGUE }
                        } else {
                            npc(FaceAnim.FRIENDLY, "Thanks Bwana! Enjoy the facilities!").also { stage = END_DIALOGUE }
                            setAttribute(player, "shilo-village:blacksmith-doors", true)
                        }
                    }

                    2 -> player("No Thanks!").also { stage++ }
                }

            3 -> npc("Very well Bwana, have a nice day.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.YOHNUS_513)
    }
}
