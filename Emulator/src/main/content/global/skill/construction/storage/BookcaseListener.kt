package content.global.skill.construction.storage

import content.global.skill.construction.storage.box.BookcaseItem
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class BookcaseListener : InteractionListener {
    private val BOOKCASE =
        intArrayOf(
            Scenery.BOOKCASE_13597,
            Scenery.BOOKCASE_13598,
            Scenery.BOOKCASE_13599
        )

    private val BOOKCASE_RESTRICTED_CONTENT =
        intArrayOf(
            Items.HOLY_BOOK_3840,
            Items.DAMAGED_BOOK_3839,
            Items.BOOK_OF_BALANCE_3844,
            Items.DAMAGED_BOOK_3843,
            Items.UNHOLY_BOOK_3842,
            Items.DAMAGED_BOOK_3841,
            Items.STRANGE_BOOK_5507,
            Items.BOOK_ON_CHICKENS_7464,
            Items.BOOK_OF_FOLKLORE_5508,
            Items.PVP_WORLDS_MANUAL_14056
        )

    private val INTERFACE = 467
    override fun defineListeners() {
        /*
         * Handles interaction with bookcase.
         */

        on(BOOKCASE, IntType.SCENERY, "search") { player, _ ->
            animate(player, Animations.USE_OBJECT_POH_3659)
            setAttribute(player, "con:bookcase", true)
            openInterface(player, INTERFACE).also {
                sendString(player, "Bookcase", INTERFACE, 225)
                val books = BookcaseItem.values()
                PacketRepository.send(
                    ContainerPacket::class.java,
                    ContainerContext(player, INTERFACE, 164, 30, books.map { Item(it.takeId) }.toTypedArray(), false)
                )
                books.forEach { book ->
                    val itemName = getItemName(book.takeId)
                    val itemExamine = ItemDefinition.forId(book.takeId).examine
                    sendString(
                        player,
                        "${core.tools.YELLOW}$itemName</col> <br>$itemExamine",
                        INTERFACE,
                        55 + book.labelId * 2
                    )
                }
            }
            return@on true
        }


        /*
         * Handles use the restricted book on bookcase.
         */

        onUseWith(IntType.SCENERY, BOOKCASE_RESTRICTED_CONTENT, *BOOKCASE) { player, _, _ ->
            sendMessage(player, "There doesn't seem to be space for that on the bookcase.")
            return@onUseWith true
        }
    }
}