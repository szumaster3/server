package content.global.plugin.iface

import core.game.interaction.InterfaceListener
import org.rs.consts.Components

/**
 * Handles the death interface.
 * @author Vexia
 */
class DeathInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.AIDE_DEATH_153) { player, _, _, buttonID, _, _ ->
            if (buttonID == 1) {
                player.getSavedData().globalData.setDisableDeathScreen(true)
                player.interfaceManager.close()
            }
            return@on true
        }
    }
}
