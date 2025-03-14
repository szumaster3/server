package content.global.handlers.item.book.guide

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class BlurberryCocktailGuide : InteractionListener {
    /*
     * Contains a collection of recipes for the various gnome cocktails.
     * It is useful for the Gnome Restaurant minigame.
     */

    companion object {
        private const val TITLE = "The Blurberry Cocktail Guide"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Cocktail Recipes", 55),
                        BookLine("<col=FF0000>Blurberry Special", 57),
                        BookLine("<col=FF0000>Chocolate Saturday", 58),
                        BookLine("<col=FF0000>Drunk Dragon", 59),
                        BookLine("<col=FF0000>Fruit Blast", 60),
                        BookLine("<col=FF0000>Pineapple Punch", 61),
                        BookLine("<col=FF0000>Short Green Guy", 62),
                        BookLine("<col=FF0000>Wizard Blizzard", 63),
                        BookLine("<col=FF0000>Blurberry Special", 64),
                    ),
                    Page(
                        BookLine("Ingredients:", 66),
                        BookLine("<col=FF0000>1 shot vodka", 68),
                        BookLine("<col=FF0000>1 shot gin", 69),
                        BookLine("<col=FF0000>1 shot brandy", 70),
                        BookLine("<col=FF0000>Juice 2 lemons", 71),
                        BookLine("<col=FF0000>Juice 1 orange", 72),
                        BookLine("Mix the ingredients together", 74),
                        BookLine("in the cocktail shaker.", 75),
                        BookLine("Shake well and pour into", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("a glass. Add diced orange", 55),
                        BookLine("followed by chunks of", 56),
                        BookLine("lemon. Top the glass with", 57),
                        BookLine("a slice of lime and finish", 58),
                        BookLine("with some equa leaves.", 59),
                        BookLine("<col=08088A>Chocolate Saturday", 61),
                        BookLine("Ingredients:", 63),
                        BookLine("<col=FF0000>1 shot whisky", 65),
                    ),
                    Page(
                        BookLine("<col=FF0000>1 sprig of equa leaves", 66),
                        BookLine("<col=FF0000>Dash of milk", 67),
                        BookLine("<col=FF0000>1 bar of chocolate", 68),
                        BookLine("Mix the ingredients together", 70),
                        BookLine("in the cocktail shaker", 71),
                        BookLine("along with a crumbled", 72),
                        BookLine("chocolate bar. Shake well", 73),
                        BookLine("and pour into a glass.", 74),
                        BookLine("Warm it in an oven, then", 75),
                        BookLine("add fresh cream. Finally", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("sprinkle with chocolate", 55),
                        BookLine("dust.", 56),
                        BookLine("<col=08088A>Drunk Dragon", 58),
                        BookLine("Ingredients:", 60),
                        BookLine("<col=FF0000>1 shot vodka", 61),
                        BookLine("<col=FF0000>1 shot gin", 62),
                        BookLine("<col=FF0000>Dwellberry juice", 63),
                    ),
                    Page(
                        BookLine("Mix the ingredients together", 66),
                        BookLine("in the cocktail shaker.", 67),
                        BookLine("Shake well and pour into", 68),
                        BookLine("a glass. Add diced pineapple", 69),
                        BookLine("then top with fresh cream.", 70),
                        BookLine("Heat the drink briefly", 71),
                        BookLine("in a warm oven before", 72),
                        BookLine("serving.", 73),
                        BookLine("<col=08088A>Fruit Blast", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Ingredients:", 56),
                        BookLine("<col=FF0000>Juice 1 pineapple", 58),
                        BookLine("<col=FF0000>Juice 1 orange", 59),
                        BookLine("<col=FF0000>Juice 1 lemon", 60),
                        BookLine("Mix the ingredients together", 62),
                        BookLine("in the cocktail shaker.", 63),
                        BookLine("Shake well and pour into", 64),
                        BookLine("a glass. Simply top with", 65),
                    ),
                    Page(
                        BookLine("slices of fresh lemon", 66),
                        BookLine("to finish this light and", 67),
                        BookLine("refreshing mix.", 68),
                        BookLine("<col=08088A>Pineapple Punch", 71),
                        BookLine("Ingredients:", 73),
                        BookLine("<col=FF0000>Juice 1 lemon", 75),
                        BookLine("<col=FF0000>Juice 1 orange", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=FF0000>Juice 2 pineapples", 55),
                        BookLine("Mix the ingredients together", 57),
                        BookLine("in the cocktail shaker.", 58),
                        BookLine("Shake well and pour into", 59),
                        BookLine("a glass. Add chunks of", 60),
                        BookLine("pineapple followed by", 61),
                        BookLine("diced lime. Finish the", 62),
                        BookLine("drink by placing a single", 63),
                        BookLine("slice of orange on top.", 64),
                    ),
                    Page(
                        BookLine("<col=08088A>Short Green Guy (SGG)", 66),
                        BookLine("Ingredients:", 68),
                        BookLine("<col=FF0000>1 shot vodka", 70),
                        BookLine("<col=FF0000>Juice 3 limes", 71),
                        BookLine("Mix the ingredients together", 73),
                        BookLine("in the cocktail shaker.", 74),
                        BookLine("Shake well and pour into", 75),
                        BookLine("a glass. Add a sprinkling", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("of equa leaves followed", 55),
                        BookLine("by a single lime slice", 56),
                        BookLine("to finish this gnome classic.", 57),
                        BookLine("<col=08088A>Wizard Blizzard", 59),
                        BookLine("Ingredients:", 61),
                        BookLine("<col=FF0000>2 shots vodka", 63),
                        BookLine("<col=FF0000>1 shot gin", 64),
                        BookLine("<col=FF0000>Juice 1 orange", 65),
                    ),
                    Page(
                        BookLine("<col=FF0000>Juice 1 lime", 66),
                        BookLine("<col=FF0000>Juice 1 lemon", 67),
                        BookLine("Mix the ingredients together", 69),
                        BookLine("in the cocktail shaker.", 70),
                        BookLine("Shake well and pour into", 71),
                        BookLine("a glass. Add pineapple", 72),
                        BookLine("chunks to the drink.", 73),
                        BookLine("Finally finish this magical", 74),
                        BookLine("concoction with", 75),
                        BookLine("slices of lime.", 76),
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
        on(Items.COCKTAIL_GUIDE_2023, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
