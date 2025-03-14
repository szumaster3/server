package content.region.fremennik.dialogue.neitiznot

import core.api.addItem
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class MawnisBurowgarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("It makes me proud to know that the helm of my", "ancestors will be worn in battle.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("I thank you on behalf of all my kinsmen Talvald.").also { stage++ }
            1 -> {
                if (player.hasItem(Item(Items.HELM_OF_NEITIZNOT_10828)) ||
                    player.hasItem(Item(Items.HELM_OF_NEITIZNOT_10828))
                ) {
                    end()
                }
                player("Ah yes, about that beautiful helmet.").also { stage++ }
            }
            2 ->
                npc(
                    "You mean the priceless heirloom that I have to you as",
                    "a sign of my trust and gratitude?",
                ).also { stage++ }
            3 -> player("Err yes, that one. I may have mislaid it.").also { stage++ }
            4 ->
                npc(
                    "It's a good job I have alert and loyal men who notice",
                    "when something like this is left lying around and picks it",
                    "up.",
                ).also {
                    stage++
                }
            5 -> npc("I'm afraid I'm going to have to charge you a", "50,000GP handling cost.").also { stage++ }
            6 -> {
                if (!removeItem(player, Item(995, 50000))) {
                    player("I don't have that much money on me.")
                    stage++
                } else {
                    end()
                    addItem(player, Items.HELM_OF_NEITIZNOT_10828)
                    sendItemDialogue(player, Items.HELM_OF_NEITIZNOT_10828, "The Burgher hands you his crown.")
                }
            }
            7 -> npc("Well, come back when you do.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MawnisBurowgarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAWNIS_BUROWGAR_5504)
    }
}
