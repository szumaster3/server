package content.global.skill.herblore

import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class SwampToadListener : InteractionListener {

    override fun defineListeners() {
        on(Items.SWAMP_TOAD_2150, IntType.ITEM, "remove-legs") { player, item ->
            val replaced = replaceSlot(player, item.index, Item(Items.TOADS_LEGS_2152, 1))
            if (replaced != null) {
                sendMessage(player, "You pull the legs off the toad. Poor toad. At least they'll grow back.")
            }
            return@on true
        }
    }
}