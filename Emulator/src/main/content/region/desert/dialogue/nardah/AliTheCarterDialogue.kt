package content.region.desert.dialogue.nardah

import core.api.addItemOrDrop
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AliTheCarterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Hello, friend! Welcome to Nardah.", "Do you happen to be in need of water?").also { stage++ }
            1 -> options("Yes I am!", "No thank you I'm good.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Yes I am!").also { stage++ }
                    2 -> player("No thank you.").also { stage = END_DIALOGUE }
                }

            3 -> npc("It'll be 1000 coins for a full waterskin.").also { stage++ }
            4 -> options("Oh, wow. Um, sure.", "Oh my! No thank you.").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> player("Oh, wow. Um, sure.").also { stage++ }
                    2 -> player("Oh my! No thank you.").also { stage = END_DIALOGUE }
                }

            6 -> {
                if (!removeItem(player, Item(Items.COINS_995, 1000))) {
                    player("Err, I seem to not have enough gold.").also { stage++ }
                } else {
                    end()
                    addItemOrDrop(player, Items.WATERSKIN4_1823, 1)
                }
            }

            7 -> npc("Too bad, friend.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheCarterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_CARTER_3030)
    }
}
