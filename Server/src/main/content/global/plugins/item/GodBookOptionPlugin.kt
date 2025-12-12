package content.global.plugins.item

import content.data.GodBook
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests

/**
 * Listener handling for [GodBook].
 *
 * - Checking pages.
 * - Preaching.
 * - Using a page on a damaged book.
 * - Blessing.
 * - Equipping.
 */
class GodBookOptionPlugin : InteractionListener {

    private val books = mapOf(
        Items.HOLY_BOOK_3840        to BookType.SARADOMIN,
        Items.UNHOLY_BOOK_3842      to BookType.ZAMORAK,
        Items.BOOK_OF_BALANCE_3844  to BookType.GUTHIX
    )

    override fun defineListeners() {

        /*
         * Handles checking pages in damaged books.
         */

        GodBook.values().forEach { book ->
            on(book.damagedBookId, IntType.ITEM, "check") { player, node ->
                val nodeItemId = node.id
                val messages = Array(4) { i ->
                    if (book.hasPage(player, nodeItemId, i + 1)) {
                        "The ${getNumberName(i + 1)} page is in the book."
                    } else {
                        "The ${getNumberName(i + 1)} page is missing."
                    }
                }
                sendDialogueLines(player, *messages)
                return@on true
            }
        }

        /*
         * Handles preaching.
         */

        books.forEach { (itemId, type) ->
            on(itemId, IntType.ITEM, "preach") { player, _ ->
                openDialogue(player, HolyDialogue(type))
                return@on true
            }
        }

        /*
         * Handles equipping the book.
         */

        onEquip(books.keys.toIntArray()) { player, node ->
            val type = books[node.id] ?: return@onEquip true
            if (!isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                sendMessage(player, "You need to complete The Horror from the Deep quest to equip this.")
                return@onEquip false
            }
            if (getStatLevel(player, Skills.PRAYER) < 30) {
                sendMessage(player, "You need a Prayer level of at least 30 to wield the ${type.name.lowercase()}.")
                return@onEquip false
            }
            return@onEquip true
        }

        /*
         * Handles blessing the Saradomin book.
         */

        onUseWith(IntType.ITEM, Items.HOLY_BOOK_3840, Items.UNBLESSED_SYMBOL_1716) { player, _, item ->
            bless(player, item, Items.HOLY_SYMBOL_1718)
            return@onUseWith true
        }

        /*
         * Handles blessing the Zamorak book.
         */

        onUseWith(IntType.ITEM, Items.UNHOLY_BOOK_3842, Items.UNPOWERED_SYMBOL_1722) { player, _, item ->
            bless(player, item, Items.UNHOLY_SYMBOL_1724)
            return@onUseWith true
        }

        /*
         * Handles blessing the Book of Balance (holy).
         */

        onUseWith(IntType.ITEM, Items.BOOK_OF_BALANCE_3844, Items.UNBLESSED_SYMBOL_1716) { player, _, item ->
            bless(player, item, Items.HOLY_SYMBOL_1718)
            return@onUseWith true
        }

        /*
         * Handles blessing the Book of Balance (unholy).
         */

        onUseWith(IntType.ITEM, Items.BOOK_OF_BALANCE_3844, Items.UNPOWERED_SYMBOL_1722) { player, _, item ->
            bless(player, item, Items.UNHOLY_SYMBOL_1724)
            return@onUseWith true
        }

        /*
         * Handles inserting pages into damaged books.
         */

        GodBook.values().forEach { book ->
            book.pageIds.forEach { pageId ->
                onUseWith(IntType.ITEM, book.damagedBookId, pageId) { player, damagedBookNode, pageNode ->
                    val damagedBookId = damagedBookNode.id
                    val usedPageId = pageNode.id

                    val godBook = GodBook.forItem(damagedBookId, true)
                    if (godBook != null && godBook.isPage(usedPageId)) {
                        godBook.insertPage(player, damagedBookId, usedPageId)
                    } else {
                        sendMessage(player, "This page cannot be used with this book.")
                    }

                    return@onUseWith true
                }
            }
        }
    }

