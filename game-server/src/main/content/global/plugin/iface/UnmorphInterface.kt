package content.global.plugin.iface

import core.api.playAudio
import core.api.restoreTabs
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components

class UnmorphInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onClose(Components.UNMORPH_375) { player, _ ->
            Component(Components.UNMORPH_375)
                .setUncloseEvent { player: Player, c: Component? ->
                    c?.open(player)
                    return@setUncloseEvent true
                }.also {
                    player.unlock()
                    playAudio(player, 1521)
                    player.appearance.transformNPC(-1)
                    restoreTabs(player)
                }
            return@onClose true
        }
    }
}
