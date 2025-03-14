package content.region.desert.dialogue.alkharid

import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class KarimDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "Would you like to buy a nice kebab? Only one gold.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("I think I'll give it a miss.", "Yes please.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I think I'll give it a miss.").also { stage = END_DIALOGUE }
                    2 -> player(FaceAnim.HAPPY, "Yes please.").also { stage++ }
                }

            2 -> {
                end()
                if (player.inventory.freeSlots() == 0) {
                    player(FaceAnim.HALF_GUILTY, "I don't have enough room, sorry.")
                } else if (!player.inventory.contains(995, 1)) {
                    sendMessage(player, "You need 1 gp to buy a kebab.")
                } else {
                    player.inventory.remove(Item(995, 1))
                    player.inventory.add(Item(1971, 1))
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KarimDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KARIM_543)
    }
}
