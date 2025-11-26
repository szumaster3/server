package content.global.plugins.interfaces

import core.api.closeDialogue
import core.game.interaction.InterfaceListener
import shared.consts.Components

class MessageInterface : InterfaceListener{
    override fun defineInterfaceListeners() {
        on(Components.MESSAGE5_214) { player, _, _, buttonID, _, _ ->
            if (buttonID == 6) closeDialogue(player)
            return@on true
        }
    }


}