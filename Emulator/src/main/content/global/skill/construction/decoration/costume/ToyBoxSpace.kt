package content.global.skill.construction.decoration.costume

import content.global.handlers.iface.DiangoReclaimInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class ToyBoxSpace : InteractionListener {
    override fun defineListeners() {
        on(Scenery.TOY_BOX_18802, IntType.SCENERY, "open") { player, _ ->
            DiangoReclaimInterface.open(player)
            return@on true
        }
    }
}
