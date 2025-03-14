package content.region.misthalin.quest.phoenixgang.dialogue

import content.region.misthalin.quest.phoenixgang.ShieldofArrav
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ReldoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var knightSword: Quest? = null
    private var shieldArrav: Quest? = null
    private var isDiary = false
    private val level = 1

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        knightSword = player.getQuestRepository().getQuest(Quests.THE_KNIGHTS_SWORD)
        shieldArrav = player.getQuestRepository().getQuest(Quests.SHIELD_OF_ARRAV)
        if (args.size == 2 && args[1] == "book") {
            player("Aha! 'The Shield of Arrav'! Exactly what I was looking", "for.")
            stage = 3
            return true
        }
        if (getQuestStage(player, Quests.THE_LOST_TRIBE) == 40 && player.inventory.contains(Items.BROOCH_5008, 1)
        ) {
            options("Hello stranger.", "I have a question about my Achievement Diary.", "Ask about the brooch.")
        } else {
            options("Hello stranger.", "I have a question about my Achievement Diary.")
        }
        stage = -1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (isDiary) {
            when (stage) {
                999 -> end()
                -1 -> {
                    options(
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later.",
                    )
                    stage++
                }

                0 ->
                    when (buttonId) {
                        1 -> {
                            player("What is the Achievement Diary?")
                            stage = 410
                        }

                        2 -> {
                            player("What are the rewards?")
                            stage = 420
                        }

                        3 -> {
                            player("How do I claim the rewards?")
                            stage = 430
                        }

                        4 -> {
                            player("See you later.")
                            stage = 999
                        }
                    }

                440 -> {
                    npc("Really? How extraordinary! Well, that certainly is a ", "worthy achievement indeed.")
                    stage++
                }

                441 -> {
                    player("Thanks. Was there a reward?")
                    stage++
                }

                442 -> {
                    npc("A reward? Well, yes, I suppose I can help you there.")
                    stage = 444
                }

                444 -> {
                    Diary.flagRewarded(player, DiaryType.VARROCK, level)
                    sendDialogue(
                        "Reldo takes the Varrock armour and attaches some more plate metal",
                        "to it, filling it out to look like a proper suit of armour. He etches",
                        "some words into the armour, which glows slightly before fading.",
                    )
                    stage++
                }

                445 -> {
                    npc(
                        "I have enhanced your body armour with a spell so that,",
                        "when using the Edgeville furnace, you'll have the chance",
                        "of smelting an extra bar up to mithril. It will also give",
                        "you the chance of an extra ore when Mining up to",
                    )
                    stage++
                }

                446 -> {
                    npc("mithril as well.")
                    stage++
                }

                447 -> {
                    npc(
                        "Smithing can take time, so I have placed a small speed",
                        "enhancement on the armour. When you smith with this armour on, there is a chance that you will be able to",
                        "smith faster each time and thus decrease the time it",
                    )
                    stage++
                }

                448 -> {
                    npc("takes to produce forged items.")
                    stage++
                }

                449 -> {
                    npc(
                        "I can also change your Varrock Teleport spell so that it",
                        "takes you to the Grand Exchange, if you'd find that more convenient.",
                    )
                    stage++
                }

                450 -> {
                    npc("As an extra reward I have also given you a magical lamp to help with your skills.")
                    stage++
                }

                451 -> {
                    player("Wow, thanks! What if I lose the armour?")
                    stage++
                }

                452 -> {
                    npc("Oh, that's not a problem. You can come back to see me", "and I'll get you another set.")
                    stage = -1
                }

                460 -> {
                    Diary.grantReplacement(player, DiaryType.VARROCK, level)
                    npc("You better be more careful this time.")
                    stage = 999
                }

                410 -> {
                    npc(
                        "It's a diary that helps you keep track of particular",
                        "achievements. Here in Varrock it can help you",
                        "discover some quite useful things. Eventually, with",
                        "enough exploration, the people of Varrock will reward",
                    )
                    stage++
                }

                411 -> {
                    npc("you.")
                    stage++
                }

                412 -> {
                    npc("You can see what tasks you have listed by clicking on", "the green button in the Quest List.")
                    stage = 999
                }

                420 -> {
                    npc(
                        "Well, there's three different levels of Varrock Armour,",
                        "which match up with the three levels of difficulty. Each",
                        "has the same rewards as the previous level, and an",
                        "additional one too... but I won't spoil your surprise.",
                    )
                    stage++
                }

                421 -> {
                    npc("Rest assured, the people of Varrock are happy to see", "you visiting the land.")
                    stage = 999
                }

                430 -> {
                    npc(
                        "Just complete the tasks so they're all ticked off, then",
                        "you can claim your reward. Most of them are",
                        "straightforward; you might find some require quests to",
                        "be started, if not finished.",
                    )
                    stage++
                }

                431 -> {
                    npc("To claim the different Varrock Armour, speak to Vannaka", "Rat Burgis, and myself.")
                    stage = 999
                }
            }
            return true
        }
        if (stage == -1) {
            when (buttonId) {
                1 -> {
                    npc("Hello stranger.")
                    stage = 0
                }

                2 -> {
                    player("I have a question about my Achievement Diary.")
                    stage = 900
                }

                3 -> {
                    player("What can you tell me about this brooch?")
                    stage = 2000
                }
            }
            return true
        }
        if (stage == 900) {
            sendDiaryDialogue()
            return true
        }

        if (stage >= 2000) {
            when (stage) {
                2000 -> {
                    npc("I've never seen that symbol before. Where did you find", "it?")
                    stage++
                }

                2001 -> {
                    player("In a cave beneath Lumbridge.")
                    stage++
                }

                2002 -> {
                    npc("Very odd. Have you any idea how it got there?")
                    stage++
                }

                2003 -> {
                    player("A goblin might have dropped it.")
                    stage++
                }

                2004 -> {
                    npc("I've never heard of a goblin carrying a brooch like this.", "But just a minute...")
                    stage++
                }

                2005 -> {
                    npc(
                        "The other day I filed a book about ancient goblin tribes.",
                        "It's somewhere on the west end of the library, I think.",
                        "Maybe that will be of some use.",
                    )
                    player.getQuestRepository().getQuest(Quests.THE_LOST_TRIBE).setStage(player, 42)
                    stage++
                }

                2006 -> end()
            }
            return true
        }
        if (knightSword!!.getStage(player) == 10) {
            when (stage) {
                else -> {
                    handleKnightSword(buttonId)
                    handleQuest(buttonId)
                }
            }
            return true
        }
        when (shieldArrav!!.getStage(player)) {
            20 ->
                when (stage) {
                    0 -> {
                        player("Ok. I've read the book. Do you know where I can find", "the Phoenix Gang?")
                        stage = 1
                    }

                    1 -> {
                        npc("No, I don't. I think I know someone who might", "however.")
                        stage = 2
                    }

                    2 -> {
                        npc(
                            "If I were you I would talk to Baraeck, the fur trader in",
                            "the market place. I've heard he has connections with the",
                            "Phoenix Gang.",
                        )
                        stage = 3
                    }

                    3 -> {
                        player("Thanks, I'll try that!")
                        stage = 4
                    }

                    4 -> {
                        shieldArrav!!.setStage(player, 30)
                        end()
                    }
                }

            10 ->
                when (stage) {
                    0 -> {
                        player("About that book... where is it again?")
                        stage = 1
                    }

                    1 -> {
                        npc("I'm not sure where it is exactly... but I'm sure it's", "around here somewhere.")
                        stage = 2
                    }

                    2 -> end()
                    14 -> end()
                    3 -> {
                        interpreter.sendDialogue("You take the book from the bookcase.")
                        stage = 4
                    }

                    4 -> {
                        if (!player.inventory.add(ShieldofArrav.BOOK)) {
                            GroundItemManager.create(ShieldofArrav.BOOK, player)
                        }
                        end()
                    }
                }

            0 ->
                when (stage) {
                    0 -> {
                        options("I'm in search  of a quest.", "Do you have anything to trade?", "What do you do?")
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player("I'm in search of a quest.")
                                stage = 10
                            }

                            2 -> {
                                player("Do you have anything to trade?")
                                stage = 20
                            }

                            3 -> {
                                player("What do you do?")
                                stage = 30
                            }
                        }

                    30 -> {
                        npc("I am the palace librarian.")
                        stage = 31
                    }

                    31 -> {
                        player("Ah. That's why you're in the library then.")
                        stage = 32
                    }

                    32 -> {
                        npc("Yes.")
                        stage = 33
                    }

                    33 -> end()
                    20 -> {
                        npc("Only knowledge.")
                        stage = 21
                    }

                    21 -> {
                        player("How much do you want for that then?")
                        stage = 22
                    }

                    22 -> {
                        npc("No, sorry, that was just my little joke. I'm not the", "trading type.")
                        stage = 23
                    }

                    23 -> {
                        player("Ah well.")
                        stage = 24
                    }

                    24 -> end()
                    else -> handleQuest(buttonId)
                }

            else ->
                when (stage) {
                    0 -> {
                        options("Do you have anything to trade?", "What do you do?")
                        stage = 1
                    }

                    else -> regular(buttonId)
                }
        }
        return true
    }

    fun handleQuest(buttonId: Int) {
        when (stage) {
            10 -> {
                npc("Hmmm. I don't... believe there are any here...")
                stage = 11
            }

            11 -> {
                npc("Let me think actually...")
                stage = 12
            }

            12 -> {
                npc(
                    "Ah yes. I know. If you look in a book called 'The Shield",
                    "of Arrav', you'll find a quest in there.",
                )
                stage = 13
            }

            13 -> {
                shieldArrav!!.start(player)
                npc("I'm not not sure where the book is mind you... but I'm", "sure it's around here somewhere.")
                stage = 14
            }

            14 -> {
                player("Thank you.")
                stage = 15
            }

            15 -> end()
        }
    }

    fun regular(buttonId: Int) {
        when (stage) {
            1 ->
                when (buttonId) {
                    1 -> {
                        player("Do you have anything to trade?")
                        stage = 20
                    }

                    2 -> {
                        player("What do you do?")
                        stage = 30
                    }
                }

            30 -> {
                npc("I am the palace librarian.")
                stage = 31
            }

            31 -> {
                player("Ah. That's why you're in the library then.")
                stage = 32
            }

            32 -> {
                npc("Yes.")
                stage = 33
            }

            33 -> end()
            20 -> {
                npc("Only knowledge.")
                stage = 21
            }

            21 -> {
                player("How much do you want for that then?")
                stage = 22
            }

            22 -> {
                npc("No, sorry, that was just my little joke. I'm not the", "trading type.")
                stage = 23
            }

            23 -> {
                player("Ah well.")
                stage = 24
            }

            24 -> end()
        }
    }

    fun handleKnightSword(buttonId: Int) {
        when (stage) {
            0 -> {
                options("Do you have anything to trade?", "What do you do?", "What do you know about Imcando dwarves?")
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Do you have anything to trade?")
                        stage = 20
                    }

                    2 -> {
                        player("What do you do?")
                        stage = 30
                    }

                    3 -> {
                        player("What do you know about Imcando dwarves?")
                        stage = 40
                    }
                }

            30 -> {
                npc("I am the palace librarian.")
                stage = 31
            }

            31 -> {
                player("Ah. That's why you're in the library then.")
                stage = 32
            }

            32 -> {
                npc("Yes.")
                stage = 33
            }

            33 -> end()
            20 -> {
                npc("Only knowledge.")
                stage = 21
            }

            21 -> {
                player("How much do you want for that then?")
                stage = 22
            }

            22 -> {
                npc("No, sorry, that was just my little joke. I'm not the", "trading type.")
                stage = 23
            }

            23 -> {
                player("Ah well.")
                stage = 24
            }

            24 -> end()
            40 -> {
                npc("The Imcando dwarves, you say?")
                stage = 41
            }

            41 -> {
                npc(
                    "Ah yes... for many hundreds of years they were the",
                    "world's most skilled smiths. They used secret smithing",
                    "knowledge passed down from generation to generation.",
                )
                stage = 42
            }

            42 -> {
                npc(
                    "Unfortunately, about a century ago, the once thriving",
                    "race was wiped out during the barbarian invasions of",
                    "that time.",
                )
                stage = 43
            }

            43 -> {
                player("So are there any Imcando left at all?")
                stage = 44
            }

            44 -> {
                npc(
                    "I believe a few of them survived, but with the bulk of",
                    "their population destroyed their numbers have dwindled",
                    "even further.",
                )
                stage = 45
            }

            45 -> {
                npc(
                    "I believe I remember a couple living in Asgarnia near",
                    "the cliffs on the Asgarnian southern peninsula, but they",
                    "DO tend to keep to themselves.",
                )
                stage = 46
            }

            46 -> {
                npc(
                    "They tend not to tell people that they're the",
                    "descendants of the Imcando, which is why people think",
                    "that the tribe has died out totally, but you may well",
                    "have more luck talking to them if you bring them some",
                )
                stage = 47
            }

            47 -> {
                npc("redberry pie. They REALLY like redberry pie.")
                stage = 48
            }

            48 -> {
                knightSword!!.setStage(player, 20)
                end()
            }
        }
    }

    private fun sendDiaryDialogue() {
        isDiary = true
        if (Diary.canClaimLevelRewards(player, DiaryType.VARROCK, level)) {
            player("I've finished all the medium tasks in my Varrock", "Achievement Diary.")
            stage = 440
            return
        }
        if (Diary.canReplaceReward(player, DiaryType.VARROCK, level)) {
            player("I've seemed to have lost my armour...")
            stage = 460
            return
        }
        options(
            "What is the Achievement Diary?",
            "What are the rewards?",
            "How do I claim the rewards?",
            "See you later.",
        )
        stage = 0
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RELDO_2660, NPCs.RELDO_2661)
    }
}
