package content.global.plugin.scenery

import core.api.getSceneryName
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class LookAtPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles interaction with footprints.
         */

        for (i in Scenery.FOOTPRINTS_18877..Scenery.FOOTPRINTS_18900) {
            on(i, IntType.SCENERY, "look at") { player, _ ->
                sendMessage(player, "The ${getSceneryName(i).lowercase()} seem to be going south-west.")
                return@on true
            }
        }
    }
}
