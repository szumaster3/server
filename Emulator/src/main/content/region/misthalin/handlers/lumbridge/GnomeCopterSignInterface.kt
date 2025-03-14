package content.region.misthalin.handlers.lumbridge

import core.api.closeTabInterface
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class GnomeCopterSignInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.CARPET_INFO_723) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                11 -> closeTabInterface(player)
            }
            return@on true
        }
    }
}
