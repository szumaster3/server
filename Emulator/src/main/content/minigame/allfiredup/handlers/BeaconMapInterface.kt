package content.minigame.allfiredup.handlers

import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class BeaconMapInterface : InterfaceListener {
    companion object {
        const val BEACON_MAP_575 = Components.BEACON_MAP_575
    }

    override fun defineInterfaceListeners() {
        on(BEACON_MAP_575) { _, _, _, _, _, _ ->
            return@on true
        }
    }
}
