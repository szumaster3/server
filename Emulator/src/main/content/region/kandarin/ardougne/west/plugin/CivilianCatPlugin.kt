package content.region.kandarin.ardougne.west.plugin

import content.global.handlers.item.withnpc.grownCatItemIds
import content.global.handlers.item.withnpc.kittenItemIds
import core.api.replaceSlot
import core.api.sendItemDialogue
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

class CivilianCatPlugin : InteractionListener {
    private val civilians = intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)

    override fun defineListeners() {
        onUseWith(IntType.NPC, grownCatItemIds, *civilians) { player, used, _ ->
            player.familiarManager.removeDetails(used.idHash)
            sendItemDialogue(player, Items.DEATH_RUNE_560, "You hand over the cat.<br>You are given 100 Death Runes.")
            replaceSlot(player, used.asItem().slot, Item(Items.DEATH_RUNE_560, 100))
            return@onUseWith true
        }

        onUseWith(IntType.NPC, kittenItemIds, *civilians) { player, _, npc ->
            sendNPCDialogue(player, npc.id, "That kitten isn't big enough; come back when it's bigger.")
            return@onUseWith true
        }
    }
}
