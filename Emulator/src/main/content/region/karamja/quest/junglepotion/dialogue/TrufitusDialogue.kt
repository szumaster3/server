package content.region.karamja.quest.junglepotion.dialogue

import content.region.karamja.quest.junglepotion.handlers.JungleObjective
import core.api.quest.finishQuest
import core.api.sendDialogueLines
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs.consts.NPCs
import org.rs.consts.Quests

class TrufitusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var objective: JungleObjective? = null

    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.JUNGLE_POTION)
        when (quest!!.getStage(player)) {
            0 -> npc("Greetings Bwana! I am Trufitus Shakaya of the Tai", "Bwo Wannai village.")
            10, 20, 30, 40, 50 -> {
                objective = JungleObjective.forStage(quest!!.getStage(player))
                npc("Hello, Bwana, do you have the " + objective!!.getName() + "?")
            }

            60 ->
                sendDialogueLines(
                    player,
                    "Trufitus shows you some techniques in Herblore. You gain some",
                    "experience in Herblore.",
                )

            100 ->
                if (!player.getAttribute("trufitus-post-jp", false)) {
                    npc("My greatest respect Bwana, I have communed with", "my gods and the future")
                    setAttribute(player, "/save:trufitus-post-jp", true)
                    stage = 0
                } else {
                    player("Hello Bwana!")
                    stage = 10
                }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            0 ->
                when (stage) {
                    0 -> {
                        npc("Welcome to our humble village.")
                        stage++
                    }

                    1 -> {
                        options(
                            "What does Bwana mean?",
                            "Tai Bwo Wannai? What does that mean?",
                            "It's a nice village, where is everyone?",
                        )
                        stage++
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("What does Bwana mean?")
                                stage = 10
                            }

                            2 -> {
                                player("Tai Bwo Wannai? What does that mean?")
                                stage = 20
                            }

                            3 -> {
                                player("It's a nice village, where is everyone?")
                                stage = 30
                            }
                        }

                    10 -> {
                        npc(
                            "Gracious sir, it means friend. And friends come in",
                            "peace. I assume that you come in peace?",
                        )
                        stage++
                    }

                    11 -> {
                        player("Yes, of course I do.")
                        stage++
                    }

                    12 -> {
                        npc("Well, that is good news, as I have a proposition for", "you.")
                        stage++
                    }

                    13 -> {
                        player("A proposition eh? Sounds interesting!")
                        stage++
                    }

                    14 -> {
                        npc("I hoped you would think so. My people are afraid to", "stay in the village.")
                        stage++
                    }

                    15 -> {
                        npc("They have returned to the jungle and I need to", "commune with the gods")
                        stage++
                    }

                    16 -> {
                        npc("to see what fate befalls us. You can help me by", "collecting some herbs that I need.")
                        stage++
                    }

                    17 -> {
                        player("Me? How Can I help?")
                        stage = 33
                    }

                    20 -> {
                        npc("it means 'small clearing in the jungle' but it is now the", "name of our village.")
                        stage++
                    }

                    21 -> {
                        player("It's a nice village, where is everyone?")
                        stage = 30
                    }

                    30 -> {
                        npc(
                            "My people are afraid to stay in the village. They have",
                            "returned to the jungle. I need to commune with the",
                            "gods to see what fate befalls us.",
                        )
                        stage++
                    }

                    31 -> {
                        npc("You may be able to help with this.")
                        stage++
                    }

                    32 -> {
                        player("Me? How Can I help?")
                        stage++
                    }

                    33 -> {
                        npc(
                            "I need to make a special brew! A potion that helps",
                            "to commune with the gods. For this potion, I need",
                            "special herbs, that are only found deep in the jungle.",
                        )
                        stage++
                    }

                    34 -> {
                        npc(
                            "I can only guide you so far as the herbs are not easy",
                            "to find. With some luck, you will find each herb in turn",
                            "and bring it to me. I will then give you details of where",
                            "to find the next herb.",
                        )
                        stage++
                    }

                    35 -> {
                        npc("In return for this great favour I will give you training", "in Herblore.")
                        stage++
                    }

                    36 -> {
                        player(
                            "It sounds like just the challenge for me. And it would",
                            "make a nice break from killing things!",
                        )
                        stage++
                    }

                    37 -> {
                        npc("That is excellent Bwana! The first herb that you need", "to gather is called")
                        stage++
                    }

                    38 -> {
                        npc("Snake Weed.")
                        stage++
                    }

                    39 -> {
                        npc("It grows near vines in an area to the south west where")
                        stage++
                    }

                    40 -> {
                        npc("the ground turns soft and the water kisses your feet.")
                        stage++
                    }

                    41 -> {
                        quest!!.start(player)
                        end()
                    }
                }

            10, 20, 30, 40, 50 ->
                when (stage) {
                    0 -> {
                        options("Of course!", "Not yet, sorry, what's the clue again?")
                        stage++
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player("Of course!")
                                stage = 10
                            }

                            2 -> {
                                player("Not yet, sorry, what's the clue again?")
                                stage = 20
                            }
                        }

                    10 ->
                        if (!player.inventory.containsItem(objective!!.herb.product)) {
                            npc("Please, don't try to deceive me.")
                            stage = 11
                        } else {
                            if (player.inventory.remove(objective!!.herb.product)) {
                                quest!!.setStage(player, quest!!.getStage(player) + 10)
                                interpreter.sendItemMessage(
                                    objective!!.herb.product,
                                    "You give the " + objective!!.getName() + " to Trufitus.",
                                )
                                stage = 50
                            }
                        }

                    11 -> {
                        npc("I really need that " + objective!!.getName() + " if I am to make this", "potion.")
                        stage++
                    }

                    12 -> end()
                    20 -> {
                        npc(*objective!!.clue)
                        stage = 11
                    }

                    50 -> {
                        objective = JungleObjective.forStage(quest!!.getStage(player))
                        when (quest!!.getStage(player)) {
                            20 -> {
                                npc(
                                    "Great, you have the Snake Weed! Many thanks. Ok,",
                                    "the next herb is called " + objective!!.getName() + ". It is related to the palm",
                                    "and crows to the east in its brother's shady profusion.",
                                )
                                stage = 200
                            }

                            30 -> {
                                npc("Great, you have the Ardrigal! Many thanks.")
                                stage = 300
                            }

                            40 -> {
                                npc("Well done Bwana, just two more herbs to collect.")
                                stage = 400
                            }

                            50 -> {
                                npc("Ah Volencia Moss, beautiful. One final herb and the", "potion will be complete.")
                                stage = 500
                            }
                        }
                    }

                    200 -> {
                        npc(
                            "To the east you will find a small peninsula, it is just",
                            "after the cliffs come down to meet the sands, here is",
                            "where you should search for it.",
                        )
                        stage++
                    }

                    201 -> end()
                    300 -> {
                        npc(
                            "You are doing well Bwana. The next herb is called Sito",
                            "Foil, and it grows best where the ground has been",
                            "blackened by the living flame.",
                        )
                        stage++
                    }

                    301 -> end()
                    400 -> {
                        npc(
                            "The next herb is called Volencia Moss. It clings to",
                            "rocks for its existence. It is difficult to see, so you",
                            "must search for it well.",
                        )
                        stage++
                    }

                    401 -> {
                        npc(
                            "It prefers rocks of high metal content and a frequently",
                            "disturbed environment. There is some, I believe to the",
                            "south east of this village.",
                        )
                        stage++
                    }

                    402 -> end()
                    500 -> {
                        npc(
                            "This is the most difficult to find as it is inhabits the",
                            "darkness of the underground. It is called Rogues",
                            "Purse, and is only to be found in caverns",
                        )
                        stage++
                    }

                    501 -> {
                        npc(
                            "in the northern part of this island. A secret entrance to",
                            "the caverns is set int the northern cliffs of this land.",
                            "Take care Bwana as it may be dangerous.",
                        )
                        stage++
                    }

                    502 -> end()
                }

            60 ->
                when (stage) {
                    50 -> {
                        npc(
                            "Most excellent Bwana! You have returned all the herbs",
                            "to me and, I can finish the preparations for the potion,",
                            "and at last divine with the gods.",
                        )
                        stage = 600
                    }

                    600 -> {
                        npc(
                            "Many blessings on you! I must now prepare, please",
                            "excuse me while I make the arrangements.",
                        )
                        stage = -1
                    }

                    -1 -> {
                        sendDialogueLines(
                            player,
                            "Trufitus shows you some techniques in Herblore. You gain some",
                            "experience in Herblore.",
                        )
                        stage++
                    }

                    0 -> {
                        finishQuest(player, Quests.JUNGLE_POTION)
                        end()
                    }
                }

            100 ->
                when (stage) {
                    0 -> {
                        npc("looks good for my people. We are happy now that the", "gods are not angry with us.")
                        stage++
                    }

                    1 -> {
                        npc("With some blessings we will be safe here.")
                        stage++
                    }

                    2 -> {
                        npc(
                            "You should deliver the good news to Bwana Timfraku,",
                            "Chief of Tai Bwo Wannai. He lives in a raised hut not",
                            "far from here.",
                        )
                        stage++
                    }

                    3 -> end()
                    10 -> {
                        npc(
                            "Greetings! I hope things are going well for you now.",
                            "I have no new information since we last spoke.",
                        )
                        stage++
                    }

                    11 -> {
                        npc(
                            "Needless to say, that if something does come up I",
                            "will certainly get in touch directly.",
                        )
                        stage = 3
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRUFITUS_740)
    }
}
