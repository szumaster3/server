package content.global.skill.magic.iface

import core.api.getAttribute
import core.api.lock
import core.api.teleport
import core.api.visualize
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.TeleportManager.TeleportType
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Graphics

class TeleotherInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.TP_OTHER_326) { player, _, _, buttonID, _, _ ->
            if(buttonID == 5) {
                lock(player, 2)
                if (teleport(player, getAttribute(player, "t-o_location", player.location), TeleportType.TELE_OTHER)) {
                    visualize(player, Animations.OLD_SHRINK_AND_RISE_UP_TP_1816, Graphics.TELEOTHER_ACCEPT_342)
                }
            }
            player.interfaceManager.close()
            return@on true
        }
    }
}
