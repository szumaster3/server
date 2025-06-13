package content.global.plugin.iface

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Book Interface.
 *
 * Example usage:
 * ```
 * on(Items.GLASSBLOWING_BOOK_11656, IntType.ITEM, "read") { player, _ ->
 *             BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
 *             return@on true
 *         }
 * ```
 * Example model set:
 * ```
 * BookInterface.setModelOnPage(
 *             player,
 *             1, // PAGE SET
 *             BUCKET_MODEL,
 *             INTERFACE_ID,
 *             BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[3], // INDEX
 *             BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[3], // INDEX
 *             500, // ZOOM
 *             200, // PITCH
 *             0    // YAW
 *         )
 * ```
 *
 * Example colors:
 * ```
 * core.tools.[COLOR]
 *
 * const val BLACK = "<col=000000>"
 * const val RED = "<col=ff0000>"
 * const val ORANGE = "<col=ff6600>"
 * const val YELLOW = "<col=ffff00>"
 * const val GREEN = "<col=66ff33>"
 * const val BLUE = "<col=0000ff>"
 * const val PURPLE = "<col=cc00ff>"
 * const val WHITE = "<col=ffffff>"
 *
 * const val DARK_RED = "<col=8A0808>"
 * const val DARK_ORANGE = "<col=ab7000>"
 * const val DARK_YELLOW = "<col=a6a300>"
 * const val DARK_GREEN = "<col=007d0c>"
 * const val DARK_BLUE = "<col=08088A>"
 * const val DARK_PURPLE = "<col=734a75>"
 * ```
 *
 * In-game command to view models `::models`
 *
 * Example book: [Ultimate Guide to Glassblowing][content.global.plugin.item.book.guide.GlassblowingGuide]
 *
 * @see [core.game.system.command.sets.ModelViewerCommandSet]
 *
 * @author ovenbreado
 */
class BookInterface : InterfaceListener {
    companion object {
        const val CALLBACK_ATTRIBUTE = "bookInterfaceCallback"
        const val CURRENT_PAGE_ATTRIBUTE = "bookInterfaceCurrentPage"

        const val FANCY_BOOK_26 = 26
        const val FANCY_BOOK_2_27 = 27
        const val FANCY_BOOK_3_49 = 49

        val FANCY_BOOK_26_LINE_IDS = arrayOf(
            101, 65, 66, 97, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
            82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96
        )
        val FANCY_BOOK_2_27_LINE_IDS = arrayOf(
            163, 5, 6, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
            53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67
        )

        val FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS = arrayOf(
            // Left.  [Index 0-14]
            1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,
            // Right. [Index 15-29]
            31,33,35,37,39,41,43,45,47,49,51,53,55,57,59
        )

        val FANCY_BOOK_26_IMAGE_DRAW_IDS = arrayOf(
            // Left.  [Index 0-14]
            2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,
            // Right. [Index 15-29]
            32,34,36,38,40,42,44,46,48,50,52,54,56,58,60
        )

        val FANCY_BOOK_3_49_LINE_IDS = arrayOf(6, 77, 78, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76)
        val FANCY_BOOK_26_BUTTON_IDS = arrayOf(61, 63)
        val FANCY_BOOK_2_27_BUTTON_IDS = arrayOf(1, 3, 159, 100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150, 152, 154, 156, 158)
        val FANCY_BOOK_3_49_BUTTON_IDS = arrayOf(51, 53)

        val FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS = arrayOf(
            // Left.  [Index 0-10]
            7,9,11,13,15,17,19,21,23,25,27,
            // Right. [Index 11-20]
            29,31,33,35,37,39,41,43,45,47,49
        )

        val FANCY_BOOK_3_49_IMAGE_DRAW_IDS = arrayOf(
            // Left.
            8,10,12,14,16,18,20,22,24,26,28,
            // Right.
            30,32,34,36,38,40,42,44,46,48,50
        )

        val FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS = arrayOf(9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 91, 93, 95, 97)
        val FANCY_BOOK_2_27_IMAGE_DRAW_IDS = arrayOf(10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98)

        /**
         * Opens a book interface with pagination callback.
         */
        fun openBook(
            player: Player,
            bookComponent: Int,
            displayCallback: (player: Player, pageNum: Int, buttonId: Int) -> Boolean,
        ) {
            closeInterface(player)
            setAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)
            setAttribute(player, CALLBACK_ATTRIBUTE, displayCallback)

            animate(player, Animations.READ_BOOK_1350)

            when (bookComponent) {
                FANCY_BOOK_26 -> openInterface(player, FANCY_BOOK_26)
                FANCY_BOOK_2_27 -> openInterface(player, FANCY_BOOK_2_27)
                FANCY_BOOK_3_49 -> openInterface(player, FANCY_BOOK_3_49)
            }

            displayCallback.invoke(player, 0, 0)
        }

