package content.global.skill.farming

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.NPCs

class LeprechaunNote : InteractionListener {
    private val cropId = Plantable.values().map { it.harvestItem }.toIntArray()
    private val tooLeprechaun =
        intArrayOf(NPCs.TOOL_LEPRECHAUN_3021, NPCs.GOTH_LEPRECHAUN_8000, NPCs.TOOL_LEPRECHAUN_4965, NPCs.TECLYN_2861)

    override fun defineListeners() {
        onUseWith(IntType.NPC, cropId, *tooLeprechaun) { player, used, with ->
            val usedItem = used.asItem()
            val npc = with.asNpc()
            val facialExpression = when (npc.id) {
                NPCs.TOOL_LEPRECHAUN_3021 -> FaceAnim.OLD_NORMAL
                else -> FaceAnim.FRIENDLY
            }

            if (usedItem.noteChange != usedItem.id) {
                val amt = amountInInventory(player, usedItem.id)
                if (removeItem(player, Item(usedItem.id, amt))) {
                    addItem(player, usedItem.noteChange, amt)
                }
                sendItemDialogue(player, usedItem.id, "The leprechaun exchanges your ${usedItem.name.lowercase()} for a banknote.")
            } else {
                sendNPCDialogue(player, npc.id, "Nay, I've got no banknotes to exchange for that item.", facialExpression)
            }
            return@onUseWith true
        }
    }
}
