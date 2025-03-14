package content.global.skill.crafting.items.armour.snelms

import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class SnelmCraftingListener : InteractionListener {
    private val shellIDs = Snelm.values().map(Snelm::shell).toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, shellIDs, Items.CHISEL_1755) { player, used, _ ->
            val item = Snelm.fromShellId(used.id) ?: return@onUseWith true

            submitIndividualPulse(
                entity = player,
                pulse = SnelmCraftingPulse(player, used.asItem(), item),
            )

            return@onUseWith true
        }
    }
}
