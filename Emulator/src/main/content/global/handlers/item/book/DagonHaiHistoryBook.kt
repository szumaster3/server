package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class DagonHaiHistoryBook : InteractionListener {
    /*
     * Book found in the Varrock library. Telling story of "What lies below" quest.
     */

    companion object {
        private const val TITLE = "The Fall of the Dagon'hai"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("'...In scarred lands and", 55),
                        BookLine("ruined fields, that circle", 56),
                        BookLine("flaming towers high, where", 57),
                        BookLine("cruel and cursed torments", 58),
                        BookLine("yield, the Dark of Man,", 59),
                        BookLine("the Dagon'hai.'", 60),
                        BookLine("The Prophecies of Zamorak", 61),
                        BookLine("...until the day that the", 62),
                        BookLine("Zamorakian Magi were welcomed", 63),
                        BookLine("around the human numbers.", 64),
                        BookLine("The Zamorakian numbers", 65),
                    ),
                    Page(
                        BookLine("grew and some moved their", 66),
                        BookLine("order to the city of Varrock.", 67),
                        BookLine("These were the Dagon'hai.", 68),
                        BookLine("The practices of the Dagon'hai", 69),
                        BookLine("were looked darkly upon", 70),
                        BookLine("by the priests of Saradomin", 71),
                        BookLine("within the walls of Varrock", 72),
                        BookLine("and, thus, the Dagon'hai", 73),
                        BookLine("were forced into a hidden", 74),
                        BookLine("war, away from the eyes", 75),
                        BookLine("of the citizens of Varrock.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Priests of Saradomin were", 55),
                        BookLine("found dead in darkened", 56),
                        BookLine("streets and crumbling houses.", 57),
                        BookLine("Dagon'hai were cast into", 58),
                        BookLine("cells and tormented through", 59),
                        BookLine("purification. Varrock was", 60),
                        BookLine("torn between the followers", 61),
                        BookLine("of two warring deities.", 62),
                        BookLine("Yet, the people of Varrock", 63),
                        BookLine("did nothing... until the", 64),
                        BookLine("tower fell. The famed Wizard's", 65),
                    ),
                    Page(
                        BookLine("Tower in the south burned", 66),
                        BookLine("for what seemed like and", 67),
                        BookLine("age and the people of Varrock", 68),
                        BookLine("knew they had been misled.", 69),
                        BookLine("They turned upon the Zamorakian", 70),
                        BookLine("mages with murderous intent", 71),
                        BookLine("and the Dagon'hai, even", 72),
                        BookLine("with their skills in dark", 73),
                        BookLine("wizardry, were unable to", 74),
                        BookLine("confront such a vengeance", 75),
                        BookLine("-filled assault against", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("their order,", 55),
                        BookLine("supported in full by the", 56),
                        BookLine("priests of Saradomin.", 57),
                        BookLine("The Dagon'hai were all", 58),
                        BookLine("but decimated. Their numbers", 59),
                        BookLine("were shattered and they", 60),
                        BookLine("hid themselves in the darkest", 61),
                        BookLine("houses in the most dimly", 62),
                        BookLine("lit of streets within the", 63),
                        BookLine("city. Those that survived", 64),
                        BookLine("the exodus delved deeper", 65),
                    ),
                    Page(
                        BookLine("into the most evil of arcane", 66),
                        BookLine("magic, intent on extracting", 67),
                        BookLine("retribution upon the priests", 68),
                        BookLine("of Saradomin and the people", 69),
                        BookLine("of Varrock. Varrock may", 70),
                        BookLine("have fallen if not for", 71),
                        BookLine("the purest of chances.", 72),
                        BookLine("A young guardsman on patrol", 73),
                        BookLine("in the city happened across", 74),
                        BookLine("a dimly lit house in a", 75),
                        BookLine("corner of the city. Sensing", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("something evil in the air,", 55),
                        BookLine("he cautiously peered through", 56),
                        BookLine("one of the small windows", 57),
                        BookLine("and was aghast to see the", 58),
                        BookLine("High Elders of the Dagon'hai", 59),
                        BookLine("engaged in a sacrificial", 60),
                        BookLine("ritual. Sounding the alarm,", 61),
                        BookLine("the guard called for help", 62),
                        BookLine("and the Dagon'hai, knowing", 63),
                        BookLine("they would be slaughtered,", 64),
                        BookLine("fled into the night.", 65),
                    ),
                    Page(
                        BookLine("The last remaining Dagon'hai", 66),
                        BookLine("were chased throughout", 67),
                        BookLine("Varrock to the great statue", 68),
                        BookLine("of Saradomin himself. Then,", 69),
                        BookLine("they all but disappeared.", 70),
                        BookLine("Nobody knows what happened", 71),
                        BookLine("to them, but some say they", 72),
                        BookLine("were set upon by creatures", 73),
                        BookLine("from the Wilderness. Others", 74),
                        BookLine("say that they teleported", 75),
                        BookLine("themselves away to a place", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("of safety. A few claimed", 55),
                        BookLine("they saw men disappearing", 56),
                        BookLine("into the earth beneath", 57),
                        BookLine("the statue itself. It was", 58),
                        BookLine("as though the statue of", 59),
                        BookLine("Saradomin had claimed their", 60),
                        BookLine("lives in his name.", 61),
                        BookLine("The Dagon'hai never", 62),
                        BookLine("reappeared in Varrock,", 63),
                        BookLine("yet the people knew", 64),
                        BookLine("that one day they would", 65),
                    ),
                    Page(
                        BookLine("return to have their", 66),
                        BookLine("revenge.", 67),
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
        on(Items.DAGONHAI_HISTORY_11001, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
