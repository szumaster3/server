package content.global.handlers.item.scroll.note

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

class ScrawledNote : InteractionListener {
    companion object {
        private fun ScrawledNoteContent(player: Player) {
            val scrawledNoteContent =
                arrayOf(
                    "Day 1...",
                    "",
                    "I have prepared the incantations, and will invoke",
                    "the spirits of my ancestors to pay them homage.",
                    "octagram of binding.",
                    "",
                    "Day 2...",
                    "",
                    "What have I done?",
                    "My spirit is overthrown by feelings of fear and evil.",
                    "I feel helpless, and weak... from my teachings, I know -",
                )

            sendString(player, scrawledNoteContent.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
        }
    }

    override fun defineListeners() {
        on(Items.SCRAWLED_NOTE_717, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222).also {
                ScrawledNoteContent(
                    player,
                )
            }
            return@on true
        }
    }
}
