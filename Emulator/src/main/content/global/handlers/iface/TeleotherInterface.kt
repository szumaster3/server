package content.global.handlers.iface

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.TeleportManager.TeleportType
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Graphics

class TeleotherInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.TP_OTHER_326, 5) { player, _, _, _, _, _ ->
            lock(player, 2)
            if (teleport(player, getAttribute(player, "t-o_location", player.location), TeleportType.TELE_OTHER)) {
                visualize(player, Animations.OLD_SHRINK_AND_RISE_UP_TP_1816, Graphics.TELEOTHER_ACCEPT_342)
            }
            closeInterface(player)
            return@on true
        }
    }
}
