package content.region.kandarin.handlers

import content.region.kandarin.quest.elena.handlers.PlagueCityUtils
import core.api.replaceSlot
import core.api.sendItemDialogue
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

class PlagueCityCatExchange : InteractionListener {
    private val civilians = intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)
    val cats = PlagueCityUtils.grownCatItemIds.map { it.id }.toIntArray()
    private val kittens = PlagueCityUtils.kittenItemIds

    override fun defineListeners() {
        onUseWith(IntType.NPC, cats, *civilians) { player, used, _ ->
            player.familiarManager.removeDetails(used.idHash)
            sendItemDialogue(player, Items.DEATH_RUNE_560, "You hand over the cat.<br>You are given 100 Death Runes.")
            replaceSlot(player, used.asItem().slot, Item(Items.DEATH_RUNE_560, 100))
            return@onUseWith true
        }

        onUseWith(IntType.NPC, kittens, *civilians) { player, _, npc ->
            sendNPCDialogue(player, npc.id, "That kitten isn't big enough; come back when it's bigger.")
            return@onUseWith true
        }
    }
}
