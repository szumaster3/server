package content.global.handlers.iface

import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.request.assist.AssistSession
import org.rs.consts.Components

class RequestAssistInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.REQ_ASSIST_301) { player, _, _, buttonID, _, _ ->
            val session = AssistSession.getExtension(player) ?: return@on true
            if (player !== session.player) {
                return@on true
            }

            when (buttonID) {
                15 -> session.toggleButton(0.toByte())
                20 -> session.toggleButton(1.toByte())
                25 -> session.toggleButton(2.toByte())
                30 -> session.toggleButton(3.toByte())
                35 -> session.toggleButton(4.toByte())
                40 -> session.toggleButton(5.toByte())
                45 -> session.toggleButton(6.toByte())
                50 -> session.toggleButton(7.toByte())
                55 -> session.toggleButton(8.toByte())
            }
            session.refresh()
            return@on true
        }
    }
}
