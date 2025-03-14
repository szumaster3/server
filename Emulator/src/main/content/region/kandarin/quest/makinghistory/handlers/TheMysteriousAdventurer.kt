package content.region.kandarin.quest.makinghistory.handlers

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.node.entity.player.Player

class TheMysteriousAdventurer {
    companion object {
        private val TITLE = "The Mysterious Adventurer"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Word is spreading of a", 55),
                        BookLine("brave and intelligent", 56),
                        BookLine("adventurer who goes by", 57),
                        BookLine("the player has,", 58),
                        BookLine("talked to a great", 59),
                        BookLine("many people, helping", 60),
                        BookLine("solve problems and", 61),
                        BookLine("improving their lives.", 62),
                        BookLine("", 63),
                        BookLine("The list of achievements", 64),
                        BookLine("include:", 65),
                    ),
                    Page(
                        BookLine("- Confronting the demon", 66),
                        BookLine("  Delrith with the sword", 67),
                        BookLine("  Silverlight.", 68),
                        BookLine("", 69),
                        BookLine("- Returning the skull of", 70),
                        BookLine("  a ghost so that he may", 71),
                        BookLine("  rest in peace.", 72),
                        BookLine("", 73),
                        BookLine("- Collecting ingredients", 74),
                        BookLine("  for the chef in Lumbridge", 75),
                        BookLine("  to make a cake.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("- Helping Dr Fenkenstrain", 55),
                        BookLine("  create a monster.", 56),
                        BookLine("", 57),
                        BookLine("- Being launched from a", 58),
                        BookLine("  cannon wearing a gold", 59),
                        BookLine("  helm just to help an", 60),
                        BookLine("  old dwarf.", 61),
                        BookLine("", 62),
                        BookLine("- Meddling with a Mourner's", 63),
                    ),
                    Page(
                        BookLine("  soup in West Ardougne.", 66),
                        BookLine("", 67),
                        BookLine("- Shearing many sheep to", 68),
                        BookLine("  help Fred the Farmer.", 69),
                        BookLine("", 70),
                        BookLine("- Sabotaging the plans of", 71),
                        BookLine("  the black knights with", 72),
                        BookLine("  just a cabbage!", 73),
                        BookLine("", 74),
                        BookLine("- Turning a chicken called", 75),
                        BookLine("  Ernest into a man.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("- Participating in the", 56),
                        BookLine("  fishing contest at", 57),
                        BookLine("  Hemenster and winning", 58),
                        BookLine("  the prize.", 59),
                        BookLine("", 60),
                        BookLine("- Searching for a lost cat", 61),
                        BookLine("  and returning its kitten.", 62),
                        BookLine("", 63),
                        BookLine("- Defeating the incredible", 64),
                        BookLine("  threat of Nomad.", 65),
                    ),
                    Page(
                        BookLine("", 66),
                        BookLine("- Helping the Void Knights", 67),
                        BookLine("  and deciding the fate of", 68),
                        BookLine("  the Wizard Grayzag.", 69),
                        BookLine("", 70),
                        BookLine("- Tracking down and actually", 71),
                        BookLine("  touching Guthix's prized", 72),
                        BookLine("  stone of power.", 73),
                        BookLine("", 74),
                        BookLine("- Getting initiated into", 75),
                        BookLine("  the elitist Legend's Guild.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("- Fighting incredibly dangerous", 56),
                        BookLine("  sea creatures at the", 57),
                        BookLine("  Piscatoris Fishing Colony.", 58),
                        BookLine("", 59),
                        BookLine("- Uncovering an ancient magic", 60),
                        BookLine("  spellbook in the heart of", 61),
                        BookLine("  the desert.", 62),
                        BookLine("", 63),
                        BookLine("- Vanquishing a zombie disease", 64),
                        BookLine("  affecting deadly ogres.", 65),
                    ),
                    Page(
                        BookLine("", 66),
                        BookLine("- Making a beautiful, regal", 67),
                        BookLine("  quality garden for the", 68),
                        BookLine("  Queen of Varrock.", 69),
                        BookLine("", 70),
                        BookLine("- Charming all of the rats", 71),
                        BookLine("  in Port Sarim!", 72),
                        BookLine("", 73),
                        BookLine("- Battling against the", 74),
                        BookLine("  fearsome Dagannoth", 75),
                        BookLine("  alongside the Fremennik.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("The list goes on, so we can", 56),
                        BookLine("see that player is destined,", 57),
                        BookLine("is destined for great things", 58),
                        BookLine("for which Gielinor should", 59),
                        BookLine("be truly grateful!", 60),
                    ),
                ),
            )

        @Suppress("UNUSED_PARAMETER")
        private fun display(
            player: Player,
            pageNum: Int,
            buttonID: Int,
        ): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }

        fun openBook(player: Player) {
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
        }
    }
}
