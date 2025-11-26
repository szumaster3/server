package content.global.plugins.interfaces

import core.api.repositionChild
import core.game.interaction.InterfaceListener
import shared.consts.Components

class TutorialTextInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.TUTORIAL_TEXT_372) { player, _ ->
            val indices = intArrayOf(1, 2, 3, 4)
            val xs = intArrayOf(10, 10, 10, 10)
            val ys = intArrayOf(35, 50, 65, 80)

            for (i in indices.indices) {
                repositionChild(player, Components.TUTORIAL_TEXT_372, indices[i], xs[i], ys[i])
            }
            return@onOpen true
        }

        onOpen(Components.TUTORIAL_TEXT2_421) { player, _ ->
            repositionChild(player, Components.TUTORIAL_TEXT2_421, 0, 19, 21)
            return@onOpen true
        }
    }


}