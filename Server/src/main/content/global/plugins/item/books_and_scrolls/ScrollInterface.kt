package content.global.plugins.item.books_and_scrolls

import core.api.closeInterface
import core.api.openInterface
import core.game.node.entity.player.Player
import shared.consts.Components

/**
 * Represents the scroll interface.
 * @author ovenbreado
 */
class ScrollInterface {
    companion object {
        private val MESSAGESCROLL_220_LINE_IDS = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)

        fun scrollSetup(player: Player, scrollComponent: Int, contents: Array<ScrollLine>) {
            closeInterface(player)
            if (scrollComponent == Components.MESSAGESCROLL_220) {
                openInterface(player, Components.MESSAGESCROLL_220)
                setPageContent(player, Components.MESSAGESCROLL_220, MESSAGESCROLL_220_LINE_IDS, contents)
            }
        }

        fun setPageContent(player: Player, componentId: Int, scrollLineIds: Array<Int>, contents: Array<ScrollLine>) {
            for (line in contents) {
                if (scrollLineIds.contains(line.child)) {
                    player.packetDispatch.sendString(line.message, componentId, line.child)
                }
            }
        }
    }
}

class ScrollLine(
    val message: String,
    val child: Int,
)
