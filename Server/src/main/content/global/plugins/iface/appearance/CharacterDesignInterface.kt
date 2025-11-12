package content.global.plugins.iface.appearance

import content.region.island.tutorial.plugin.CharacterDesign
import core.game.interaction.InterfaceListener
import shared.consts.Components

/**
 * Listener for character design.
 */
class CharacterDesignInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.APPEARANCE_771) { player, _, _, buttonID, _, _ ->
            CharacterDesign.handleButtons(player, buttonID)
            return@on true
        }
    }
}
