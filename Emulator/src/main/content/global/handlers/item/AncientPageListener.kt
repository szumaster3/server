package content.global.handlers.item

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Represents the ancient pages.
 */
class AncientPageListener : InteractionListener {

    companion object {
        private val ancientPageTranscripts = listOf(
            // Ancient page number 1.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     I have researched",
                "these accursed creatures of mithril more, the results are disturbing.",
                "Analysis leads me to believe that they were created by a higher power,",
                "what sort of creature could be so powerful?"
            ),

            // Ancient page number 2.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     ...metallic for missiles",
                "and magical energies...green for close combat and magic...",
                "both have their breath weapon for which they are infamous...",
                "next I will try reason and logic, may Guthix watch over me.",
            ),

            // Ancient page number 3.
            listOf(
                "",
                "",
                "                                                     I must caution against",
                "offering the wrong gifts to the spirits inhabiting the",
                "world beyond this. They seem quick to anger if the",
                "offerer was in any way responsible for the offering's",
                "death.",
            ),

            // Ancient page number 4.
            listOf(
                "",
                "",
                "                                                     Item 1 - mithril present",
                "in large quantities as suspected. Item 2 - guarded by",
                "vast dragons of the same metal. Recommendation - this",
                "has potential to undermine our monopoly, suggest we",
                "keep the location secret.",
            ),

