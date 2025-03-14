package content.region.misthalin.dialogue.varrock

import core.api.removeItem
import core.api.sendDialogueOptions
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
class TrampDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Got any spare change, mate?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "Yes, I can spare a little money.",
                    "Sorry, you'll have to earn it yourself.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes, I can spare a little money.").also { stage = 2 }
                    2 ->
                        player(FaceAnim.FURIOUS, "Sorry, you'll have to earn it yourself, just like I did.").also {
                            stage = 3
                        }
                }
            2 -> {
                end()
                if (!removeItem(player, Item(Items.COINS_995, 1))) {
                    sendMessage(player, "You only need one coin to give to this tramp.")
                } else {
                    npc(FaceAnim.HALF_GUILTY, "Thanks mate!")
                }
            }
            3 -> npc(FaceAnim.HALF_GUILTY, "Please yourself.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TrampDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRAMP_11)
    }
}
