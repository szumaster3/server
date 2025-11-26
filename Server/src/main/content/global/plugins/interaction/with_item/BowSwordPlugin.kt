package content.global.plugins.interaction.with_item

import core.api.sendDialogue
import core.game.interaction.InteractionListener
import shared.consts.Items

class BowSwordPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles try to equip bow sword.
         */

        onEquip(Items.BOW_SWORD_6818) { player, _ ->
            sendDialogue(player, "It's a bow and a sword...how am I supposed to use this?!")
            return@onEquip false
        }
    }
}