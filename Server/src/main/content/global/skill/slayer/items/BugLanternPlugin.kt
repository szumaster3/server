package content.global.skill.slayer.items

import core.api.getStatLevel
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

/**
 * Handles use the bug lantern.
 */
class BugLanternPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles lighting the unlit bug lantern using a tinderbox.
         */

        onUseWith(IntType.ITEM, UNLIT_LANTERN, Items.TINDERBOX_590) { player, used, _ ->
            if (getStatLevel(player, Skills.FIREMAKING) < REQUIRED_FIREMAKING) {
                sendMessage(player, "You need a Firemaking level of at least $REQUIRED_FIREMAKING in order to do this.")
                return@onUseWith true
            }

            replaceSlot(player, used.index, Item(LIT_LANTERN, 1))
            sendMessage(player, "You light the bug lantern.")
            return@onUseWith true
        }

        /*
         * Handles extinguishing the lit bug lantern.
         */

        on(LIT_LANTERN, IntType.ITEM, "extinguish") { player, item ->
            replaceSlot(player, item.index, Item(UNLIT_LANTERN, 1))
            sendMessage(player, "You extinguish the bug lantern.")
            return@on true
        }
    }

    companion object {
        private const val UNLIT_LANTERN = Items.UNLIT_BUG_LANTERN_7051
        private const val LIT_LANTERN = Items.LIT_BUG_LANTERN_7053
        private const val REQUIRED_FIREMAKING = 33
    }
}
