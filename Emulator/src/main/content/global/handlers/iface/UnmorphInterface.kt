package content.global.handlers.iface

import core.api.ui.restoreTabs
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components

class UnmorphInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onClose(Components.UNMORPH_375) { player, _ ->
            Component(Components.UNMORPH_375)
                .setCloseEvent { player: Player, c: Component? ->
                    c?.open(player)
                    return@setCloseEvent true
                }.also {
                    player.unlock()
                    player.appearance.transformNPC(-1)
                    restoreTabs(player)
                }
            return@onClose true
        }
    }
}
