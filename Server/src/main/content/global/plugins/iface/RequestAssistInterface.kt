package content.global.plugins.iface

import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.request.assist.AssistSessionPulse
import shared.consts.Components

/**
 * Represents the Request assist interface.
 * @see AssistSessionPulse
 */
class RequestAssistInterface : InterfaceListener {
    override fun defineInterfaceListeners() {

        /*
         * Handles request assist interface.
         */

        on(Components.REQ_ASSIST_301) { player, _, _, buttonID, _, _ ->
            val session = AssistSessionPulse.getExtension(player) ?: return@on true
            if (player != session.player) {
                return@on true
            }
            val buttonIndex = (buttonID - 15) / 5
            if (buttonIndex in 0..8) {
                session.toggleButton(buttonIndex.toByte())
            }
            session.refresh()
            return@on true
        }
    }
}
