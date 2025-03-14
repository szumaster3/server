package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class PieRecipesBook : InteractionListener {
    /*
     * It can be purchased from Romily Weaklax on
     * the bottom floor of the cooking guild.
     */

    companion object {
        private const val TITLE = "Pie recipe book"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Redberry Pie", 55),
                        BookLine("Pour a hand full", 56),
                        BookLine("of Redberries into", 57),
                        BookLine("an empty Pie Shell,", 58),
                        BookLine("bake until the berries", 59),
                        BookLine("are soft and serve warm.", 60),
                        BookLine("<col=08088A>Meat Pie", 62),
                        BookLine("Line a fresh Pie", 63),
                        BookLine("Shell with Cooked", 64),
                        BookLine("Meat and heat until", 65),
                    ),
                    Page(
                        BookLine("the pastry starts to", 66),
                        BookLine("bronze,serve with", 67),
                        BookLine("a selection of sauces.", 68),
                        BookLine("<col=08088A>Mud Pie", 70),
                        BookLine("Start with a Pie", 71),
                        BookLine("Shell and add a Bucket", 72),
                        BookLine("of Compost,then pour", 73),
                        BookLine("in a Bucket of Water", 74),
                        BookLine("to keep the consistency", 75),
                        BookLine("gooey. To finish,cover", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("with Clay and bake", 55),
                        BookLine("until a good shell", 56),
                        BookLine("forms. Serve at maximum", 57),
                        BookLine("speed a good over-arm", 58),
                        BookLine("throw!", 59),
                        BookLine("<col=08088A>Apple Pie", 61),
                        BookLine("Take a Pie Shell and", 62),
                        BookLine("layer in Apple,cook", 63),
                        BookLine("until the juices start", 64),
                        BookLine("to bubble and leave", 65),
                    ),
                    Page(
                        BookLine("to cool before serving.", 66),
                        BookLine("<col=08088A>Garden Pie", 68),
                        BookLine("Fill a Pie Shell", 69),
                        BookLine("with Tomato,then", 70),
                        BookLine("add Onion and top", 71),
                        BookLine("with Cabbage. Bake", 72),
                        BookLine("golden brown and", 73),
                        BookLine("serve with a steak.", 74),
                        BookLine("<col=08088A>Fish Pie", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Take one Pie Shell", 55),
                        BookLine("and fill with trout,", 56),
                        BookLine("add a Cod for flavour,", 57),
                        BookLine("and then top with", 58),
                        BookLine("Potato for texture.", 59),
                        BookLine("Cook well until the", 60),
                        BookLine("potato turns golden", 61),
                        BookLine("and serve.", 62),
                        BookLine("<col=08088A>Admiral Pie", 64),
                        BookLine("For a more upperclass", 65),
                    ),
                    Page(
                        BookLine("fish pie,fill your", 66),
                        BookLine("Pie Shell with Salmon", 67),
                        BookLine("and then add Tuna for", 68),
                        BookLine("colour. Top with Potato", 69),
                        BookLine("and cook until golden.", 70),
                        BookLine("<col=08088A>Wild Pie", 72),
                        BookLine("Line a Pie Shell", 73),
                        BookLine("with raw Bear Meat,", 74),
                        BookLine("then add Raw Chompy", 75),
                        BookLine("for substance,and", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("top with fresh Rabbit", 55),
                        BookLine("Meat. Bake until the", 56),
                        BookLine("juices start to bubble", 57),
                        BookLine("and serve.", 58),
                        BookLine("<col=08088A>Wilder Pie", 60),
                        BookLine("Take an Uncooked", 61),
                        BookLine("Wild Pie,then add", 62),
                        BookLine("Crushed Dragonstone", 63),
                        BookLine("to the top and bake", 64),
                        BookLine("for that extra crunch.", 65),
                    ),
                    Page(
                        BookLine("<col=08088A>Summer Pie", 66),
                        BookLine("Into a Pie Shell,", 67),
                        BookLine("put Strawberry,then", 68),
                        BookLine("a layer of Watermelon,", 69),
                        BookLine("and top with Apple.", 70),
                        BookLine("Cook well and leave", 71),
                        BookLine("to cool before serving.", 72),
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
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.PIE_RECIPE_BOOK_7162, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
