package content.region.fremennik.dialogue.miscellania

import core.api.addItemOrDrop
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FlowerGirlDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.NEUTRAL, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.ASKING, "Good day. What are you doing?").also { stage++ }
            1 -> npc(FaceAnim.NEUTRAL, "I'm selling flowers, 15gp for three. Would you like some?").also { stage++ }
            2 -> options("Yes, please.", "No, thank you.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> {
                        if (inInventory(player, 995, 15)) {
                            npc(FaceAnim.HAPPY, "Thank you! Here you go.").also { stage = END_DIALOGUE }
                            player.inventory.remove(Item(995, 15))
                            addItemOrDrop(player, 2460, 1)
                        } else {
                            player(FaceAnim.HALF_THINKING, "I'm sorry, but I don't have 15gp.").also {
                                stage = END_DIALOGUE
                            }
                        }
                    }

                    2 -> player(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FlowerGirlDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FLOWER_GIRL_1378)
    }
}
