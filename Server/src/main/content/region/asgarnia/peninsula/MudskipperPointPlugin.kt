package content.region.asgarnia.peninsula

import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class MudskipperPointPlugin : InteractionListener {

    override fun defineListeners() {

        on(Scenery.SIGNPOST_10090, IntType.SCENERY, "read") { player, _ ->
            sendDialogue(player, "${core.tools.RED}BEWARE OF THE MUDSKIPPERS!")
            return@on true
        }
    }
}