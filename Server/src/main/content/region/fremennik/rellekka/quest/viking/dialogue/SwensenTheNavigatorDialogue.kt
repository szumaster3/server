package content.region.fremennik.rellekka.quest.viking.dialogue

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Swensen The Navigator dialogue.
 *
 * # Relations
 * - [FremennikTrials]
 */
@Initializable
class SwensenTheNavigatorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        when {
            inInventory(player, Items.WEATHER_FORECAST_3705, 1) -> {
                playerl(FaceAnim.HAPPY, "I would like your map of fishing spots.")
                stage = 120
            }

            inInventory(player, Items.SEA_FISHING_MAP_3704, 1) -> {
                playerl(
                    FaceAnim.ASKING,
                    "If this map of fishing spots is so valuable, why did you give it away to me so easily?"
                )
                stage = 125
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false) -> {
                playerl(FaceAnim.ASKING, "I have a trade item here.")
                stage = 130
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 9 -> {
                npcl(FaceAnim.HAPPY, "Greetings Outlander.")
                stage = 115
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 8 -> {
                npcl(FaceAnim.HAPPY, "Greetings Outlander.")
                stage = 105
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_MAZE, false) -> {
                npc("Outlander! You have finished my maze!", "I am genuinely impressed!")
                stage = 100
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_VOTE, false) -> {
                npc("You have my vote!")
                stage = 1000
            }

            isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                playerl(FaceAnim.HAPPY, "Hello!")
                stage = 140
            }

            player.questRepository.hasStarted(Quests.THE_FREMENNIK_TRIALS) -> {
                player("Hello!")
                stage = 0
            }

            else -> {
                playerl(FaceAnim.HAPPY, "Hello!")
                stage = 145
            }
        }

