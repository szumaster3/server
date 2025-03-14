package content.global.handlers.item.scroll.note

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

class FibonacciNote : InteractionListener {
    companion object {
        private fun NotesTheFeud(player: Player) {
            val notesthefeudContent =
                arrayOf("The piece of paper has the word 'Fibonacci'", "scrawled on it.")
            sendString(player, notesthefeudContent.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
        }
    }

    override fun defineListeners() {
        on(Items.NOTE_4598, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222).also { NotesTheFeud(player) }
            return@on true
        }
    }
}
