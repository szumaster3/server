package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class MyNotesBook : InteractionListener {
    /*
     * Book given during Barbarian Training by Otto Godblessed.
     */

    companion object {
        private const val TITLE = "My notes"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Ancient page 1.", 55),
                        BookLine("I have researched these", 56),
                        BookLine("accursed creatures of", 57),
                        BookLine("mithril more, the results", 58),
                        BookLine("are disturbing. Analysis", 59),
                        BookLine("leads me to believe that", 60),
                        BookLine("they were created by a", 61),
                        BookLine("higher power, what sort", 62),
                        BookLine("of creature could be", 63),
                        BookLine("so powerful?", 64),
                    ),
                    Page(
                        BookLine("<col=08088A>Ancient page 2.", 66),
                        BookLine("...metallic for missiles", 67),
                        BookLine("and magical energies...", 68),
                        BookLine("green for close combat", 69),
                        BookLine("and magic...both have", 70),
                        BookLine("their breath weapon for", 71),
                        BookLine("which they are infamous", 72),
                        BookLine("...next I will try reason", 73),
                        BookLine("and logic, may Guthix", 74),
                        BookLine("watch over me.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Ancient page 3.", 55),
                        BookLine("I must caution against", 56),
                        BookLine("offering the wrong gifts", 57),
                        BookLine("to the spirits inhabiting", 58),
                        BookLine("the world beyond this.", 59),
                        BookLine("They seem quick to anger", 60),
                        BookLine("if the offerer was in any", 61),
                        BookLine("way responsible for the", 62),
                        BookLine("offering's death.", 63),
                        BookLine("<col=08088A>Ancient page 4.", 65),
                    ),
                    Page(
                        BookLine("Item 1 - mithril present", 66),
                        BookLine("in large quantities", 67),
                        BookLine("as suspected.", 68),
                        BookLine("Item 2 - guarded by vast", 70),
                        BookLine("dragons of the same metal.", 71),
                        BookLine("Recommendation - this", 72),
                        BookLine("has potential to undermine", 73),
                        BookLine("our monopoly, suggest", 74),
                        BookLine("we keep the location secret.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Ancient page 5.", 55),
                        BookLine("The combination of each", 56),
                        BookLine("of these prime ingredients", 57),
                        BookLine("will result in a fine pie,", 58),
                        BookLine("heightening the reactions", 59),
                        BookLine("of the lucky fellow who", 60),
                        BookLine("eats it. Sadly, most demand", 61),
                        BookLine("in my establishment is", 62),
                        BookLine("from scruffy fellows", 63),
                        BookLine("who make the place", 64),
                        BookLine("look untidy.", 65),
                    ),
                    Page(
                        BookLine("<col=08088A>Ancient page 6.", 66),
                        BookLine("...no one I know of has", 67),
                        BookLine("access to an onyx amulet,", 68),
                        BookLine("the cost is just too", 69),
                        BookLine("immense. Enchanted,", 70),
                        BookLine("charged dragonstone amulets", 71),
                        BookLine("appear to be able to", 72),
                        BookLine("increase the perceptions", 73),
                        BookLine("of the wearer, at least", 74),
                        BookLine("when mining rocks for ore,", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("they can increase the", 55),
                        BookLine("yield of precious gems.", 56),
                        BookLine("<col=08088A>Ancient page 7.", 58),
                        BookLine("...because her coach", 59),
                        BookLine("was a pumpkin. We laughed", 60),
                        BookLine("so much I thought my sides", 61),
                        BookLine("would split. I think that", 62),
                        BookLine("too much dwarven ale may", 63),
                        BookLine("have clouded by views,", 64),
                        BookLine("since it no longer seems", 65),
                    ),
                    Page(
                        BookLine("a fit jest...", 66),
                        BookLine("<col=08088A>Ancient page 8.", 68),
                        BookLine("In search of the", 69),
                        BookLine("incarnation of Scabaras,", 70),
                        BookLine("I probe the darkest", 71),
                        BookLine("recesses of the hidden", 72),
                        BookLine("world. If he lies in wait,", 73),
                        BookLine("I must be the one to", 74),
                        BookLine("reunite him with his", 75),
                        BookLine("fellow deities. What", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("would befall us if he", 55),
                        BookLine("were forever isolated?", 56),
                        BookLine("His bitterness can", 57),
                        BookLine("only grow...", 58),
                        BookLine("<col=08088A>Ancient page 9.", 60),
                        BookLine("I am not sure anyone will", 61),
                        BookLine("ever read this, as I say my", 62),
                        BookLine("final farewells. If any", 63),
                        BookLine("find this, pass on my", 64),
                        BookLine("regards to Annalise, if", 65),
                    ),
                    Page(
                        BookLine("she still lives. I cannot", 66),
                        BookLine("see this being a place", 67),
                        BookLine("visited by those as", 68),
                        BookLine("doomed as I am.", 69),
                        BookLine("<col=08088A>Ancient page 10.", 71),
                        BookLine("If I ever get my hands", 72),
                        BookLine("on Otto he'll be in his", 73),
                        BookLine("grave. Sending me to", 74),
                        BookLine("this place to fester", 75),
                        BookLine("and die. 'See wonders'", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("he says 'Impress the spirits'", 55),
                        BookLine("he says. The only way", 56),
                        BookLine("I'll be impressing the", 57),
                        BookLine("spirits is as one.", 58),
                        BookLine("While he sits out there", 59),
                        BookLine("living in luxury...", 60),
                        BookLine("<col=08088A>Ancient page 11.", 62),
                        BookLine("...is the stone here?...", 63),
                        BookLine("was the stone here?...", 64),
                        BookLine("will the stone be here?...", 65),
                    ),
                    Page(
                        BookLine("would that my sanity were", 66),
                        BookLine("firm enough that I knew", 67),
                        BookLine("what my questions meant.", 68),
                        BookLine("Then I might seek an answer.", 69),
                        BookLine("<col=08088A>Ancient page 12.", 71),
                        BookLine("A momentary flash but", 72),
                        BookLine("I saw it! Hornless but", 73),
                        BookLine("the same deep crimson", 74),
                        BookLine("of its set-mates.", 75),
                        BookLine("A glorious thing to see,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("guarded by the creatures", 55),
                        BookLine("in the upper galleries.", 56),
                        BookLine("I must have it,", 57),
                        BookLine("I will have it!", 58),
                        BookLine("<col=08088A>Ancient page 13.", 60),
                        BookLine("It read 'Laufata ki", 61),
                        BookLine("Glough ki Ta Quir Priw", 62),
                        BookLine("Undo eso, Tolly, gnomo", 63),
                        BookLine("kar is Glough hamo sarko", 64),
                        BookLine("pro Arposandra Qua!'", 65),
                    ),
                    Page(
                        BookLine("- If only I had studied", 66),
                        BookLine("my languages more", 67),
                        BookLine("diligently.", 68),
                        BookLine("<col=08088A>Ancient page 14.", 70),
                        BookLine("When Pukkamay first made", 71),
                        BookLine("toad crunchies, everyone", 72),
                        BookLine("thought he was mad.", 73),
                        BookLine("'Chewy toads, in crunchies?", 74),
                        BookLine("It'll never work' they said", 75),
                        BookLine("- how wrong they were...", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Ancient page 15.", 55),
                        BookLine("...I learned my lesson", 56),
                        BookLine("when I tried to take", 57),
                        BookLine("that Zamorakian wine.", 58),
                        BookLine("I really think the monks", 59),
                        BookLine("overreacted. I always", 60),
                        BookLine("thought they were supposed", 61),
                        BookLine("to be teetotal anyway.", 62),
                        BookLine("<col=08088A>Ancient page 16.", 64),
                    ),
                    Page(
                        BookLine("The Baxtorian Falls are", 66),
                        BookLine("well worth investigating,", 67),
                        BookLine("in my opinion, since water", 68),
                        BookLine("leaves the lake above this", 69),
                        BookLine("area yet none appears to", 70),
                        BookLine("flow into it. What could", 71),
                        BookLine("be the source of this", 72),
                        BookLine("quantity of water?", 73),
                        BookLine("<col=08088A>Ancient page 17.", 75),
                        BookLine("My investigations show", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("that water temperature", 55),
                        BookLine("in the Baxtorian Falls", 56),
                        BookLine("area are substantially", 57),
                        BookLine("higher than might be", 58),
                        BookLine("expected given the local", 59),
                        BookLine("climate. The source of", 60),
                        BookLine("this heat will now be", 61),
                        BookLine("the focus of my studies.", 62),
                        BookLine("<col=08088A>Ancient page 18.", 64),
                        BookLine("Our best guess is that", 65),
                    ),
                    Page(
                        BookLine("some sort of heat generation", 66),
                        BookLine("process is occurring in", 67),
                        BookLine("the tunnels we have scryed,", 68),
                        BookLine("though the denizens are", 69),
                        BookLine("proving quite a hindrance", 70),
                        BookLine("to our free travel. Research", 71),
                        BookLine("was never this dangerous", 72),
                        BookLine("for my teachers.", 73),
                        BookLine("<col=08088A>Ancient page 19.", 75),
                        BookLine("Barakur has proved his worth,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("as we were forced to slay", 55),
                        BookLine("several dragons on our way", 56),
                        BookLine("to inner regions. The caves", 57),
                        BookLine("are clearly of artificial", 58),
                        BookLine("manufacture, though the", 59),
                        BookLine("excavations must have been", 60),
                        BookLine("thousands of years ago,", 61),
                        BookLine("to judge by the...", 62),
                        BookLine("<col=08088A>Ancient page 20.", 64),
                        BookLine("Our theory of artificial", 65),
                    ),
                    Page(
                        BookLine("origins has been vindicated!", 66),
                        BookLine("We have discovered a sturdy", 67),
                        BookLine("door, which seems to be a", 68),
                        BookLine("similar age to the original", 69),
                        BookLine("excavations. I am sure that", 70),
                        BookLine("there is magical or mechanical", 71),
                        BookLine("activity behind it, though it", 72),
                        BookLine("may simply be a great", 73),
                        BookLine("rush of water.", 74),
                        BookLine("<col=08088A>Ancient page 21.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The door resists all of our", 55),
                        BookLine("efforts to penetrate it,", 56),
                        BookLine("even the Fishing explosives", 57),
                        BookLine("which Derril suggested were", 58),
                        BookLine("tried with no success.", 59),
                        BookLine("There must be some way", 60),
                        BookLine("through which is linked to", 61),
                        BookLine("magic, though perhaps all", 62),
                        BookLine("who know the secret are", 63),
                        BookLine("now long dead.", 64),
                    ),
                    Page(
                        BookLine("<col=08088A>Ancient page 22.", 66),
                        BookLine("Buy from Bob's Brilliant Axes", 67),
                        BookLine("- if you see our prices", 68),
                        BookLine("bettered, let us know and", 69),
                        BookLine("we will beat them. Offer", 70),
                        BookLine("subject to availability", 71),
                        BookLine("and at Bob's discretion.", 72),
                        BookLine("Does not affect your legal", 73),
                        BookLine("rights under Lumbridge laws.", 74),
                        BookLine("<col=08088A>Ancient page 23.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("...created spheres of power", 55),
                        BookLine("to support those who should", 56),
                        BookLine("come to that dread place", 57),
                        BookLine("thereafter. But the well", 58),
                        BookLine("senses the light of these", 59),
                        BookLine("spheres, and will not...", 60),
                        BookLine("<col=08088A>Ancient page 24.", 62),
                        BookLine("...barred the magi loyal", 63),
                        BookLine("to the great Lord Zamorak", 64),
                        BookLine("from entering the tower,", 65),
                    ),
                    Page(
                        BookLine("keeping the new-found", 66),
                        BookLine("power of the runes to", 67),
                        BookLine("themselves. But one brave", 68),
                        BookLine("soul gathered his followers", 69),
                        BookLine("and forced his way into", 70),
                        BookLine("the library...", 71),
                        BookLine("<col=08088A>Ancient page 25.", 73),
                        BookLine("...and I have lost my way", 74),
                        BookLine("in this accursed dungeon.", 75),
                        BookLine("The air is foul and bugs", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("are everywhere...a noise", 55),
                        BookLine("from down the tunnel.", 56),
                        BookLine("There was a bell on", 57),
                        BookLine("the floor. Grasping the", 58),
                        BookLine("bell I rang it with all", 59),
                        BookLine("my might...it attacked me.", 60),
                        BookLine("<col=08088A>Ancient page 26.", 62),
                        BookLine("...perhaps the dragonkin", 63),
                        BookLine("were involved? A wild", 64),
                        BookLine("theory, I know, but", 65),
                        BookLine("look at the evidence...", 66),
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
        on(Items.MY_NOTES_11339, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
