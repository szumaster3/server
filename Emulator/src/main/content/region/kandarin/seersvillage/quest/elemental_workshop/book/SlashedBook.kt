package content.region.kandarin.seersvillage.quest.elemental_workshop.book

import content.global.plugin.iface.BookInterface
import content.region.kandarin.seersvillage.quest.elemental_workshop.plugin.EWUtils
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class SlashedBook : InteractionListener {
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
            sendMessage(player, "The book has two parts: an introduction and an instruction section.")
            sendMessage(player, "You flip the book open to the introduction and start reading.")
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
            return@on true
        }

        on(Items.BATTERED_BOOK_2886, IntType.ITEM, "read") { player, _ ->
            sendMessage(player, "The book has two parts: an introduction and an instruction section.")
            sendMessage(player, "You flip the book open to the introduction and start reading.")
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
            return@on true
        }
    }
}
