package content.global.plugin.item.withobject

import core.api.removeItem
import core.api.setVarbit
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class GWDEntranceRopePlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles using rope on gwd entrance.
         */

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.HOLE_26340) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            setVarbit(player, 3932, 1)
            return@onUseWith true
        }
    }
}
