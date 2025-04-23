package content.global.handlers.item.boltpouch

import core.api.openInterface
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class BoltPouchListener : InteractionListener {

    val bolts = intArrayOf(877,878,6061,6062,879,9236,9139,9141,9288,9295,9302,9336,9239,9236,9237,9238,9239,9240,9241,9242,9243,9244,9245,9145,13083,13084,13085,13086,9142,9289,9296,9303,9337,9240,9338,9241,9143,9290,9297,9304,9339,9242,9340,9243,9144,9291,9298,9305,9341,9244,9342,9245,9140,13280,9139,9286,9293,9300,9287,9294,9301,9292,9299,9306,9335,9237,880,13083,13084,13085,13086)

    override fun defineListeners() {

        /*
         * Handles opening bolt pouch interface.
         */

        on(Items.BOLT_POUCH_9433, IntType.ITEM, "open") { player, _ ->
            openInterface(player, Components.XBOWS_POUCH_433)
            return@on true
        }

        /*
         * Handles adding bolts to the pouch.
         */

        onUseWith(IntType.ITEM, bolts, Items.BOLT_POUCH_9433) { player, item, otherItem ->
            val pouchItem = if (item.id == Items.BOLT_POUCH_9433) item else otherItem
            val boltItem = if (pouchItem == item) otherItem else item

            val success = BoltPouch.storeBolt(player, boltItem.asItem())

            if (!success) {
                sendMessage(player, "You don't have space to store that type of bolt.")
            }

            return@onUseWith true
        }
    }
}