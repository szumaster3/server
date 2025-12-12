package content.data

import core.api.asItem
import core.api.isQuestComplete
import core.api.removeItem
import core.api.sendMessage
import core.game.node.entity.player.Player
import shared.consts.Items
import shared.consts.Quests

/**
 * Represents the god books.
 */
enum class GodBook(val bookId: Int, val damagedBookId: Int, val blessItemIds: IntArray, val pageIds: IntArray) {
    HOLY_BOOK(Items.HOLY_BOOK_3840, Items.DAMAGED_BOOK_3839, intArrayOf(Items.HOLY_SYMBOL_1718), intArrayOf(Items.SARADOMIN_PAGE_1_3827, Items.SARADOMIN_PAGE_2_3828, Items.SARADOMIN_PAGE_3_3829, Items.SARADOMIN_PAGE_4_3830)),
    BOOK_OF_BALANCE(Items.BOOK_OF_BALANCE_3844, Items.DAMAGED_BOOK_3843, intArrayOf(Items.HOLY_SYMBOL_1718, Items.UNHOLY_SYMBOL_1724), intArrayOf(Items.GUTHIX_PAGE_1_3835, Items.GUTHIX_PAGE_2_3836, Items.GUTHIX_PAGE_3_3837, Items.GUTHIX_PAGE_4_3838)),
    UNHOLY_BOOK(Items.UNHOLY_BOOK_3842, Items.DAMAGED_BOOK_3841, intArrayOf(Items.UNHOLY_SYMBOL_1724), intArrayOf(Items.ZAMORAK_PAGE_1_3831, Items.ZAMORAK_PAGE_2_3832, Items.ZAMORAK_PAGE_3_3833, Items.ZAMORAK_PAGE_4_3834));

    /**
     * Inserts a page into this [GodBook].
     *
     * @param player The player who is inserting the page.
     * @param bookId The id of the damaged book to insert the page into.
     * @param pageId The id of the page being inserted.
     */
    fun insertPage(player: Player, bookId: Int, pageId: Int) {
        if (!isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
            sendMessage(player, "You need to complete The Horror from the Deep quest to do this.")
            return
        }

        val pageIndex = getPageIndex(pageId)
        if (pageIndex !in 1..pageIds.size) {
            sendMessage(player, "This page cannot be used with this book.")
            return
        }

        if (hasPage(player, bookId, pageIndex)) {
            sendMessage(player, "The book already has that page.")
            return
        }

        if (removeItem(player, pageId)) {
            setPageHash(player, bookId, pageIndex)
            sendMessage(player, "You add the page to the book...")

            if (isComplete(player, bookId)) {
                player.savedData.globalData.apply {
                    godPages = BooleanArray(pageIds.size)
                    godBook = -1
                }
                player.inventory.replace(this@GodBook.bookId.asItem(), bookId)
                player.savedData.globalData.godBook = this@GodBook.bookId
                sendMessage(player, "The book is now complete!")

                val message = when (this) {
                    UNHOLY_BOOK -> "unholy symbols"
                    HOLY_BOOK -> "holy symbols"
                    else -> "unblessed holy symbols"
                }
                sendMessage(player, "You can now use it to bless $message!")
            }
        }
    }

    /**
     * Checks if a given page id belongs to this book.
     * @param pageId The id of the page to check.
     * @return True if the page belongs to this book, false otherwise.
     */
    fun isPage(pageId: Int): Boolean = pageIds.contains(pageId)

    /**
     * Checks if the book is complete (all pages inserted) for the player.
     * @param player The player to check.
     * @param bookId The id of the book.
     * @return True if all pages are inserted, false otherwise.
     */
    fun isComplete(player: Player, bookId: Int): Boolean = (1..4).all { hasPage(player, bookId, it) }

    /**
     * Marks a specific page as inserted in the book for the player.
     * @param player The player updating the book.
     * @param bookId The id of the book.
     * @param pageIndex The index of the page to mark as inserted (1-4).
     */
    private fun setPageHash(player: Player, bookId: Int, pageIndex: Int) {
        player.savedData.globalData.godPages[pageIndex - 1] = true
    }

    /**
     * Checks if a page is already in the player book.
     * @param player The player to check.
     * @param bookId The id of the book.
     * @param pageIndex The page index to check (1-4).
     * @return True if the page is present, false otherwise.
     */
    fun hasPage(player: Player, bookId: Int, pageIndex: Int): Boolean =
        player.savedData.globalData.godPages[pageIndex - 1]

    /**
     * Gets the 1-based index of a page in this book.
     * @param pageId The id of the page.
     * @return The index of the page (1-4), or 0 if not found.
     */
    private fun getPageIndex(pageId: Int): Int = pageIds.indexOf(pageId) + 1

    companion object {
        /**
         * Finds the [GodBook] for the item id.
         * @param itemId The item id to check.
         * @param damaged True to search for a damaged book, false for the completed book.
         * @return The matching [GodBook] or null if none found.
         */
        fun forItem(itemId: Int, damaged: Boolean): GodBook? =
            values().find { if (damaged) it.damagedBookId == itemId else it.bookId == itemId }
    }
}