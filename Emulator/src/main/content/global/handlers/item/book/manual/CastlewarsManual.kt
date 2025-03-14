package content.global.handlers.item.book.manual

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class CastlewarsManual : InteractionListener {
    companion object {
        private const val TITLE = "Castlewars Manual"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Objective", 55),
                        BookLine("The aim is to get into", 56),
                        BookLine("your opponents castle and", 57),
                        BookLine("take their team standard.", 58),
                        BookLine("Then bring that back and", 59),
                        BookLine("capture it on your teams", 60),
                        BookLine("standard.", 61),
                    ),
                    Page(
                        BookLine("<col=08088A>Toolkit", 66),
                        BookLine("This useful item allows", 67),
                        BookLine("you to repair broken doors", 68),
                        BookLine("and catapults. Simply use", 69),
                        BookLine("it on the item to be repaired,", 70),
                        BookLine("or have one in your inventory", 71),
                        BookLine("when you select the option,", 72),
                        BookLine("and you'll rebuild it!", 73),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Bandages", 55),
                        BookLine("These can be used to heal", 56),
                        BookLine("some health and restore", 57),
                        BookLine("some of your running energy.", 58),
                        BookLine("You can also use them to", 59),
                        BookLine("heal fellow players.", 60),
                    ),
                    Page(
                        BookLine("<col=08088A>Explosive Potion", 66),
                        BookLine("A simple but effective item,", 67),
                        BookLine("use it to blow up your", 68),
                        BookLine("opponents catapult and", 69),
                        BookLine("barricades! It can also", 70),
                        BookLine("be used to clear the", 71),
                        BookLine("tunnels under the arena", 72),
                        BookLine("for a sneak attack into", 73),
                        BookLine("your opponents castle. Don't", 74),
                        BookLine("forget to collapse the", 75),
                        BookLine("tunnels into your own castle!", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Barricade", 55),
                        BookLine("Use these constructs", 56),
                        BookLine("to block your opponents", 57),
                        BookLine("movement and prevent", 58),
                        BookLine("them accessing your castle.", 59),
                        BookLine("Each team can only have", 60),
                        BookLine("10 built at any time.", 61),
                    ),
                    Page(
                        BookLine("<col=08088A>Bucket", 66),
                        BookLine("Fill a bucket with water", 67),
                        BookLine("and you can put out a", 68),
                        BookLine("burning catapult or barricade,", 69),
                        BookLine("but be quick or it'll", 70),
                        BookLine("be destroyed.", 71),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Tinderbox", 55),
                        BookLine("Logs aren't all that's", 56),
                        BookLine("flammable, use a tinderbox", 57),
                        BookLine("to set light to your", 58),
                        BookLine("opponents catapult and", 59),
                        BookLine("barricades.", 60),
                    ),
                    Page(
                        BookLine("<col=08088A>Pickaxe", 66),
                        BookLine("Use a pickaxe to mine", 67),
                        BookLine("your way through the", 68),
                        BookLine("tunnels under the arena", 69),
                        BookLine("for a sneak attack into", 70),
                        BookLine("your opponents castle.", 71),
                        BookLine("Don't forget to collapse", 72),
                        BookLine("the tunnels into your", 73),
                        BookLine("own castle though!", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Catapult", 55),
                        BookLine("Use this war machine", 56),
                        BookLine("to launch rocks at your", 57),
                        BookLine("opponents. Just give", 58),
                        BookLine("it rough coordinates", 59),
                        BookLine("and let the rock fly,", 60),
                        BookLine("just be careful not to", 61),
                        BookLine("hit your team with it!", 62),
                    ),
                    Page(
                        BookLine("<col=08088A>Rock", 66),
                        BookLine("Used as ammo for the", 67),
                        BookLine("catapult, and not much", 68),
                        BookLine("else. Brings new meaning", 69),
                        BookLine("to the phrase 'flies", 70),
                        BookLine("like a rock'.", 71),
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
        on(Items.CASTLEWARS_MANUAL_4055, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
