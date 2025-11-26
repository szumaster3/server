package content.global.plugins.interfaces.player_kit

import content.region.island.tutorial.plugin.CharacterDesign
import core.api.openOverlay
import core.game.interaction.InterfaceListener
import shared.consts.Components

/**
 * Listener for character design.
 */
class CharacterDesignInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.APPEARANCE_771) { player, _ ->
            if(player.interfaceManager.isResizable) openOverlay(player, 333)
            return@onOpen true
        }
        on(Components.APPEARANCE_771) { player, _, _, buttonID, _, _ ->
            CharacterDesign.handleButtons(player, buttonID)
            return@on true
        }
    }
}