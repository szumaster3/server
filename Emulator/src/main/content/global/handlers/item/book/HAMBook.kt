package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.openDialogue
import core.api.sendDialogue
import core.api.sendPlayerDialogue
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import org.rs.consts.Items

class HAMBook : InteractionListener {
    /*
     * The Book of 'h.a.m' is used during the Zogre Flesh Eaters quest.
     * It is found in the wardrobe in Sithik Ints' room. When used on
     * Sithik Ints, he asks if the player has fought for their life
     * against the odd monster or two. In response to the player accusing
     * him of hating monsters and ogres in particular, he challenges the
     * player to back it up with facts.
     */

    companion object {
        private const val TITLE = "Book of 'h.a.m'"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("You read this book for", 55),
                        BookLine("a while, it seems to", 56),
                        BookLine("be some sort of political", 57),
                        BookLine("manifesto about how", 58),
                        BookLine("the king doesn't do", 59),
                        BookLine("enough to safeguard", 60),
                        BookLine("the citizens of the", 61),
                        BookLine("realm from the monsters", 62),
                        BookLine("that still thrive within", 63),
                        BookLine("the borders. It sends", 64),
                        BookLine("out a rallying to all", 65),
                    ),
                    Page(
                        BookLine("people who would want", 66),
                        BookLine("to stop monsters, to", 67),
                        BookLine("join the HAM movement.", 68),
                    ),
                ),
            )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(
            player,
            BookInterface.FANCY_BOOK_3_49,
            TITLE,
            CONTENTS,
        )
        return true
    }

    override fun defineListeners() {
        on(Items.BOOK_OF_HAM_4829, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            if (player.interfaceManager.close()) {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            when (stage) {
                                0 ->
                                    sendDialogue(
                                        player,
                                        "You read this book for a while, it seems to be some sort of political manifesto about how the king doesn't do enough to safeguard the citizens of the realm from the monsters that still thrive within the borders. It sends out a rallying cry to all people who would want to stop monsters, to join the HAM movement.",
                                    ).also { stage++ }

                                1 ->
                                    sendPlayerDialogue(
                                        player,
                                        "Hmmm, Sithik must really hate monsters then, I wonder if he hates ogres in particular?",
                                    ).also { stage = END_DIALOGUE }
                            }
                        }
                    },
                )
            }
            return@on true
        }
    }
}
