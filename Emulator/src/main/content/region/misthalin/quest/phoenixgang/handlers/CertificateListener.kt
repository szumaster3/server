package content.region.misthalin.quest.phoenixgang.handlers

import core.api.removeItem
import core.api.replaceSlot
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class CertificateListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.HALF_CERTIFICATE_11173, Items.HALF_CERTIFICATE_11174) { player, used, with ->
            if (removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(Items.CERTIFICATE_769))
            }
            return@onUseWith true
        }
    }
}
