package content.global.handlers.item.scroll.note

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

class TheFeudNote : InteractionListener {
    companion object {
        private fun NotesTheFeud(player: Player) {
            val notesthefeudContent =
                arrayOf(
                    "These numbers contained within,",
                    "A sequence age old begin,",
                    "Have a look,",
                    "Where do they fit in?",
                    "1,1,2,3,5....",
                )

            sendString(player, notesthefeudContent.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
        }
    }

    override fun defineListeners() {
        on(Items.NOTE_4597, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222).also { NotesTheFeud(player) }
            return@on true
        }
    }
}
