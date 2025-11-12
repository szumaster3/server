package content.region.asgarnia.rimmington.plugin

import content.global.plugins.iface.BookInterface
import content.global.plugins.iface.BookLine
import content.global.plugins.iface.Page
import content.global.plugins.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import shared.consts.Scenery

class MelzarNotes : InteractionListener {

    companion object {
        private val melzarPages = arrayOf(
            PageSet(
                Page(
                    BookLine("...My ritual was a success!", 55),
                    BookLine("I have managed to subdue", 56),
                    BookLine("a lesser demon. I can", 57),
                    BookLine("channel its magic to", 58),
                    BookLine("master the forces of life", 59),
                    BookLine("and death - as long as its", 60),
                    BookLine("chains hold...", 61),
                )
            ),
            PageSet(
                Page(
                    BookLine("...On my fortieth attempt,", 55),
                    BookLine("the process was partially", 56),
                    BookLine("successful. The rat's flesh", 57),
                    BookLine("continues to decay, but it", 58),
                    BookLine("regains the faculty for", 59),
                    BookLine("movement that it had in life.", 60),
                    BookLine("It is too early to say how", 61),
                    BookLine("many of its mental faculties", 62),
                    BookLine("have survived...", 63),
                )
            ),
            PageSet(
                Page(
                    BookLine("...Close study of the undead", 55),
                    BookLine("rat's behaviour reveals that", 56),
                    BookLine("no behavioural patterns", 57),
                    BookLine("acquired in life survive the", 58),
                    BookLine("process, even if the brain is", 59),
                    BookLine("perfectly preserved. I will", 60),
                    BookLine("have to find a way to conjure", 61),
                    BookLine("the deceased spirit separately...", 62),
                )
            ),
            PageSet(
                Page(
                    BookLine("...I have searched through the", 55),
                    BookLine("ruins of the refugee camp and", 56),
                    BookLine("found many personal items. I", 57),
                    BookLine("can use these to conjure the", 58),
                    BookLine("spirits of my deceased", 59),
                    BookLine("countrymen...", 60),
                )
            ),
            PageSet(
                Page(
                    BookLine("...I used the portrait of my", 55),
                    BookLine("wife in the ritual, and I am", 56),
                    BookLine("sure that the smoke formed", 57),
                    BookLine("fleetingly into the shape of", 58),
                    BookLine("her face. I could barely", 59),
                    BookLine("contain my tears to see her", 60),
                    BookLine("once again. She seemed in", 61),
                    BookLine("such pain...", 62),
                )
            ),
            PageSet(
                Page(

                    BookLine("...I have nearly perfected the", 55),
                    BookLine("formula: I can now conjure", 56),
                    BookLine("ghosts that persist for several", 57),
                    BookLine("hours. I am sure I can soon", 58),
                    BookLine("make them permanent...", 59),


                    )
            ),
            PageSet(
                Page(
                    BookLine("...The ghosts have a", 55),
                    BookLine("recognisable form, but they", 56),
                    BookLine("are still non-corporeal. They also", 57),
                    BookLine("seem to be in great distress,", 58),
                    BookLine("but they do not respond to", 58),
                    BookLine("anything I say...", 59),
                )
            ),
            PageSet(
                Page(
                    BookLine("...The ghosts are still", 55),
                    BookLine("screaming. They seem to be in", 56),
                    BookLine("pain, and they want to be", 57),
                    BookLine("returned to true death. But", 58),
                    BookLine("when I have perfected my", 59),
                    BookLine("techniques they will thank me...", 60),
                )
            ),
            PageSet(
                Page(
                    BookLine("...Cried again today. Great", 55),
                    BookLine("weight in heart. I am causing", 56),
                    BookLine("so much pain to the souls I", 57),
                    BookLine("capture from the dead-world.", 58),
                    BookLine("But demon says they will", 59),
                    BookLine("thank me in the end...", 60),
                )
            ),
            PageSet(
                Page(

                    BookLine("...Had dream last night of great", 55),
                    BookLine("cabbage. Cabbage of Jas. Used", 56),
                    BookLine("by gods to create all life and", 57),
                    BookLine("magic. All runes have part of", 58),
                    BookLine("its power. So with runes I", 59),
                    BookLine("can control power above magic...", 60),
                )
            ),
            PageSet(
                Page(
                    BookLine("...I searched the ruins of the", 55),
                    BookLine("camp again and have assembled", 56),
                    BookLine("a number of complete skeletons.", 57),
                    BookLine("As the flesh is all gone it", 58),
                    BookLine("will be more difficult to", 59),
                    BookLine("animate them than the rats...", 60),

                    )
            ),
            PageSet(
                Page(
                    BookLine("...I have managed to create a", 55),
                    BookLine("magical binding that can bond", 56),
                    BookLine("bones to one another and", 57),
                    BookLine("create a complete, magically", 58),
                    BookLine("animated skeleton. Animating", 59),
                    BookLine("a creature with no internal", 60),
                    BookLine("anatomy proves difficult,", 61),
                    BookLine("however...", 62),
                )
            ),
            PageSet(
                Page(
                    BookLine("...have been able to bind the", 55),
                    BookLine("bones together. Demon says it", 56),
                    BookLine("will soon be time. Life force", 57),
                    BookLine("can be transferred...", 58),
                )
            ),
            PageSet(
                Page(
                    BookLine("...I have succeeded in", 55),
                    BookLine("animating several of the skeletons,", 56),
                    BookLine("but without complete anatomies", 57),
                    BookLine("they cannot hold the spirits", 58),
                    BookLine("of my dead countrymen.", 59),
                    BookLine("Most will serve as guards,", 60),
                    BookLine("but I have selected two on", 61),
                    BookLine("which I will attempt to grow", 62),
                    BookLine("flesh...", 63),
                )
            ),
            PageSet(
                Page(
                    BookLine("...Note to self: must stop", 55),
                    BookLine("thinking about cabbages...", 56),
                )
            ),
            PageSet(
                Page(
                    BookLine("...My attempts at re-creating", 55),
                    BookLine("my dead countrymen's flesh", 56),
                    BookLine("were a partial success, but", 57),
                    BookLine("the flesh decays as soon as", 58),
                    BookLine("it appears. The creature is", 59),
                    BookLine("still not complete enough to", 60),
                    BookLine("house a spirit...", 61),
                )
            ),
            PageSet(
                Page(

                    BookLine("...Have made flesh grow on", 55),
                    BookLine("bones like cabbage from seed.", 56),
                    BookLine("All power come from cabbage.", 57),
                    BookLine("Zombies not useful...", 58),
                )
            ),
            PageSet(
                Page(
                    BookLine("...With power custard have", 55),
                    BookLine("mastered life force. Souls of", 56),
                    BookLine("dead kinsmen scream and twist,", 57),
                    BookLine("but I can contain them into", 58),
                    BookLine("bodies.", 59),
                    BookLine("All hail Zamor Za Zaro Z ZZZ", 60),
                )
            ),
            PageSet(
                Page(
                    BookLine("...Cabbage cabbage cabcab", 55),
                    BookLine("cabbabbabbage egabac", 56),
                    BookLine("CABBAGE cbaaeg CCCC...", 57),
                )
            ),
            PageSet(
                Page(
                    BookLine("...hate dragons hate dragons", 55),
                    BookLine("hate dragons...", 56),
                )
            ),
            PageSet(
                Page(
                    BookLine("ELVARG!", 55),
                    BookLine("", 56),
                    BookLine("", 57),
                    BookLine("O cabbage, thou art sick!", 58),
                    BookLine("The fire-breathing worm,", 59),
                    BookLine("That flies in the night", 60),
                    BookLine("In the howling storm", 61),
                    BookLine("Has found out thy bed", 62),
                    BookLine("Of leafy joy", 63),
                    BookLine("And his dark secret love", 64),
                    BookLine("Does thy life destroy.", 65),
                )
            ),
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        val randomIndex = melzarPages.indices.random()
        val randomBook = melzarPages[randomIndex]
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, "Melzar's notes", arrayOf(randomBook))
        return true
    }

    override fun defineListeners() {
        on(Scenery.BOOKSHELVES_25046, IntType.SCENERY, "Search") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}