        /**
         * Renders a book's current page with title and content.
         */
        fun pageSetup(player: Player, bookComponent: Int, title: String, contents: Array<PageSet>, hasPagination: Boolean = true, ) {
            val currentPage = getAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)

            when (bookComponent) {
                FANCY_BOOK_26 -> setupFancyBook(player, FANCY_BOOK_26, FANCY_BOOK_26_LINE_IDS, FANCY_BOOK_26_BUTTON_IDS, title, currentPage, contents, hasPagination)
                FANCY_BOOK_2_27 -> setupFancyBook(player, FANCY_BOOK_2_27, FANCY_BOOK_2_27_LINE_IDS, FANCY_BOOK_2_27_BUTTON_IDS, title, currentPage, contents, hasPagination)
                FANCY_BOOK_3_49 -> setupFancyBook(player, FANCY_BOOK_3_49, FANCY_BOOK_3_49_LINE_IDS, FANCY_BOOK_3_49_BUTTON_IDS, title, currentPage, contents, hasPagination)
            }
        }

        /**
         * Populates book content based on current page and layout.
         */
        private fun setupFancyBook(player: Player, bookComponent: Int, lineIds: Array<Int>, buttonIds: Array<Int>, title: String, currentPage: Int, contents: Array<PageSet>, hasPagination: Boolean, ) {
            clearBookLines(player, bookComponent, lineIds)
            clearButtons(player, bookComponent, buttonIds)
            setTitle(player, bookComponent, lineIds, title)

            if (hasPagination) {
                setPagination(
                    player = player,
                    componentId = bookComponent,
                    bookLineIds = lineIds,
                    bookButtonIds = buttonIds,
                    currentPage = currentPage,
                    totalPages = contents.size,
                    hasRightPage = contents[currentPage].pages.size == 1,
                )
            }

            setPageContent(player, bookComponent, lineIds, buttonIds, currentPage, contents)
        }

        /**
         * Clears all text lines in the book interface.
         */
        fun clearBookLines(player: Player, componentId: Int, bookLineIds: Array<Int>, ) {
            openInterface(player, componentId)
            bookLineIds.forEach { player.packetDispatch.sendString("", componentId, it) }
        }

        /**
         * Hides all book interface buttons.
         */
        fun clearButtons(player: Player, componentId: Int, bookButtonIds: Array<Int>, ) {
            bookButtonIds.forEach { player.packetDispatch.sendInterfaceConfig(componentId, it, true) }
        }

        /**
         * Sets the book title on the first line.
         */
        fun setTitle(player: Player, componentId: Int, bookLineIds: Array<Int>, title: String, ) {
            player.packetDispatch.sendString(title, componentId, bookLineIds[0])
        }

        /**
         * Configures pagination buttons and page numbers.
         */
        fun setPagination(player: Player, componentId: Int, bookLineIds: Array<Int>, bookButtonIds: Array<Int>, currentPage: Int, totalPages: Int, hasRightPage: Boolean, ) {
            player.packetDispatch.sendInterfaceConfig(componentId, bookButtonIds[0], currentPage <= 0)
            player.packetDispatch.sendInterfaceConfig(componentId, bookButtonIds[1], currentPage >= totalPages - 1)
            player.packetDispatch.sendString("" + (currentPage * 2 + 1), componentId, bookLineIds[1])
            player.packetDispatch.sendString("" + (currentPage * 2 + 2), componentId, bookLineIds[2])

            if (hasRightPage) {
                val lineId =
                    when (componentId) {
                        FANCY_BOOK_26 -> FANCY_BOOK_26_LINE_IDS[2]
                        FANCY_BOOK_2_27 -> FANCY_BOOK_2_27_LINE_IDS[2]
                        FANCY_BOOK_3_49 -> FANCY_BOOK_3_49_LINE_IDS[2]
                        else -> null
                    }
                lineId?.let { player.packetDispatch.sendString("", componentId, it) }
            }
        }

        /**
         * Fills book lines/buttons with page content.
         */
        fun setPageContent(player: Player, componentId: Int, bookLineIds: Array<Int>, bookButtonIds: Array<Int>, currentPage: Int, contents: Array<PageSet>, ) {
            for (page in contents[currentPage].pages) {
                for (line in page.lines) {
                    if (bookLineIds.contains(line.child)) {
                        player.packetDispatch.sendString(line.message, componentId, line.child)
                    }
                    if (bookButtonIds.contains(line.child)) {
                        player.packetDispatch.sendInterfaceConfig(componentId, line.child, false)
                        player.packetDispatch.sendString(line.message, componentId, line.child)
                    }
                }
            }
        }

        /**
         * Displays a model on a book page if visible.
         */
        fun setModelOnPage(player: Player, pageSet: Int, modelId: Int, componentId: Int, enableLineId: Int, drawLineId: Int, zoom: Int, pitch: Int, yaw: Int, ) {
            if (pageSet == getAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)) {
                player.packetDispatch.sendInterfaceConfig(componentId, enableLineId, false)
                player.packetDispatch.sendModelOnInterface(modelId, componentId, drawLineId, 0)
                player.packetDispatch.sendAngleOnInterface(componentId, drawLineId, zoom, pitch, yaw)
            } else {
                player.packetDispatch.sendInterfaceConfig(componentId, enableLineId, true)
            }
        }

        /**
         * Checks if given page is the last one.
         */
        fun isLastPage(pageNum: Int, totalPages: Int, ): Boolean = pageNum == totalPages - 1

        /**
         * Changes page and triggers content refresh callback.
         */
        private fun changePageAndCallback(player: Player, increment: Int, buttonId: Int, ) {
            val callback: ((player: Player, pageNum: Int, buttonId: Int) -> Boolean)? =
                getAttribute(player, CALLBACK_ATTRIBUTE, null)
            val currentPage = getAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)
            playAudio(player, Sounds.TURN_BOOK_PAGE_2417)

            if (increment > 0) animate(player, 3140) // 3142 (different book cover)
            else if (increment < 0) animate(player, 3141) // 3143 (different book cover)

            setAttribute(player, CURRENT_PAGE_ATTRIBUTE, currentPage + increment)
            callback?.invoke(player, currentPage + increment, buttonId)
        }
    }

    override fun defineInterfaceListeners() {

        on(FANCY_BOOK_26) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                FANCY_BOOK_26_BUTTON_IDS[0] -> changePageAndCallback(player, -1, buttonID)
                FANCY_BOOK_26_BUTTON_IDS[1] -> changePageAndCallback(player, 1, buttonID)
                else -> (changePageAndCallback(player, 0, buttonID))
            }
            return@on true
        }

        on(FANCY_BOOK_2_27) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                FANCY_BOOK_2_27_BUTTON_IDS[0] -> changePageAndCallback(player, -1, buttonID)
                FANCY_BOOK_2_27_BUTTON_IDS[1] -> changePageAndCallback(player, 1, buttonID)
                else -> (changePageAndCallback(player, 0, buttonID))
            }
            return@on true
        }

        on(FANCY_BOOK_3_49) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                FANCY_BOOK_3_49_BUTTON_IDS[0] -> changePageAndCallback(player, -1, buttonID)
                FANCY_BOOK_3_49_BUTTON_IDS[1] -> changePageAndCallback(player, 1, buttonID)
                else -> (changePageAndCallback(player, 0, buttonID))
            }
            return@on true
        }
    }
}

class PageSet(vararg pages: Page) {
    val pages: Array<Page> = pages as Array<Page>
}

class Page(vararg lines: BookLine) {
    val lines: Array<BookLine> = lines as Array<BookLine>
}

class BookLine(val message: String, val child: Int, )
