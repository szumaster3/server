package content.global.handlers.iface

import core.api.ui.closeDialogue
import core.api.ui.repositionChild
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class TutorialInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.TUTORIAL_TEXT_372) { player, _ ->
            repositionChild(player, Components.TUTORIAL_TEXT_372, 1, 10, 35)
            repositionChild(player, Components.TUTORIAL_TEXT_372, 2, 10, 50)
            repositionChild(player, Components.TUTORIAL_TEXT_372, 3, 10, 65)
            repositionChild(player, Components.TUTORIAL_TEXT_372, 4, 10, 80)
            return@onOpen true
        }

        on(Components.MESSAGE5_214) { player, _, _, buttonID, _, _ ->
            if (buttonID == 6) closeDialogue(player)
            return@on true
        }

        onOpen(Components.TUTORIAL_TEXT2_421) { player, _ ->
            repositionChild(player, Components.TUTORIAL_TEXT2_421, 0, 19, 21)
            return@onOpen true
        }
    }
}
