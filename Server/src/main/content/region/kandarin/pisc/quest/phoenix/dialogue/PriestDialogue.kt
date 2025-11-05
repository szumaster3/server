package content.region.kandarin.pisc.quest.phoenix.dialogue

import content.data.GameAttributes
import content.region.kandarin.pisc.quest.phoenix.InPyreNeed
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * The Priest dialogue.
 *
 * # Relations
 * - [In Pyre Need][content.region.kandarin.pisc.quest.phoenix.InPyreNeed]
 */
@Initializable
class PriestDialogue : Dialogue {

    constructor()

    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        when (getVarbit(player, InPyreNeed.PROGRESS)) {
            1 -> {
                npc(FaceAnim.FRIENDLY, "You've returned!")
                stage = 27
            }

            2, 3, 4, 5, 6, 7, 8, 9 -> {
                npc(FaceAnim.FRIENDLY, player.username + "! How is your quest to save the phoenix", "progressing?")
                stage = 111
            }

            10 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    player.username + "! I saw everything through a small vent in",
                    "the roof of the phoenix roost! You have succeeded!"
                )
                stage = 100
            }

            else -> player(FaceAnim.FRIENDLY, "Hello, there.")
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (stage) {
            0 -> {
                npc(FaceAnim.SCARED, "Please, adventurer, I beg you. You must help her!")
                stage++
            }

            1 -> {
                player(FaceAnim.FRIENDLY, "Woah, hold your unicorns! Help who?")
                stage++
            }

            2 -> {
                npc(FaceAnim.HALF_GUILTY, "I...I'm sorry. My emotions have run away with me. ", "Allow me to explain.")
                stage++
            }

            3 -> {
                npc(FaceAnim.HALF_GUILTY, "This cave is a place of great significance. Do you know", "why?")
                stage++
            }

            4 -> {
                player(FaceAnim.FRIENDLY, "I can't say I do.")
                stage++
            }

            5 -> {
                npc(FaceAnim.HALF_GUILTY, "It is the roost of the legendary firebird, the phoenix!")
                stage++
            }

            6 -> {
                player(FaceAnim.FRIENDLY, "Really?")
                stage++
            }

            7 -> {
                player(FaceAnim.FRIENDLY, "But wait, how is this related to who I have to help?")
                stage++
            }

            8 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It is related because it is the phoenix that is in need of",
                    "your assistance."
                )
                stage++
            }

            9 -> {
                player(FaceAnim.FRIENDLY, "You're starting to confuse me.")
                stage++
            }

            10 -> {
                player(FaceAnim.FRIENDLY, "Perhaps you should take a deep breath and start from", "the beginning.")
                stage++
            }

            11 -> {
                npc(FaceAnim.HALF_GUILTY, "Okay...*sigh*")
                stage++
            }

            12 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I have been studying the phoenix for some time now,",
                    "out of curiosity and admiration."
                )
                stage++
            }

            13 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "While not truly immortal, the phoenix has the capacity",
                    "to live forever if certain...conditions...are met."
                )
                stage++
            }

            14 -> {
                player(FaceAnim.FRIENDLY, "Conditions?")
                stage++
            }

            15 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The phoenix has a natural lifespan of 500 years. There",
                    "is a set ritual it must go through when its life is ending,",
                    "in order for it to be reborn and live for another five",
                    "centuries."
                )
                stage++
            }

            16 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The end of the phoenix's current lifespan is nigh and it",
                    "has returned to its roost to complete this ritual. I came",
                    "here to witness this once in a lifetime event."
                )
                stage++
            }

            17 -> {
                player(
                    FaceAnim.FRIENDLY,
                    "So, the phoenix has returned to its roost and you came",
                    "to watch it be reborn. I fail to see the problem."
                )
                stage++
            }

            18 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The phoenix was gravely wounded on its way back to",
                    "its roost. It barely managed to reach its lair."
                )
                stage++
            }

            19 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It now rests there, slowly slipping from this world and",
                    "soon it will be gone forever, incapable of completing its",
                    "rebirth ritual owing to its wounds."
                )
                stage++
            }

            20 -> {
                player(FaceAnim.FRIENDLY, "A tragic tale, but what can I do about it?")
                stage++
            }

            21 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I know the ritual that must be completed. To save the",
                    "phoenix, you must go into the cave and perform it."
                )
                stage++
            }

            22 -> {
                npc(
                    FaceAnim.HAPPY,
                    "Completing the ritual before the phoenix takes its last",
                    "breath will ensure its revival, and continue the",
                    "potentially eternal life of this magnificent beast."
                )
                stage++
            }

            23 -> {
                player(
                    FaceAnim.FRIENDLY,
                    "If you know the ritual, why can't you venture forth",
                    "and help the phoenix yourself?"
                )
                stage++
            }

            24 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Because I am an old man whose mind and fingers lack",
                    "the necessary skill to complete the ritual..."
                )
                stage++
            }

            25 -> {
                npc(FaceAnim.HALF_GUILTY, "...and whose body lacks the necessary heat-resistance.")
                stage++
            }

            26 -> {
                player(FaceAnim.FRIENDLY, "...")
                stage++
            }

            27 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The task I ask of you will not take long, and you will",
                    "be well rewarded for your efforts. Will you please",
                    "complete the phoenix's ritual?"
                )
                stage++
            }

            28 -> {
                options("Yes, I will help.", "No, I have other things to do.")
                stage++
            }

            29 -> when (buttonID) {
                1 -> {
                    player("Yes, I will help.")
                    setAttribute(player, GameAttributes.TALK_WITH_PRIEST, 0)
                    setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 1, true)
                    setQuestStage(player, Quests.IN_PYRE_NEED, 2)
                    stage = 31
                }

                2 -> {
                    player("No, I have other things to do.")
                    stage++
                }
            }

            30 -> {
                npc(
                    FaceAnim.STRUGGLE,
                    "Well, I can't force you. If you change your mind, hurry back. The phoenix's life could fade at any moment."
                )
                stage++
            }

            31 -> {
                npc(
                    FaceAnim.HAPPY,
                    "You'll help? Oh, praise be to Guthix! I knew I could",
                    "trust you from the moment I saw you."
                )
                stage++
            }

            32 -> {
                npc(FaceAnim.FRIENDLY, "What is your name, adventurer?")
                stage++
            }

            33 -> {
                player(FaceAnim.FRIENDLY, "You can call me " + player.username + ".")
                stage++
            }

            34 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Well, " + player.username + ", it's nice to meet you. No doubt you",
                    "have questions about the task at hand."
                )
                stage++
            }

            35 -> {
                npc(FaceAnim.FRIENDLY, "What would you like to ask about?")
                stage++
            }

            36 -> {
                options("The phoenix.", "The ritual.", "The phoenix's lair.", "No more questions.")
                stage++
            }

            37 -> when (buttonID) {
                1 -> {
                    player(FaceAnim.FRIENDLY, "Could you tell me about the phoenix?")
                    player.incrementAttribute(GameAttributes.TALK_WITH_PRIEST)
                    stage++
                }

                2 -> {
                    player(FaceAnim.FRIENDLY, "Could you tell me about the phoenix's rebirth ritual?")
                    player.incrementAttribute(GameAttributes.TALK_WITH_PRIEST)
                    stage = 44
                }

                3 -> {
                    player(FaceAnim.FRIENDLY, "Could you tell me about the phoenix's lair?")
                    player.incrementAttribute(GameAttributes.TALK_WITH_PRIEST)
                    stage = 72
                }

                4 -> {
                    player("That's all the information I need for now.")
                    stage = 79
                }
            }

            38 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "The phoenix is an ancient creature - older than either",
                    "of us can comprehend."
                )
                stage++
            }

            39 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "It is a large, powerful avian creature, surrounded by",
                    "flames and covered in a fiery plumage. It also has",
                    "strong magical abilities."
                )
                stage++
            }

            40 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Every five hundred years, as it reaches the limits of its",
                    "lifespan, it returns to its roost to complete a ritual and",
                    "be reborn."
                )
                stage++
            }

            41 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "You see, while not truly immortal, the phoenix can be",
                    "reborn any number of times, so long as it completes its",
                    "rebirth ritual."
                )
                stage++
            }

            42 -> {
                npc(
                    FaceAnim.SAD,
                    "But, thanks to its wounds, the phoenix could soon be",
                    "gone from this world forever."
                )
                stage++
            }

            43 -> {
                player(FaceAnim.HALF_ASKING, "Thanks for the information. Now, back to my other", "questions.")
                stage = 35
            }

            44 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "The ritual is not that complicated, really; it just requires",
                    "certain ingredients and a set of dexterous hands."
                )
                stage++
            }

            45 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "In order to be reborn, the phoenix must be burnt alive",
                    "on a funeral pyre woven from the wood of five",
                    "particular trees."
                )
                stage++
            }

            46 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "You must weave a basket from the fletched twigs of",
                    "cinnamon, sassafras, ailanthus, cedar and mastic trees."
                )
                stage++
            }

            47 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "This basket must be placed on the base of the pyre in",
                    "the phoenix's roost, and then it must be lit."
                )
                stage++
            }

            48 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Usually, you would also need to best the phoenix in",
                    "combat to weaken it, so that it can succumb to the",
                    "flames."
                )
                stage++
            }

            49 -> {
                npc(FaceAnim.FRIENDLY, "This time, however, the phoenix is already weak and", "dying.")
                stage++
            }

            50 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "The phoenix will enter the pyre and be burnt to ashes.",
                    "From these ashes, it will be reborn - young and strong",
                    "again."
                )
                stage++
            }

            51 -> if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) < 2) {
                // First time
                player(
                    "Oh, I see. I need to search the four corners of the",
                    "world for five trees. Well, I suppose I'd best get",
                    "packing-"
                )
                stage++
            } else {
                // Second time
                npc(
                    FaceAnim.HAPPY,
                    "There are specimens of each tree growing inside the phoenix's lair, tended to by the phoenix's thralls in its absence and revived from the trees ashes whenever they die."
                )
                stage++
            }

            52 -> {
                npc(FaceAnim.HAPPY, "While I understand your concern, that won't be a", "problem at all.")
                stage++
            }

            53 -> {
                player("How so?")
                stage++
            }

            54 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "As I have said, the phoenix is an ancient creature with",
                    "some limited power over life and death."
                )
                stage++
            }

            55 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "The trees you require are growing inside its very lair,",
                    "and have been for millenia! The phoenix revives them",
                    "from their own ashes each time they die."
                )
                stage++
            }

            56 -> {
                player("That's amazing. What a foresighted creature the", "phoenix is!")
                setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 2, true)
                stage = 58
            }

            58 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Each tree is on a separate level of the lair. You must",
                    "collect the twigs before moving on. It's pointless to",
                    "continue on to a deeper level without having the twigs",
                    "from all the levels before it."
                )
                stage++
            }

            59 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Only fresh twigs will suffice for the ritual. If you leave",
                    "the lair at any time, you will have to go through and",
                    "collect fresh twigs from every tree."
                )
                stage++
            }

            60 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "There are some other items required for the ritual that",
                    "you cannot get from inside the lair."
                )
                stage++
            }

            61 -> {
                player("Oh? What would those be then?")
                stage++
            }

            62 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Tools. Secateurs, to prune the trees and gather the",
                    "twigs, a knife to fletch them and a tinderbox to light the",
                    "pyre."
                )
                stage++
            }

            63 -> {
                npc(
                    FaceAnim.SAD,
                    "Given the circumstances, I'd be happy to give you the",
                    "necessary tools for free. I brought a set to complete the",
                    "ritual myself, but...I'm too old."
                )
                stage++
            }

            64 -> {
                options("Yes, please give me the tools I need.", "No thank you, I will gather the tools myself.")
                stage++
            }

            65 -> when (buttonID) {
                1 -> {
                    player(FaceAnim.FRIENDLY, "Yes, please give me the tools I need.")
                    stage++
                }

                2 -> {
                    player(FaceAnim.FRIENDLY, "No thank you, I will gather the tools myself.")
                    stage = 71
                }
            }

            66 -> {
                sendItemDialogue(player, Items.SECATEURS_5329, "The priest hands you some secateurs.")
                addItemOrDrop(player, Items.SECATEURS_5329, 1)
                stage++
            }

            67 -> {
                sendItemDialogue(player, Items.KNIFE_946, "The priest hands you a knife.")
                addItemOrDrop(player, Items.KNIFE_946, 1)
                stage++
            }

            68 -> {
                sendItemDialogue(player, Items.TINDERBOX_590, "The priest hands you a tinderbox.")
                addItemOrDrop(player, Items.TINDERBOX_590, 1)
                stage++
            }

            69 -> {
                npc(FaceAnim.FRIENDLY, "You should now have all the tools you need for the", "ritual")
                stage++
            }

            70 -> {
                player("Thank you!")
                stage++
            }

            71 -> {
                player("Now, back to my other questions.")
                stage = 35
            }

            72 -> {
                npc(FaceAnim.FRIENDLY, "The phoenix's lair is a lava-filled cave, guarded by its", "thralls.")
                stage++
            }

            73 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "You see, the phoenix has power over life and death that",
                    "is not limited to itself. It has the ability to resurrect",
                    "lesser creatures."
                )
                stage++
            }

            74 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "These revived creatures now roam the phoenix's lair,",
                    "guarding it from interlopers during the phoenix's long",
                    "absences."
                )
                stage++
            }

            75 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "They usually attack anyone who enters, weeding out",
                    "the weaklings to ensure only the worthy reach the",
                    "phoenix in its roost, deep inside the lair."
                )
                stage++
            }

            76 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "I have had a peek inside, however, and the creatures",
                    "seem unusually passive at the moment."
                )
                stage++
            }

            77 -> {
                npc(FaceAnim.SAD, "Standing out here, I occasionally hear them cry out, as", "if in pain.")
                stage++
            }

            78 -> {
                player("Thanks for the information. Now, back to my other", "questions.")
                stage = 35
            }

            79 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Please, help the phoenix. Such a wondrous creature",
                    "should not be taken from this world in this way."
                )
                stage++
            }

            80 -> {
                player("I'll try my best. I'll be back soon.")
                setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 4, true)
                stage = END_DIALOGUE
            }

            81 -> {
                player(
                    FaceAnim.HALF_ASKING,
                    "I'm a little unsure on what I have to do next. Could I ask you some questions?"
                )
                stage = 35
            }

            100 -> {
                player("Yes, I have!")
                stage++
            }

            101 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "The phoenix is saved and you, " + player.username + ", are a hero",
                    "of nature! As an added bonus, I got to see it. My 50",
                    "years of waiting were not in vain after all!"
                )
                stage++
            }

            102 -> {
                player(
                    "Yes, I'm glad you watched all my hard work from a",
                    "comfortable vantage point outside of the lair."
                )
                stage++
            }

            103 -> {
                npc(
                    FaceAnim.SAD,
                    "Come now, Player. It wasn't like that. I'm a frail",
                    "old man! What possible good could I have done?"
                )
                stage++
            }

            104 -> {
                player("I suppose you're right.")
                stage++
            }

            105 -> {
                npc(FaceAnim.FRIENDLY, "Besides, the most important point is that the phoenix is", "saved!")
                stage++
            }

            106 -> {
                player(
                    "Oh, that reminds me – she also said to thank you for",
                    "the shrine, and asks when you plan to return her",
                    "trinkets."
                )
                stage++
            }

            107 -> {
                npc(FaceAnim.FRIENDLY, "She...she knew about me?")
                stage++
            }

            108 -> {
                npc(
                    FaceAnim.SAD,
                    "I should have known. Had I not been welcome in the",
                    "lair, I would have ended up like the rest of them. I was",
                    "a fool to think it was my skill that kept me alive."
                )
                stage++
            }

            109 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "Anyway, " + player.username + ", I wouldn't expect you to help for",
                    "mere thanks. Here is your reward!"
                )
                stage++
            }

            110 -> {
                end()
                finishQuest(player, Quests.IN_PYRE_NEED)
                updateQuestTab(player)
            }

            111 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "I'm a little unsure on what I have to do next. Could I ask you some questions?"
                )
                stage = 36
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRIEST_OF_GUTHIX_8555)
    }
}
