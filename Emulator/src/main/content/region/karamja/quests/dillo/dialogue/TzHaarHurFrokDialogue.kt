package content.region.karamja.quests.dillo.dialogue

import core.api.*
import core.api.ui.closeDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class TzHaarHurFrokDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_FRIENDLY, "Are tunnels fixed and TzHaar-Hur safe yet?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Can you treat these oak planks for me?", "No, not yet.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(
                    FaceAnim.ASKING, "Can you treat these oak planks for me? I need them",
                    "to be fire-resistant before I can start repairing the",
                    "tunnels."
                ).also { stage++ }

                2 -> player(FaceAnim.HALF_GUILTY, "No, not yet.").also { stage = END_DIALOGUE }
            }

            2 -> npc(FaceAnim.CHILD_FRIENDLY, "Yes, hand them over and I can take care of it.").also { stage++ }
            3 -> options("Here you go.", "I've changed my mind.").also { stage++ }
            4 -> when (buttonId) {
                1 -> {
                    closeDialogue(player)
                    if (!inInventory(player, Items.OAK_PLANK_8778)) {
                        sendMessage(player, "You don't have the right materials.")
                        return true
                    }
                    lock(player, 4)
                    sendMessage(player, "You hand over your planks to TzHaar-Hur-Frok...")
                    queueScript(player, 3, QueueStrength.SOFT) {
                        var amount = amountInInventory(player, Items.OAK_PLANK_8778)
                        if (removeItem(player, Item(Items.OAK_PLANK_8778, amount))) {
                            addItem(player, Items.TREATED_OAK_PLANK_13238, amount)
                            sendMessage(player, "...TzHaar-Hur-Frok treats the planks and hands them back.")
                        }
                        return@queueScript stopExecuting(player)
                    }
                }
                2 -> end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TZHAAR_HUR_FROK_7748)
}