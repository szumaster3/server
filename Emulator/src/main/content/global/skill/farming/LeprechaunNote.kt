package content.global.skill.farming

import core.api.sendItemDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.NPCs

class LeprechaunNote : InteractionListener {
    val CROPS = Plantable.values().map { it.harvestItem }.toIntArray()
    val LEPRECHAUNS =
        intArrayOf(NPCs.TOOL_LEPRECHAUN_3021, NPCs.GOTH_LEPRECHAUN_8000, NPCs.TOOL_LEPRECHAUN_4965, NPCs.TECLYN_2861)

    override fun defineListeners() {
        onUseWith(IntType.NPC, CROPS, *LEPRECHAUNS) { player, used, with ->
            val usedItem = used.asItem()
            val npc = with.asNpc()
            val expr =
                when (npc.id) {
                    3021 -> FaceAnim.OLD_NORMAL
                    else -> FaceAnim.FRIENDLY
                }

            if (usedItem.noteChange != usedItem.id) {
                val amt = player.inventory.getAmount(usedItem.id)
                if (player.inventory.remove(Item(usedItem.id, amt))) {
                    player.inventory.add(Item(usedItem.noteChange, amt))
                }
                sendItemDialogue(player, usedItem.id, "The leprechaun exchanges your items for banknotes.")
            } else {
                player.dialogueInterpreter.sendDialogues(npc.id, expr, "That IS a banknote!")
            }

            return@onUseWith true
        }
    }
}
