package content.region.misthalin.varrock.museum.dialogue

import core.api.addItemOrDrop
import core.api.sendItemDialogue
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the information clerk dialogue.
 */
@Initializable
class InformationClerkDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc("Welcome to Varrock Museum. How can I help you today?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                sendOptions(player, "What would you like to do?", "Take a map of the Museum.", "Find out about the Dig Site exhibit.", "Find out about the Timeline exhibit.", "Find out about the Natural History exhibit.", "Find out about Kudos.")
                stage++
            }

            1 -> when (buttonId) {
                1 -> {
                    end()
                    sendItemDialogue(player, Items.MUSEUM_MAP_11184, "You reach and take a map of the Museum.")
                    addItemOrDrop(player, Items.MUSEUM_MAP_11184, 1)
                }

                2 -> {
                    player("Could you tell me about the Dig Site exhibit please?")
                    stage = 150
                }

                3 -> {
                    player("Could you tell me about the Timeline exhibit please?")
                    stage = 170
                }

                4 -> {
                    player("Could you tell me about the Natural History exhibit, please?")
                    stage = 180
                }

                5 -> {
                    npc("What is Kudos?")
                    stage = 230
                }
            }

            150 -> {
                npc(
                    "Of course. The Dig Site exhibit has several display cases",
                    "of finds discovered on the Dig Site to the east of Varrock."
                )
                stage++
            }

            151 -> {
                npc(
                    "As you've passed your Earth Science exams at the Dig",
                    "Site, you can go through into the cleaning area and clean",
                    "off some finds. This will help our Dig Site display floor to",
                    "give a more accurate view of life back in the 3rd and 5th Ages."
                )
                stage++
            }

            152 -> {
                npc(
                    "Ages, as well as earning you Kudos. If you'd like to know",
                    "more about cleaning finds, just ask the archaeologists."
                )
                stage++
            }

            153 -> {
                options("Ask about something else.", "Bye")
                stage++
            }

            154 -> when (buttonId) {
                1 -> {
                    player("What else can you inform me on?")
                    stage = 0
                }

                2 -> {
                    player("Bye!")
                    stage = END_DIALOGUE
                }
            }

            170 -> {
                npc(
                    "Why, yes. The Timeline exhibit has lots of display cases",
                    "showing things from the beginning of time right up to the",
                    "present day."
                )
                stage++
            }

            171 -> {
                npc(
                    "I know you've helped out a bit in the Timeline exhibit",
                    "upstairs, but I'm sure you can help more. When you're out",
                    "on your travels being a brave adventurer, remember that",
                    "you can come back to the Museum after some quests to"
                )
                stage++
            }

            172 -> {
                npc(
                    "let us know important historical facts. This will help us to",
                    "update the displays and make the Museum a more",
                    "informative place! You'll earn yourself Kudos too."
                )
                stage++
            }

            173 -> {
                player(
                    "Okay, thanks. One more question: why are the display",
                    "numbers all out of sequence?"
                )
                stage++
            }

            174 -> {
                npc(
                    "Ahh, that's due to the numbering being done as we were",
                    "constructing the cases and putting the displays in them,",
                    "then shuffling them into the right places. We thought",
                    "rather than renumbering them all - such a boring job,"
                )
                stage++
            }

            175 -> {
                npc(
                    "writing labels - we'd leave it. They all have unique numbers",
                    "and future displays would mess up the consecutive",
                    "numbering anyway."
                )
                stage++
            }

            176 -> {
                player("Ahhh, I see.")
                stage = 153
            }

            180 -> {
                npc(
                    "Why, yes. The Natural History exhibit has displays of",
                    "various creatures you can find around " + settings!!.name + "."
                )
                stage++
            }

            181 -> {
                npc(
                    "I see you have already demonstrated some of your",
                    "knowledge of the natural world in the Natural History",
                    "exhibit, so why not pop down and do some more quizzes",
                    "with Orlando! You can earn Kudos at the same time."
                )
                stage++
            }

            182 -> {
                options(
                    "But what's Natural History got to do with existing animals?",
                    "Ask about something else.", "Bye"
                )
                stage++
            }

            183 -> when (buttonId) {
                1 -> {
                    player("But what's natural history got to do with existing animals?")
                    stage++
                }

                2 -> {
                    player("What else can you inform me on?")
                    stage = 0
                }

                3 -> {
                    player("Bye!")
                    stage = END_DIALOGUE
                }
            }

            184 -> {
                npc(
                    "The study of natural history is simply the study of the",
                    "history of the species. The species doesn't necessarily",
                    "need to be an extinct one."
                )
                stage = 153
            }

            230 -> {
                npc(
                    "Kudos is a measure of how much you've assisted the",
                    "Museum. The more information you give us, Dig Site",
                    "finds that you clean and quizzes you solve, the",
                    "higher your Kudos."
                )
                stage++
            }

            231 -> {
                player("But what's it for?")
                stage++
            }

            232 -> {
                npc(
                    "Well, recently we found a rather interesting island to the",
                    "north of Morytania. We believe that it may be of",
                    "archaeological significance. I suspect we'll be looking for",
                    "qualified archaeologists once we have constructed our"
                )
                stage++
            }

            233 -> {
                npc(
                    "canal and barge. So, we're using Kudos as a measure of",
                    "who is willing and able to help us here at the Museum, so",
                    "they can then be invited on our digs on the new island."
                )
                stage++
            }

            234 -> {
                player("Would I qualify, then?")
                stage++
            }

            235 -> {
                npc(
                    "Unfortunately, you haven't earned enough Kudos yet, so",
                    "you aren't qualified to help us on the dig. If you're",
                    "interested in helping us out and getting that Kudos, simply",
                    "help out around the museum."
                )
                stage++
            }

            236 -> {
                player("Okay, thanks.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.INFORMATION_CLERK_5938)
    }
}