    /**
     * Handles blessing books.
     */
    fun bless(player: Player, item: Node, resultId: Int) {
        when {
            getStatLevel(player, Skills.PRAYER) < 50 ->
                sendMessage(player, "You need a Prayer level of at least 50 to do this.")
            player.skills.prayerPoints < 4 ->
                sendMessage(player, "You need at least 4 Prayer points to do this.")
            else -> {
                sendMessage(player, "You bless the ${item.asItem().name.lowercase()}.")
                player.skills.decrementPrayerPoints(40.0)
                replaceSlot(player, item.asItem().index, Item(resultId), item.asItem())
            }
        }
    }

    /**
     * Converts page number to its textual representation.
     * @param i page number (1-4)
     * @return "first", "second", "third", or "fourth"
     */
    private fun getNumberName(i: Int): String = when (i) {
        1 -> "first"
        2 -> "second"
        3 -> "third"
        else -> "fourth"
    }

    /**
     * Book types and dialogues.
     */
    enum class BookType(val anim: Int, val text: String) {
        SARADOMIN(Animations.PREACH_WHITE_1335, "This is Saradomin's wisdom."),
        GUTHIX(Animations.PREACH_GREEN_1337, "May Guthix bring you balance."),
        ZAMORAK(Animations.PREACH_RED_1336, "Zamorak give me strength!")
    }

    /**
     * Dialogue class handling preaching for each [GodBook].
     */
    class HolyDialogue(private val book: BookType) : DialogueFile() {

        private val preachings = mapOf(
            BookType.SARADOMIN to listOf(
                listOf("Protect your self, protect your friends. Mine is the glory that never ends."),
                listOf("The darkness in life may be avoided, by the light of wisdom shining."),
                listOf("Show love to your friends, and mercy to your enemies, and know that the", "wisdom of Saradomin will follow."),
                listOf("A fight begun, when the cause is just, will prevail over all others."),
                listOf("Walk proud, and show mercy,", "For you carry my name in your heart.")
            ),
            BookType.GUTHIX to listOf(
                listOf("All things must end, as all begin; Only Guthix knows the role thou must play."),
                listOf("In life, in death, in joy, in sorrow: May thine experience show thee balance."),
                listOf("Thou must do as thou must, no matter what. Thine actions bring balance to this world."),
                listOf("The river flows, the sun ignites, May you stand with Guthix in thy fights."),
                listOf("May take thee over a thousand miles.", "May Guthix bring you balance.")
            ),
            BookType.ZAMORAK to listOf(
                listOf("There is no opinion that cannot be proven true...by crushing those who choose to disagree with it."),
                listOf("Battles are not lost and won; They simply remove the weak from the equation."),
                listOf("Those who fight, then run away, shame Zamorak with their cowardice."),
                listOf("Strike fast, strike hard, strike true: The strength of Zamorak will be with you."),
                listOf("There is no opinion that cannot be proven true,", "by crushing those who choose to disagree with it.")
            )
        )

        /**
         * Handles the dialogue component interactions.
         */
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> options("Weddings", "Last Rites", "Blessings", "Preaching").also { stage++ }
                1 -> {
                    val msgList = when (buttonID) {
                        1 -> preachings[book]
                        2 -> preachings[book]
                        3 -> preachings[book]
                        4 -> preachings[book]?.random()?.let { listOf(it) }
                        else -> null
                    }?.flatten() ?: return end()
                    preach(player!!, msgList, book)
                    end()
                }
            }
        }

        /**
         * Preaches lines with animation and timing for the player.
         */
        private fun preach(player: Player, lines: List<String>, book: BookType) {
            val anim = Animation(book.anim)
            lock(player, 100)

            var index = 0
            var tick = 0

            submitIndividualPulse(player, object : Pulse() {
                override fun pulse(): Boolean {
                    tick++

                    if (tick % 2 == 0) {
                        animate(player, anim)
                    }

                    if (tick % 3 == 0) {
                        if (index < lines.size) {
                            sendChat(player, lines[index++])
                        } else {
                            sendChat(player, book.text)
                            unlock(player)
                            return true
                        }
                    }

                    return false
                }
            })
        }
    }
}