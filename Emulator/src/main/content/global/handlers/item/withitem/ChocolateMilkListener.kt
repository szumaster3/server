package content.global.handlers.item.withitem

import core.api.addItem
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class ChocolateMilkListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CHOCOLATE_DUST_1975, Items.BUCKET_OF_MILK_1927) { player, _, _ ->
            if (removeItem(player, Items.CHOCOLATE_DUST_1975) && removeItem(player, Items.BUCKET_OF_MILK_1927)) {
                sendItemDialogue(player, Items.CHOCOLATEY_MILK_1977, "You mix the chocolate into the bucket.")
                addItem(player, Items.CHOCOLATEY_MILK_1977)
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
