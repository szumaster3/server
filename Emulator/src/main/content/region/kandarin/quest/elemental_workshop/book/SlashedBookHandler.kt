package content.region.kandarin.quest.elemental_workshop.book

import content.global.handlers.iface.BookInterface
import content.region.kandarin.quest.elemental_workshop.handlers.EWUtils
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class SlashedBookHandler : InteractionListener {
    companion object {
        private val TITLE = "Book of the elemental shield"
        private val CONTENTS = EWUtils.PAGES

        @Suppress("UNUSED_PARAMETER")
        private fun display(
            player: Player,
            pageNum: Int,
            buttonID: Int,
        ): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.SLASHED_BOOK_9715, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
            return@on true
        }
    }
}