        return true
    }


    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                player(
                    "I am trying to become a member of the Fremennik",
                    "clan! The Chieftan told me that I may be able to gain",
                    "your vote at the council of elders?",
                )
                stage++
            }

            1 -> {
                npc(
                    "You wish to stop being an Outlander? I can",
                    "understand that! I have no reason why I would prevent",
                    "you becoming a Fremennik...",
                )
                stage++
            }

            2 -> {
                npc("...but you must first pass a little test for me to prove", "you are worthy.")
                stage++
            }

            3 -> {
                player("What kind of test?")
                stage++
            }

            4 -> {
                npc(
                    "Well, I serve our clan as a navigator. The seas can be",
                    "a fearful place when you know not where you are",
                    "heading.",
                )
                stage++
            }

            5 -> {
                npc(
                    "Should something happen to me, all members of our",
                    "tribe have some basic sense of direction so that they",
                    "may always return safely home.",
                )
                stage++
            }

            6 -> {
                npc(
                    "If you are able to demonstrate to me that you too have",
                    "a good sense of direction then I will recommend you to",
                    "the rest of the council of elders immediately.",
                )
                stage++
            }

            7 -> {
                player("Well, how would I go about showing that?")
                stage++
            }

            8 -> {
                npc(
                    "Ah, a simple task! Below this building I have constructed",
                    "a maze; should you be able to walk from one side to the",
                    "other that will be proof to me.",
                )
                stage++
            }

            9 -> {
                npc("You wish to try my challenge?")
                stage++
            }

            10 -> {
                options("Yes", "No")
                stage++
            }

            11 -> when (buttonId) {
                1 -> {
                    player("A maze? Is that all? Sure, it sounds simple enough.")
                    stage++
                }

                2 -> {
                    player("No, that sounds too hard.")
                    stage = 1000
                }
            }

            12 -> {
                npc(
                    "I will warn you Outlander, this maze was designed by",
                    "myself, and is of the most fiendish complexity!",
                )
                stage++
            }

            13 -> {
                player("Oh really? Watch and learn...")
                stage = 1000
                setAttribute(
                    player,
                    GameAttributes.QUEST_VIKING_SWENSEN_START,
                    true,
                )
            }

            100 -> {
                player(
                    "So does that mean I can rely on your vote at the",
                    "council of elders to allow me into your village?",
                )
                stage++
            }

            101 -> {
                npc("Of course Outlander! I am nothing if not a man of", "my word!")
                stage++
            }

            102 -> {
                player("Thanks!")
                removeAttributes(
                    player, GameAttributes.QUEST_VIKING_SWENSEN_MAZE, GameAttributes.QUEST_VIKING_SWENSEN_START
                )
                setAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_VOTE, true)
                setAttribute(
                    player,
                    GameAttributes.QUEST_VIKING_VOTES,
                    getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0) + 1
                )
                stage = 1000
            }

            105 -> playerl(
                FaceAnim.ASKING,
                "I don't suppose you have any idea where I could find a map of deep sea fishing spots do you?",
            ).also { stage++ }

            106 -> npcl(
                FaceAnim.HAPPY,
                "Hmmm? Why of course! As the navigator for the Fremennik I keep all of our maps secure right here.",
            ).also { stage++ }

            107 -> playerl(FaceAnim.HAPPY, "Great! Can I have it?").also { stage++ }
            108 -> npcl(
                FaceAnim.ANNOYED,
                "Have it? Just like that? I think not Outlander. This map shows all of the prime fishing locations nearby. It is very valuable to our clan. I am afraid I can not just give it away.",
            ).also { stage++ }

            109 -> playerl(FaceAnim.THINKING, "Perhaps I can trade you something for it?").also { stage++ }
            110 -> npcl(
                FaceAnim.ANNOYED,
                "A trade? For a map of the best fishing spots in a hundred leagues? I will trade it for no less than a weather forecast from our Seer.",
            ).also { stage++ }

            111 -> npcl(
                FaceAnim.THINKING,
                "As a navigator, the weather is extremely important for plotting the best course. Unfortunately the Seer is always too busy to help me with a forecast.",
            ).also { stage++ }

            112 -> playerl(FaceAnim.ASKING, "Where could I get a weather forecast from then?").also { stage++ }
            113 -> npcl(
                FaceAnim.ANNOYED,
                "I just told you: from the Seer. You will need to persuade him to take the time to make a forecast somehow.",
            ).also {
                player.incrementAttribute(GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 1)
                stage = 1000
            }

            115 -> playerl(
                FaceAnim.THINKING,
                "I don't suppose you have any idea where I could find a weather forecast from the Fremennik Seer do you?",
            ).also { stage++ }

            116 -> npcl(FaceAnim.ANNOYED, "Uh... from the Seer perhaps?").also { stage = 1000 }
            120 -> npcl(
                FaceAnim.ANNOYED,
                "I have already told you Outlander; I will not exchange it for anything other than a divination on the weather from our seer himself!",
            ).also { stage++ }

            121 -> playerl(FaceAnim.HAPPY, "What, like this one I have here?").also { stage++ }
            122 -> npcl(FaceAnim.AMAZED, "W-what...? I don't believe it! How did you...?").also { stage++ }
            123 -> npcl(
                FaceAnim.HAPPY,
                "I suppose it doesn't matter, you have my gratitude Outlander! With this forecast I will be able to plan a safe course for our next raiding expedition!",
            ).also {
                removeItem(player, Items.WEATHER_FORECAST_3705)
                addItemOrDrop(player, Items.SEA_FISHING_MAP_3704, 1)
                stage++
            }

            124 -> npcl(
                FaceAnim.HAPPY,
                "Here, Outlander; you may take my map of local fishing patterns with my gratitude!",
            ).also { stage = 1000 }

            125 -> npcl(
                FaceAnim.HAPPY,
                "Hmmm? Well, firstly it will be of value to our entire clan, so I have lost nothing from giving it to you.",
            ).also { stage++ }

            126 -> npcl(
                FaceAnim.HAPPY,
                "The other reason is of course that I have already memorised it, so I can make myself another copy whenever I want!",
            ).also { stage = 1000 }

            130 -> npcl(FaceAnim.ANNOYED, "It isn't for me, I'm afraid.").also { stage = 1000 }
            140 -> npcl(
                FaceAnim.HAPPY, "Greetings to you ${if (player.isMale) "brother" else "sister"} ${
                    FremennikTrials.getFremennikName(player!!)
                }. How fare you today?"
            ).also { stage++ }

            141 -> playerl(FaceAnim.HAPPY, "I am fine thanks Swensen. How are you doing?").also { stage++ }
            142 -> npcl(FaceAnim.HAPPY, "I am fine too!").also { stage = 1000 }
            145 -> npcl(
                FaceAnim.HAPPY,
                "Hello Outlander. This is my home, I would be grateful if you would leave.",
            ).also { stage++ }

            146 -> playerl(FaceAnim.THINKING, "Oh. Okay.").also { stage++ }
            147 -> npcl(
                FaceAnim.HAPPY,
                "I am sorry Outlander, I will not offer you hospitality until my Chieftain has vouched for your honesty. This is our way.",
            ).also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SWENSEN_THE_NAVIGATOR_1283)
}
