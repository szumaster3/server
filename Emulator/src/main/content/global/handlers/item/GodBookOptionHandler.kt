package content.global.handlers.item

import content.data.GodBook
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.ItemPlugin
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class GodBookOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (book in GodBook.values()) {
            book.damagedBook.definition.handlers["option:check"] = this
        }
        ClassScanner.definePlugins(PageHandler(), GodBookItem())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val book = GodBook.forItem(node.asItem(), option.equals("check", ignoreCase = true))
        if (book != null) {
            when (option) {
                "check" -> {
                    val messages = arrayOfNulls<String>(4)
                    for (i in messages.indices) {
                        messages[i] =
                            if (book.hasPage(
                                    player,
                                    node.asItem(),
                                    i + 1,
                                )
                            ) {
                                "The " + getNumberName(i + 1) + " page is in the book."
                            } else {
                                "The " + getNumberName(i + 1) +
                                    " page is missing."
                            }
                    }
                    player.dialogueInterpreter.sendDialogue(*messages)
                    return true
                }

                "preach" -> {
                    player.dialogueInterpreter.open("god-book", book)
                    return true
                }
            }
        }
        return true
    }

    inner class GodBookItem : ItemPlugin() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            for (book in GodBook.values()) {
                register(book.damagedBook.id)
            }
            return this
        }

        override fun canPickUp(
            player: Player,
            item: GroundItem,
            type: Int,
        ): Boolean {
            if (player.hasItem(item.asItem())) {
                player.sendMessage("You do not need more than one incomplete book.")
                return false
            }
            return true
        }
    }

    inner class PageHandler : UseWithHandler(3839, 3841, 3843) {
        override fun newInstance(arg: Any?): Plugin<Any?> {
            for (book in GodBook.values()) {
                for (i in book.pages) {
                    addHandler(i.id, ITEM_TYPE, this)
                }
            }
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val book = GodBook.forItem(event.usedItem, true)
            val player = event.player
            if (book != null && book.isPage(event.usedWith.asItem())) {
                book.insertPage(player, event.usedItem, event.usedWith.asItem())
                return true
            }
            return false
        }
    }

    private fun getNumberName(i: Int): String {
        return when (i) {
            1 -> "first"
            2 -> "second"
            3 -> "third"
            else -> "fourth"
        }
    }
}
