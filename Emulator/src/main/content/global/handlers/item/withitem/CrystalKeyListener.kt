package content.global.handlers.item.withitem

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class CrystalKeyListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.LOOP_HALF_OF_A_KEY_987, Items.TOOTH_HALF_OF_A_KEY_985) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.CRYSTAL_KEY_989)
                sendMessage(player, "You join the two halves of the key together.")
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
