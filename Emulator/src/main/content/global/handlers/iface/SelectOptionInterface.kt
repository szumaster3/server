package content.global.handlers.iface

import core.api.ui.repositionChild
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class SelectOptionInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.SELECT_AN_OPTION_140) { player, _ ->
            repositionChild(player, Components.SELECT_AN_OPTION_140, 0, 23, 5)
            repositionChild(player, Components.SELECT_AN_OPTION_140, 2, 31, 32)
            repositionChild(player, Components.SELECT_AN_OPTION_140, 3, 234, 32)
            repositionChild(player, Components.SELECT_AN_OPTION_140, 4, 24, 3)
            repositionChild(player, Components.SELECT_AN_OPTION_140, 5, 123, 36)
            repositionChild(player, Components.SELECT_AN_OPTION_140, 6, 334, 36)
            return@onOpen true
        }
    }
}
