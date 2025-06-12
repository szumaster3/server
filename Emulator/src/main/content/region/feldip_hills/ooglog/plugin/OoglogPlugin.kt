package content.region.feldip_hills.ooglog.plugin

import content.region.feldip_hills.ooglog.dialogue.GrimechinDialogue
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class OoglogPlugin : InteractionListener {

    companion object {
        private const val GRIMECHIN = Scenery.GRIMECHIN_29106
    }

    override fun defineListeners() {
        on(GRIMECHIN, IntType.SCENERY, "Talk-to") { player, _ ->
            openDialogue(player, GrimechinDialogue())
            return@on true
        }
    }
}
