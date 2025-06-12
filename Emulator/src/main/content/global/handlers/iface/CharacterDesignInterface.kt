package content.global.handlers.iface

import content.region.islands.tutorial_island.plugin.CharacterDesign
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

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
