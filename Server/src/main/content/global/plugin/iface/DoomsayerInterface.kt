package content.global.plugin.iface

import core.api.getVarbit
import core.api.sendMessage
import core.api.sendMessages
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.WarningManager
import core.game.node.entity.player.link.Warnings
import shared.consts.Components

/**
 * Represents the warning interface.
 * @author szu
 */
class DoomsayerInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        /*
         * Handles toggle of warnings.
         */

        on(Components.CWS_DOOMSAYER_583) { player, _, _, buttonID, _, _ ->
            val warning = Warnings.values().find { it.buttonId == buttonID }

            if (buttonID == 81) {
                sendMessage(player, "This option is only available on PVP worlds.")
                return@on true
            }

            if (warning != null) {
                if (getVarbit(player, warning.varbit) < 6) {
                    sendMessages(player, "You cannot toggle the warning screen on or off. You need to go to the area it", "is linked to enough times to have the option to do so.")
                    return@on true
                }

                val isDisbled = WarningManager.isWarningDisabled(player, warning)
                WarningManager.toggleWarning(player, warning.component)
                sendMessage(player, "You have ${if (!isDisbled) "disabled" else "enabled"} the warning.")
            }
            return@on true
        }
    }
}
