package content.global.handlers.item.withitem

import core.api.sendDialogue
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class BowSwordListener : InteractionListener {

    override fun defineListeners() {
        onEquip(Items.BOW_SWORD_6818) { player, node ->
            sendDialogue(player, "It's a bow and a sword...how am I supposed to use this?!")
            return@onEquip false
        }
    }
}