            // Ancient page number 5.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     The combination",
                "of each of these prime ingredients will result in a fine",
                "pie, heightening the reactions of the lucky fellow who eats it.",
                "Sadly, most demand in my establishment is from scruffy fellows",
                "who make the place look untidy.",
            ),

            // Ancient page number 6.
            listOf(
                "",
                "",
                "                                                     ...no one I know of",
                "has access to an onyx amulet, the cost is just too",
                "immense. Enchanted, charged dragonstone amulets",
                "appear to be able to increase the perceptions of the",
                "wearer, at least when mining rocks for ore, they can",
                "increase the yield of precious gems.",
            ),

            // Ancient page number 7.
            listOf(
                "",
                "",
                "                                                     ...because her coach",
                "was a pumpkin. We laughed so much I thought my sides",
                "would split. I think that too much dwarven ale may have",
                "clouded by views, since it no longer seems a fit jest...",
            ),

            // Ancient page number 8.
            listOf(
                "",
                "",
                "                                                     In search of the",
                "incarnation of Scabaras, I probe the darkest recesses",
                "of the hidden world. If he lies in wait, I must be the one to",
                "reunite him with his fellow deities. What would befall us if",
                "he were forever isolated? His bitterness can only grow...",
            ),

            // Ancient page number 9.
            listOf(
                "",
                "",
                "                                                     I am not sure anyone",
                "will ever read this, as I say my final farewells. If any find",
                "this, pass on my regards to Annalise, if she still lives. I",
                "cannot see this being a place visited by those as",
                "doomed as I am.",
            ),

            // Ancient page number 10.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     If I ever get",
                "my hands on Otto he'll be in his grave. Sending me to this place",
                "to fester and die. 'See wonders' he says 'Impress the spirits'",
                "he says. The only way I'll be impressing the spirits is as one.",
                "While he sits out there living in luxury...",
            ),

            // Ancient page number 11.
            listOf(
                "",
                "",
                "                                                     ...is the stone",
                "here?...was the stone here?...will the stone be",
                "here?...would that my sanity were firm enough that I",
                "knew what my questions meant. Then I might seek an",
                "answer.",
            ),

            // Ancient page number 12.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     A momentary flash",
                "but I saw it! Hornless but the same deep crimson of",
                "its set-mates. A glorious thing to see, guarded by the",
                "creatures in the upper galleries. I must have it,",
                "I will have it!",
            ),

            // Ancient page number 13.
            listOf(
                "",
                "",
                "                                                     It read 'Laufata ki",
                "Glough ki Ta Quir Priw Undo eso, Tolly, gnomo kar is",
                "Glough hamo sarko pro Arposandra Qua!' - If only I had",
                "studied my languages more diligently.",
            ),

            // Ancient page number 14.
            listOf(
                "",
                "",
                "                                                     When Pukkamay first",
                "made toad crunchies, everyone thought he was mad.",
                "'Chewy toads, in crunchies? It'll never work' they said -",
                "how wrong they were...",
            ),

            // Ancient page number 15.
            listOf(
                "",
                "",
                "                                                     ...I learned my lesson",
                "when I tried to take that Zamorakian wine. I really think",
                "the monks overreacted. I always thought they were",
                "supposed to be teetotal anyway.",
            ),

            // Ancient page number 16.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     The Baxtorian Falls",
                "are well worth investigating, in my opinion, since water",
                "leaves the lake above this area yet none appears to flow",
                "into it. What could be the source of this quantity of",
                "water?",
            ),

            // Ancient page number 17.
            listOf(
                "",
                "",
                "                                                     My investigations",
                "show that water temperature in the Baxtorian Falls area",
                "are substantially higher than might be expected given",
                "the local climate. The source of this heat will now be the",
                "focus of my studies.",
            ),

            // Ancient page number 18.
            listOf(
                "",
                "",
                "                                                     Our best guess is that",
                "some sort of heat generation process is occurring in the",
                "tunnels we have scryed, though the denizens are",
                "proving quite a hindrance to our free travel. Research",
                "was never this dangerous for my teachers.",
            ),

            // Ancient page number 19.
            listOf(
                "",
                "",
                "                                                     Barakur has proved his",
                "worth, as we were forced to slay several dragons on our",
                "way to inner regions. The caves are clearly of artificial",
                "manufacture, though the excavations must have been",
                "thousands of years ago, to judge by the...",
            ),

            // Ancient page number 20.
            listOf(
                "",
                "",
                "                                                     Our theory of artificial",
                "origins has been vindicated! We have discovered a",
                "sturdy door, which seems to be a similar age to the",
                "original excavations. I am sure that there is magical or",
                "mechanical activity behind it, though it may simply be a",
                "great rush of water.",
            ),

            // Ancient page number 21.
            listOf(
                "",
                "",
                "                                                     The door resists all of",
                "our efforts to penetrate it, even the Fishing explosives",
                "which Derril suggested were tried with no success. There",
                "must be some way through which is linked to magic,",
                "though perhaps all who know the secret are now long",
                "dead."
            ),

            // Ancient page number 22.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     Buy from Bob's",
                "Brilliant Axes - if you see our prices bettered, let us",
                "know and we will beat them. Offer subject to availability",
                "and at Bob's discretion. Does not affect your legal rights",
                "under Lumbridge laws.",
            ),

            // Ancient page number 23.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     ...created spheres",
                "of power to support those who should come to that, dread",
                "place thereafter. But the well senses the light of these",
                "spheres, and will not...",
            ),

            // Ancient page number 24.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     ...barred the magi",
                "loyal to the great Lord Zamorak from entering the tower,",
                "keeping the new-found power of the runes to themselves. But",
                "one brave soul gathered his followers and forced his way",
                "into the library...",
            ),

            // Ancient page number 25.
            listOf(// TODO: Handle split correction caused by absent source data.
                "",
                "",
                "                                                     ...and I have lost",
                "my way in this accursed dungeon. The air is foul and bugs",
                "are everywhere...a noise from down the tunnel. There was a",
                "bell on the floor. Grasping the bell I rang it with all my",
                "might...it attacked me.",
            ),

            // Ancient page number 26.
            listOf(
                "",
                "",
                "                                                     ...perhaps the",
                "dragonkin were involved? A wild theory, I know, but look",
                "at the evidence..."
            )
        )
    }

    private val ancientPages = (Items.ANCIENT_PAGE_11341..Items.ANCIENT_PAGE_11366).toIntArray()
    private val BLOODY_SCROLL_SD = Components.BLOODY_SCROLL_101
    private val BLOODY_SCROLL_HD = Components.CONTACT_SCROLL_BLOOD_498
    override fun defineListeners() {

        /*
         * Handles interaction with ancient pages.
         */

        on(ancientPages, IntType.ITEM, "copy to log") { player, node ->
            val logBook = GameAttributes.LORE_ANCIENT_PAGES_TRANSCRIPT + ":" + node.id

            if (removeItem(player, node.asItem())) {
                if (getAttribute(player, logBook, false)) {
                    sendMessage(player, "You already have this information in your logbook.")
                } else {
                    setAttribute(player, logBook, true)
                    sendMessage(player, "You copy the scrap of information to your logbook.")
                }

                openInterface(player, BLOODY_SCROLL_HD).also {
                    val index = node.id - Items.ANCIENT_PAGE_11341
                    val pageContent = ancientPageTranscripts.getOrNull(index)?.joinToString("<br>")
                        ?: "You already have this information in your logbook."

                    sendString(player, pageContent, BLOODY_SCROLL_HD, 1)
                }
            }
            return@on true
        }
    }
}