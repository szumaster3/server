package content.global.handlers.item.scroll.note

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

class ScrumpledNote : InteractionListener {
    companion object {
        private fun ScrumpledNoteContent(player: Player) {
            val scrumpledNoteContent =
                arrayOf(
                    "Day 4 ...",
                    "",
                    "These days come so fleetingly,",
                    "I have no idea how long I have been here now...",
                    "",
                    "Day 5...",
                    "",
                    "A wizened charm will release me,",
                    "but never magic that would harm ...",
                )

            sendString(player, scrumpledNoteContent.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
        }
    }

    override fun defineListeners() {
        on(Items.SCRUMPLED_NOTE_719, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222).also { ScrumpledNoteContent(player) }
            return@on true
        }
    }
}
