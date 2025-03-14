package content.region.karamja.dialogue.shilovillage

import core.api.addItem
import core.api.freeSlots
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
class SeravelDialogue(
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
            0 ->
                npc(
                    "Hello Bwana. Are you interested in buying a ticket",
                    "for the 'Lady of the Waves'?",
                ).also { stage++ }

            1 -> options("Yes, that sounds great!", "No thanks.", "Tell me more about the ship.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Yes, that sounds great!").also { stage = 10 }
                    2 -> player("No thanks.").also { stage = 20 }
                    3 -> player("Tell me more about the ship.").also { stage = 30 }
                }
            10 -> {
                end()
                if (freeSlots(player) == 0) {
                    npc("Sorry Bwana, you don't have enough space. Come back", "when you do!")
                    return true
                }
                if (!removeItem(player, Item(Items.COINS_995, 25))) {
                    npc("Sorry Bwana, you don't have enough money. Come back", "when you have 25 gold pieces.")
                } else {
                    npc("Great, nice doing business with you.")
                    addItem(player, Items.SHIP_TICKET_621)
                }
            }
            20 -> npc("Fair enough Bwana, let me know if you change your", "mind.").also { stage = END_DIALOGUE }
            30 ->
                npc(
                    "It's a ship that can take you to either Port Sarim",
                    "or Port Khazard. The ship lies west of Shilo Village",
                    "and south of Cairn Island.",
                ).also {
                    stage++
                }
            31 -> npc("The tickets cost 25 Gold Pieces. Would you like to", "purchase a ticket Bwana?").also { stage++ }
            32 -> options("Yes, that sounds great!", "No thanks.").also { stage = 1 }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SERAVEL_514)
    }
}
