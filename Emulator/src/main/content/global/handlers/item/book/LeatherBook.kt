package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class LeatherBook : InteractionListener {
    /*
     * Obtained from the ancient library during the In Aid of
     * the Myreque quest at Temple of Salve.
     */

    companion object {
        private const val TITLE = "Modern day Morytania"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Welcome dear reader", 55),
                        BookLine("to this tome. I entrust", 56),
                        BookLine("that it will suit to", 57),
                        BookLine("your enjoyment most", 58),
                        BookLine("amicably and perhaps", 59),
                        BookLine("in a scholarly pursuit,", 60),
                        BookLine("inform you of the manifest", 61),
                        BookLine("concerns alive in the", 62),
                        BookLine("land of Morytania. The", 63),
                        BookLine("land known as Morytania", 64),
                        BookLine("is as dark and uninviting", 65),
                    ),
                    Page(
                        BookLine("as Misthalin is light", 66),
                        BookLine("and welcoming. While", 67),
                        BookLine("the intention of the", 68),
                        BookLine("author is not to teach", 69),
                        BookLine("geography to its readership,", 70),
                        BookLine("some small amount of", 71),
                        BookLine("area description would", 72),
                        BookLine("perhaps shed useful", 73),
                        BookLine("light to the readership. By", 74),
                        BookLine("far the most common", 75),
                        BookLine("entrance into Morytania", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("is through the temple", 55),
                        BookLine("of Saradomin, herein", 56),
                        BookLine("after referred to as", 57),
                        BookLine("Paterdomus. It is located", 58),
                        BookLine("in lands known as Silvarea", 59),
                        BookLine("and is the site of", 60),
                        BookLine("the famous battle of", 61),
                        BookLine("the Salve. It is here", 62),
                        BookLine("that the seven brave", 63),
                        BookLine("priestly warriors stood", 64),
                        BookLine("shoulder to shoulder", 65),
                    ),
                    Page(
                        BookLine("to defend Misthalin", 66),
                        BookLine("from the darkness. Due", 67),
                        BookLine("east of Paterdomus", 68),
                        BookLine("is the wild and strangely", 69),
                        BookLine("populated village of", 70),
                        BookLine("Canifis. The citizens", 71),
                        BookLine("of this area are to", 72),
                        BookLine("be least trusted as", 73),
                        BookLine("many an innocent travelled", 74),
                        BookLine("there and has never", 75),
                        BookLine("returned. However,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("some of those who had", 55),
                        BookLine("sense enough to avoid", 56),
                        BookLine("the village did, on", 57),
                        BookLine("occasion, return from", 58),
                        BookLine("the evil lands to recount", 59),
                        BookLine("their stories, before", 60),
                        BookLine("being specially treated", 61),
                        BookLine("for diseased imaginings", 62),
                        BookLine("and fearfullness. North", 63),
                        BookLine("easterly of Canifis", 64),
                        BookLine("is a most impressive,", 65),
                    ),
                    Page(
                        BookLine("but dark Castle of", 66),
                        BookLine("no small stature, inside", 67),
                        BookLine("one may find the very", 68),
                        BookLine("stuff of nightmares.", 69),
                        BookLine("It is best avoided", 70),
                        BookLine("in the author's opinion,", 71),
                        BookLine("as is much which resides", 72),
                        BookLine("in Morytania. If one", 73),
                        BookLine("is to then make way", 74),
                        BookLine("through a most undesirable", 75),
                        BookLine("forest, infested with", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("creatures of darkness,", 55),
                        BookLine("one may happen upon", 56),
                        BookLine("a ghostly sea port,", 57),
                        BookLine("all death and decomposition.", 58),
                        BookLine("Though the author cannot", 59),
                        BookLine("confirm it, adventurers", 60),
                        BookLine("returning from such", 61),
                        BookLine("a place remarked that", 62),
                        BookLine("for those of stout", 63),
                        BookLine("composition, well rendered", 64),
                        BookLine("faculties and an eye", 65),
                    ),
                    Page(
                        BookLine("for profit could engage", 66),
                        BookLine("in meaningful work", 67),
                        BookLine("there and advance themselves.", 68),
                        BookLine("Returning once again", 69),
                        BookLine("to Canifis, one would", 70),
                        BookLine("see a huge swathe of", 71),
                        BookLine("rotten, foul smelling", 72),
                        BookLine("and life-threatening", 73),
                        BookLine("marsh. These great", 74),
                        BookLine("tracts of land are", 75),
                        BookLine("called 'Mort Myre';", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("it is the swamp of", 55),
                        BookLine("true terror. Within", 56),
                        BookLine("this dark, stench-ridden", 57),
                        BookLine("foulness lives a most", 58),
                        BookLine("disgusting creature", 59),
                        BookLine("known as the Ghast.", 60),
                        BookLine("Due in no small part", 61),
                        BookLine("to the confusing nature", 62),
                        BookLine("of the terrain and", 63),
                        BookLine("the lack of anything", 64),
                        BookLine("edible, many innocents", 65),
                    ),
                    Page(
                        BookLine("have died in this place,", 66),
                        BookLine("starved to death with", 67),
                        BookLine("hunger. When such a", 68),
                        BookLine("soul lives no longer,", 69),
                        BookLine("its form takes on the", 70),
                        BookLine("most disgusting of", 71),
                        BookLine("appearances. It hungers", 72),
                        BookLine("and eats anything it", 73),
                        BookLine("can, waylaying travellers", 74),
                        BookLine("and consuming their", 75),
                        BookLine("food, leaving nothing", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("but stinking rotten", 55),
                        BookLine("remains. If travellers", 56),
                        BookLine("have no food, it will", 57),
                        BookLine("extract life force", 58),
                        BookLine("from those it can to", 59),
                        BookLine("satiate its considerable", 60),
                        BookLine("hunger. Few that have", 61),
                        BookLine("travelled through Mort", 62),
                        BookLine("Myre have returned", 63),
                        BookLine("to give details of", 64),
                        BookLine("what lies beyond. The", 65),
                    ),
                    Page(
                        BookLine("author cannot rightly", 66),
                        BookLine("confirm the truth of", 67),
                        BookLine("what is reported here,", 68),
                        BookLine("but can simply transcribe", 69),
                        BookLine("it in the hope that", 70),
                        BookLine("one day the truth can", 71),
                        BookLine("be revealed. It is", 72),
                        BookLine("south of Mort Myre", 73),
                        BookLine("that the traveller", 74),
                        BookLine("may venture upon a", 75),
                        BookLine("twisty and turning", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("path which weaves around", 55),
                        BookLine("various large bodies", 56),
                        BookLine("of swamp water. To", 57),
                        BookLine("the south of this is", 58),
                        BookLine("said to be an ancient", 59),
                        BookLine("and evil mine. The", 60),
                        BookLine("path to this is difficult", 61),
                        BookLine("to discover but apparently", 62),
                        BookLine("holds great wealth", 63),
                        BookLine("inside of it. But gentle", 64),
                        BookLine("reader, I beseech you,", 65),
                    ),
                    Page(
                        BookLine("lay some value for", 66),
                        BookLine("your own life and leave", 67),
                        BookLine("this area to the doubtless", 68),
                        BookLine("evil creatures which", 69),
                        BookLine("prosper there on the", 70),
                        BookLine("lives of innocents", 71),
                        BookLine("in search of fame,", 72),
                        BookLine("fortune and glory. To", 73),
                        BookLine("the East of the twisty", 74),
                        BookLine("turning path is a small", 75),
                        BookLine("village known as Mort'ton.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Its inhabitants are", 55),
                        BookLine("both vile and deadly.", 56),
                        BookLine("Mort'ton is once said", 57),
                        BookLine("to have been a village", 58),
                        BookLine("making the focus of", 59),
                        BookLine("its living from the", 60),
                        BookLine("internment of important", 61),
                        BookLine("peoples into crypts", 62),
                        BookLine("beneath its rustic", 63),
                        BookLine("surface. Now the inhabitants", 64),
                        BookLine("long interred in their", 65),
                    ),
                    Page(
                        BookLine("crypts walk the surface", 66),
                        BookLine("of the village and", 67),
                        BookLine("drain the life force", 68),
                        BookLine("of innocent and adventurer", 69),
                        BookLine("alike. South of Mort'ton", 70),
                        BookLine("lies a larger but somewhat", 71),
                        BookLine("bedraggled township,", 72),
                        BookLine("long ago run into ruin.", 73),
                        BookLine("Its original name is", 74),
                        BookLine("lost, it is now mostly", 75),
                        BookLine("commonly referred to", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("as Burgh de Rott and", 55),
                        BookLine("most apt a name it", 56),
                        BookLine("is. All manner of ruined", 57),
                        BookLine("buildings litter this", 58),
                        BookLine("land. Some small token", 59),
                        BookLine("of civilization may", 60),
                        BookLine("have lived here at", 61),
                        BookLine("one point, but has", 62),
                        BookLine("long since vanished", 63),
                        BookLine("now. Little is accurately", 64),
                        BookLine("known of the eastern", 65),
                    ),
                    Page(
                        BookLine("side of Morytania except", 66),
                        BookLine("that it is known as", 67),
                        BookLine("the 'Sanguinesti' region", 68),
                        BookLine("by those that inhabit", 69),
                        BookLine("it. Also reported is", 70),
                        BookLine("that the northern area", 71),
                        BookLine("is referred to as 'Darkmeyer'", 72),
                        BookLine("and the southern side", 73),
                        BookLine("is called, 'Meiyerditch'.", 74),
                        BookLine("Those lucky few who", 75),
                        BookLine("managed to escape this", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("terrible place are", 55),
                        BookLine("few and far between", 56),
                        BookLine("so this information", 57),
                        BookLine("is largely unconfirmed. One", 58),
                        BookLine("final place which is", 59),
                        BookLine("worth a mention is", 60),
                        BookLine("the area to the east", 61),
                        BookLine("of Mort'ton, noted", 62),
                        BookLine("for its unusual appearance,", 63),
                        BookLine("three small hillocks", 64),
                        BookLine("are all that distinguish", 65),
                    ),
                    Page(
                        BookLine("this place from the", 66),
                        BookLine("rest of the area. And", 67),
                        BookLine("so dear reader, I have", 68),
                        BookLine("completed a brief and", 69),
                        BookLine("very unsure explanation", 70),
                        BookLine("of the geography of", 71),
                        BookLine("Morytania. Please excuse", 72),
                        BookLine("the author if further", 73),
                        BookLine("details prove to dispute", 74),
                        BookLine("all I have written", 75),
                        BookLine("here. This treatise", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("is merely the accumulation", 55),
                        BookLine("of vague reports gained", 56),
                        BookLine("from travellers surviving", 57),
                        BookLine("Morytania and exiting", 58),
                        BookLine("Paterdomus as they", 59),
                        BookLine("headed for the pleasanter", 60),
                        BookLine("lands of Misthalin.", 61),
                        BookLine("As example, it is reported", 62),
                        BookLine("that somewhere centrally", 63),
                        BookLine("located in the vastness", 64),
                        BookLine("of Morytania is a dark", 65),
                    ),
                    Page(
                        BookLine("and dire castle said", 66),
                        BookLine("to be the home of Drakan,", 67),
                        BookLine("the lord of Morytania.", 68),
                        BookLine("Though this has yet", 69),
                        BookLine("to be confirmed.", 70),
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
        on(Items.LEATHER_BOOK_7635, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
