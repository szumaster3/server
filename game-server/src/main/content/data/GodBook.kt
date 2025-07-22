package content.data

import core.api.hasRequirement
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Quests

/**
 * Represents the different types of God Books.
 */
enum class GodBook(val bookName: String, val book: Item, val damagedBook: Item, val blessItem: Array<Item>, val pages: Array<Item>, ) {
    HOLY_BOOK("Holy Book of Saradomin", Item(Items.HOLY_BOOK_3840), Item(Items.DAMAGED_BOOK_3839), arrayOf(Item(Items.HOLY_SYMBOL_1718)), arrayOf(Item(Items.SARADOMIN_PAGE_1_3827), Item(Items.SARADOMIN_PAGE_2_3828), Item(Items.SARADOMIN_PAGE_3_3829), Item(Items.SARADOMIN_PAGE_4_3830)),),
    BOOK_OF_BALANCE("Guthix's Book of Balance", Item(Items.BOOK_OF_BALANCE_3844), Item(Items.DAMAGED_BOOK_3843), arrayOf(Item(Items.HOLY_SYMBOL_1718), Item(Items.UNHOLY_SYMBOL_1724)), arrayOf(Item(Items.GUTHIX_PAGE_1_3835), Item(Items.GUTHIX_PAGE_2_3836), Item(Items.GUTHIX_PAGE_3_3837), Item(Items.GUTHIX_PAGE_4_3838)),),
    UNHOLY_BOOK("Unholy Book of Zamorak", Item(Items.UNHOLY_BOOK_3842), Item(Items.DAMAGED_BOOK_3841), arrayOf(Item(Items.UNHOLY_SYMBOL_1724)), arrayOf(Item(Items.ZAMORAK_PAGE_1_3831), Item(Items.ZAMORAK_PAGE_2_3832), Item(Items.ZAMORAK_PAGE_3_3833), Item(Items.ZAMORAK_PAGE_4_3834)),),
    ;

    /**
     * Checks if the player has a God Book in their inventory.
     */
    fun hasGodBook(
        player: Player,
        both: Boolean,
    ): Boolean = player.inventory.containsItems(*if (both) arrayOf(book, damagedBook) else arrayOf(book))

    /**
     * Inserts a page into the given God Book.
     */
    fun insertPage(
        player: Player,
        book: Item,
        page: Item,
    ) {
        if (!hasRequirement(player, Quests.HORROR_FROM_THE_DEEP)) return
        if (hasPage(player, book, page)) {
            player.sendMessage("The book already has that page.")
            return
        }
        if (player.inventory.remove(Item(page.id, 1))) {
            setPageHash(player, book, getPageIndex(page))
            player.sendMessage("You add the page to the book...")
            if (isComplete(player, book)) {
                player.savedData.globalData.apply {
                    godPages = BooleanArray(4)
                    godBook = -1
                }
                player.inventory.replace(this.book, book.slot)
                player.savedData.globalData.godBook = this.book.id
                player.sendMessage("The book is now complete!")
                val message =
                    when (this) {
                        UNHOLY_BOOK -> "unholy symbols"
                        HOLY_BOOK -> "holy symbols"
                        else -> "unblessed holy symbols"
                    }
                player.sendMessage("You can now use it to bless $message!")
            }
        }
    }

    /**
     * Checks if the given item is a page belonging to this book.
     */
    fun isPage(asItem: Item): Boolean = pages.any { it.id == asItem.id }

    /**
     * Checks if the given book is complete.
     */
    fun isComplete(
        player: Player,
        book: Item,
    ): Boolean = (1..4).all { hasPage(player, book, it) }

    /**
     * Checks if a specific page is present in the book.
     */
    fun hasPage(
        player: Player,
        book: Item,
        page: Item,
    ): Boolean = hasPage(player, book, getPageIndex(page))

    /**
     * Marks a specific page as inserted in the book.
     */
    fun setPageHash(
        player: Player,
        book: Item,
        pageId: Int,
    ) {
        player.savedData.globalData.godPages[pageId - 1] = true
    }

    /**
     * Checks if a page with the given id is present in the book.
     */
    fun hasPage(
        player: Player,
        book: Item,
        pageId: Int,
    ): Boolean = player.savedData.globalData.godPages[pageId - 1]

    /**
     * Retrieves the charge hash of a book.
     */
    fun getHash(book: Item): Int = book.charge - 1000

    /**
     * Gets the index of the given page in the book.
     */
    fun getPageIndex(page: Item): Int = pages.indexOfFirst { it.id == page.id } + 1

    companion object {
        /**
         * Finds the God Book for the given item.
         */
        @JvmStatic
        fun forItem(
            item: Item,
            damaged: Boolean,
        ): GodBook? = values().find { if (damaged) it.damagedBook.id == item.id else it.book.id == item.id }

        /**
         * Finds the God Book for the given page.
         */
        @JvmStatic
        fun forPage(page: Item): GodBook? = values().find { godBook -> godBook.pages.any { it.id == page.id } }
    }
}
