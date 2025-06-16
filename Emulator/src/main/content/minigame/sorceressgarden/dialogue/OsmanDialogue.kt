package content.minigame.sorceressgarden.dialogue

import content.data.GameAttributes
import content.minigame.sorceressgarden.plugin.SorceressGardenPlugin.SeasonDefinitions
import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class OsmanDialogue(player: Player? = null) : Dialogue(player) {

    private var quest: Quest? = null
    private var itemCount = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        quest = player.questRepository.getQuest(Quests.PRINCE_ALI_RESCUE)
        when (quest!!.getStage(player)) {
            100 -> {
                player("I'd like to talk about sq'irks.")
                stage = 0
                return true
            }

            60 -> {
                npc(
                    "The prince is safe on his way home with Leela.",
                    "You can pick up your payment from the chancellor.",
                )
                stage = 0
                return true
            }

            40, 50 -> {
                npc("Speak to Leela for any further instructions.")
                stage = 0
            }

            30 -> {
                player("Can you tell me what I still need to get?")
                stage = 0
            }

            20 -> {
                if (!inInventory(player, KEY_PRINT)) {
                    player("Can you tell me what I need to get?")
                } else if (!inInventory(player, BRONZE_BAR) && inInventory(player, KEY_PRINT)) {
                    npc("Good, you have the print of the key. Get a bar of", "bronze, too, and I can get the key mad.")
                    stage = 70
                } else {
                    npc("Well done; we can make the key now.")
                    stage = 80
                }
                return true
            }

            10 -> player("The chancellor trusts me. I have come for instructions.")
            0 -> player("I'd like to talk about sq'irks.")
            else -> {
            }
        }
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            100 -> when (stage) {
                else -> handleSqirks(buttonId)
            }

            60 -> end()
            40, 50 -> end()
            30 -> when (stage) {
                82 -> end()
                0 -> {
                    npc("You can collect the key from Leela.")
                    stage = 11
                }

                11 -> {
                    if (inInventory(player, YELLOW_WIG)) {
                        npc("The wig you have got, well done.")
                        itemCount++
                    } else {
                        npc(
                            "You need a wig, maybe made from wool. If you find",
                            "someone who can work with wool ask them about it.",
                            "There's a witch nearby may be able to help you dye it.",
                        )
                    }
                    stage = 12
                }

                12 -> {
                    if (inInventory(player, SKIRT)) {
                        npc("You have got the skirt, good.")
                        itemCount++
                    } else {
                        npc("You will need to get a pink skirt, same as Keli's.")
                    }
                    stage = 13
                }

                13 -> {
                    if (inInventory(player, PASTE)) {
                        npc("You have the skin paint, well done. I thought you would", "struggle to make that.")
                        itemCount++
                    } else {
                        npc(
                            "We still need something to colour the Prince's skin",
                            "lighter. There's a witch close to here. She knows about",
                            "many things. She may know some way to make the",
                            "skin lighter.",
                        )
                    }
                    stage = if (itemCount == 3) 14 else 15
                }

                14 -> {
                    npc("You do have everything for the disguise.")
                    stage = 15
                }

                15 -> if (inInventory(player, ROPE)) {
                    npc(
                        "You have the rope I see, to tie up Keli. That will be the",
                        "most dangerous part of the plan.",
                    )
                    stage = 16
                } else {
                    npc(
                        "You will still need some rope to tie up Keli, of course. I",
                        "heard that there's a good rope maker around here.",
                    )
                    stage = 16
                }

                16 -> end()
                20 -> {
                    npc(
                        "Yes, that is most important. There is no way you can",
                        "get the real key. It is on a chain around Keli's neck.",
                        "Almost impossible to steal.",
                    )
                    stage = 21
                }

                21 -> end()
            }

            0 -> when (stage) {
                else -> handleSqirks(buttonId)
            }

            20 -> when (stage) {
                80 -> {
                    interpreter.sendDialogue("Osman takes the key imprint and the bronze bar.")
                    stage = 81
                }

                81 -> if (removeItem(player, BRONZE_BAR) && removeItem(player, KEY_PRINT)) {
                    npc("Pick the key up from Leela.")
                    quest!!.setStage(player, 30)
                    stage = 82
                }

                82 -> end()
                70 -> {
                    player("I will get one and come back.")
                    stage = 71
                }

                71 -> end()
                0 -> {
                    npc("A print of the key in soft clay and a bronze bar.", "Then, collect the key from Leela.")
                    stage = 1
                }

                1 -> {
                    npc("When you have the full disguise talk to", "Leela and she will help you with the rest.")
                    stage = 2
                }

                2 -> end()
                10 -> {
                    npc(
                        "The prince is guarded by some stupid guards and a",
                        "clever woman. The woman is our only way to get the",
                        "prince out. Only she can walk freely about the area.",
                    )
                    stage = 11
                }

                11 -> {
                    npc(
                        "I think you will need to tie her up. One coil of rope",
                        "should do for that. Then, disguise the prince as her to",
                        "get him out without suspicion.",
                    )
                    stage = 12
                }

                12 -> {
                    player("How good must the disguise be?")
                    stage = 13
                }

                13 -> {
                    npc(
                        "Only enough to fool the guards at a distance. Get a",
                        "skirt like hers. Same colour, same style. We will only",
                        "have a short time.",
                    )
                    stage = 14
                }

                14 -> {
                    npc(
                        "Get a blonde wig, too. that is up to you to make or",
                        "find. Something to colour the skin of the prince.",
                    )
                    stage = 15
                }

                15 -> {
                    npc(
                        "My daughter and top spy, Leela, can help you. She has",
                        "sent word that she has discovered where they are",
                        "keeping the prince.",
                    )
                    stage = 16
                }

                16 -> {
                    npc("It's near Draynor Village. She is lurking somewhere", "near there now.")
                    stage = 17
                }

                17 -> {
                    quest!!.setStage(player, 20)
                    options(
                        "Explain the first thing again.",
                        "What is the second thing you need?",
                        "Okay, I better go find some things.",
                    )
                    stage = 50
                }

                20 -> {
                    npc(
                        "We need the key, or we need a copy made. If you can",
                        "get some soft clay then you can copy the key...",
                    )
                    stage = 21
                }

                21 -> {
                    npc(
                        "...If you can convince Lady Keli to show it to you",
                        "for a moment. She is very boastful.",
                        "It should not be too hard.",
                    )
                    stage = 22
                }

                22 -> {
                    npc("Bring the imprint to me, with a bar of bronze.")
                    stage = 23
                }

                23 -> {
                    quest!!.setStage(player, 20)
                    options(
                        "What is the first thing I must do?",
                        "What exactly is the second thing you need?",
                        "Okay, I better go find some things.",
                    )
                    stage = 24
                }

                24 -> when (buttonId) {
                    1 -> {
                        player("What is the first thing I must do?")
                        stage = 10
                    }

                    2 -> {
                        player("What exactly is the second thing I must do?")
                        stage = 20
                    }

                    3 -> {
                        player("Okay, I better go find some things.")
                        stage = 25
                    }
                }

                25 -> {
                    npc(
                        "May good luck travel with you. Don't forget to find",
                        "Leela. It can't be done without her help.",
                    )
                    stage = 26
                }

                26 -> end()
                50 -> when (buttonId) {
                    1 -> {
                        player("Explain the first thing again.")
                        stage = 10
                    }

                    2 -> {
                        player("What is th second thing you need?")
                        stage = 20
                    }

                    3 -> {
                        player("Okay, I better go find some things.")
                        stage = 25
                    }
                }
            }

            10 -> when (stage) {
                0 -> {
                    npc(
                        "Our prince is captive by the Lady Keli. We just need",
                        "to make the rescue. There are two things we need",
                        "you to do.",
                    )
                    stage = 1
                }

                1 -> {
                    options("What is the first thing I must do?", "What is the second thing you need?")
                    stage = 3
                }

                3 -> when (buttonId) {
                    1 -> {
                        player("What is the first thing I must do?")
                        stage = 10
                    }

                    2 -> {
                        player("What is the second thing you need?")
                        stage = 20
                    }
                }

                10 -> {
                    npc(
                        "The prince is guarded by some stupid guards and a",
                        "clever woman. The woman is our only way to get the",
                        "prince out. Only she can walk freely about the area.",
                    )
                    stage = 11
                }

                11 -> {
                    npc(
                        "I think you will need to tie her up. One coil of rope",
                        "should do for that. Then, disguise the prince as her to",
                        "get him out without suspicion.",
                    )
                    stage = 12
                }

                12 -> {
                    player("How good must the disguise be?")
                    stage = 13
                }

                13 -> {
                    npc(
                        "Only enough to fool the guards at a distance. Get a",
                        "skirt like hers. Same colour, same style. We will only",
                        "have a short time.",
                    )
                    stage = 14
                }

                14 -> {
                    npc(
                        "Get a blonde wig, too. that is up to you to make or",
                        "find. Something to colour the skin of the prince.",
                    )
                    stage = 15
                }

                15 -> {
                    npc(
                        "My daughter and top spy, Leela, can help you. She has",
                        "sent word that she has discovered where they are",
                        "keeping the prince.",
                    )
                    stage = 16
                }

                16 -> {
                    npc("It's near Draynor Village. She is lurking somewhere", "near there now.")
                    stage = 17
                }

                17 -> {
                    quest!!.setStage(player, 20)
                    options(
                        "Explain the first thing again.",
                        "What is the second thing you need?",
                        "Okay, I better go find some things.",
                    )
                    stage = 50
                }

                20 -> {
                    npc(
                        "We need the key, or we need a copy made. If you can",
                        "get some soft clay then you can copy the key...",
                    )
                    stage = 21
                }

                21 -> {
                    npc(
                        "...If you can convince Lady Keli to show it to you",
                        "for a moment. She is very boastful.",
                        "It should not be too hard.",
                    )
                    stage = 22
                }

                22 -> {
                    npc("Bring the imprint to me, with a bar of bronze.")
                    stage = 23
                }

                23 -> {
                    quest!!.setStage(player, 20)
                    options(
                        "What is the first thing I must do?",
                        "What exactly is the second thing you need?",
                        "Okay, I better go find some things.",
                    )
                    stage = 24
                }

                24 -> when (buttonId) {
                    1 -> {
                        player("What is the first thing I must do?")
                        stage = 10
                    }

                    2 -> {
                        player("What exactly is the second thing I must do?")
                        stage = 20
                    }

                    3 -> {
                        player("Okay, I better go find some things.")
                        stage = 25
                    }
                }

                25 -> {
                    npc(
                        "May good luck travel with you. Don't forget to find",
                        "Leela. It can't be done without her help.",
                    )
                    stage = 26
                }

                26 -> end()
            }

            else -> {
            }
        }
        return true
    }

    fun handleSqirks(buttonId: Int) {
        when (stage) {
            0 -> {
                setAttribute(player, GameAttributes.TALK_ABOUT_SQ_IRKJUICE, true)
                if (!hasSqirks()) {
                    options(
                        "Where do I get sq'irks?",
                        "Why can't you get the sq'irks yourself?",
                        "How should I squeeze the fruit?",
                        "Is there a reward for getting these sq'irks?",
                        "What's so good about sq'irk juice then?",
                    )
                    stage = 200
                } else {
                    player(
                        if (hasSqirkJuice()) "I have some sq'riks juice for you." else "I have some sq'irks for you.",
                    )
                    stage = 300
                }
            }

            300 -> if (hasSqirkJuice()) {
                rewardXP(player, Skills.THIEVING, experience)
                sendDialogueLines(
                    player,
                    "Osman imparts some Thieving advice to",
                    "you ( $experience Thieving experience points )",
                    "as a reward for the sq'irk juice.",
                )
                stage = 304
            } else {
                npc("Uh, thanks, but is there any chance that you", "could squeeze the fruit into a glass for me?")
                stage = 301
            }

            301 -> {
                options("How should I squeeze the fruit?", "Can't you do that yourself?")
                stage = 302
            }

            302 -> when (buttonId) {
                1 -> {
                    player("How should I squeeze the fruit?")
                    stage = 130
                }

                2 -> {
                    player("Can't you do that yourself?")
                    stage = 303
                }
            }

            304 -> end()
            303 -> {
                player("I only carry knives or other such devices on me", "when I'm on the job.")
                stage = 119
            }

            200 -> when (buttonId) {
                1 -> {
                    player("Where do I get sq'irks?")
                    stage = 110
                }

                2 -> {
                    player("Why can't you get the sq'irks yourself?")
                    stage = 120
                }

                3 -> {
                    player("How should I squeeze the fruit?")
                    stage = 130
                }

                4 -> {
                    player("Is there a reward for getting these sq'irks?")
                    stage = 140
                }

                5 -> {
                    player("What's so good about sq'irk juice then?")
                    stage = 150
                }
            }

            110 -> {
                npc(
                    "There is a sorceress near the south-eastern edge of Al",
                    "Kharid who grows them. Once upon a time, we",
                    "considered each other friends.",
                )
                stage = 111
            }

            111 -> {
                player("What happened?")
                stage = 112
            }

            112 -> {
                npc("We fell out, and now she won't give me any more", "fruit.")
                stage = 113
            }

            113 -> {
                player("So all I have to do is ask her for some fruit for you?")
                stage = 114
            }

            114 -> {
                npc(
                    "I doubt it will be that easy. She is not renowned for",
                    "her generosity and is very secretive about her garden's",
                    "location.",
                )
                stage = 115
            }

            115 -> {
                player("Oh come on, it should be easy enough to find.")
                stage = 116
            }

            116 -> {
                npc(
                    "Her garden has remained hidden even to me - the chief",
                    "spy of Al Kharid. I believe her garden must be hidden",
                    "by magical means.",
                )
                stage = 117
            }

            117 -> {
                player("This should be an interesting task. How many sq'irks do", "you want?")
                stage = 118
            }

            118 -> {
                npc(
                    "I'll reward you for as many as you can get your",
                    "hands on, but could you please squeeze the fruit into a",
                    "glass first?",
                )
                stage = 119
            }

            119 -> {
                options("I've another question about sq'irks.", "Thanks for the information.")
                stage = 98
            }

            98 -> when (buttonId) {
                1 -> {
                    options(
                        "Where do I get sq'irks?",
                        "Why can't you get the sq'irks yourself?",
                        "How should I squeeze the fruit?",
                        "Is there a reward for getting these sq'irks?",
                        "What's so good about sq'irk juice then?",
                    )
                    stage = 200
                }

                2 -> {
                    player("Thanks for the information.")
                    stage = 99
                }
            }

            99 -> end()
            120 -> {
                npc(
                    "I may have mentioned that I had a falling out with the",
                    "Sorceress. Well, unsurprisingly, she refuses to give me",
                    "any more of her garden's produce.",
                )
                stage = 119
            }

            130 -> {
                npc(
                    "Use a pestle and mortar to squeeze the sr'irks. Make",
                    "sure you have an empty glass with you to collect the",
                    "juice.",
                )
                stage = 119
            }

            140 -> {
                npc("Of course there is. I am a generous man. I'll teach", "you the art of Thieving for your troubles.")
                stage = 141
            }

            141 -> {
                player("How much training will you give?")
                stage = 142
            }

            142 -> {
                npc("That depends on the quantity and ripeness of the", "sq'irks you put into the juice.")
                stage = 143
            }

            143 -> {
                player("That sounds fair enough.")
                stage = 119
            }

            150 -> {
                npc(
                    "Ah it's sweet, sweet nectar for a thief or spy; it makes",
                    "light fingers lighter, fleet feet flightier and comes in four",
                    "different colours for those who are easily amused.",
                )
                stage = 151
            }

            151 -> {
                sendDialogue(player, "Osman starts salivating at the thought of sq'irk juice.")
                stage = 152
            }

            152 -> {
                player("It wouldn't have addictive properties, would it?")
                stage = 153
            }

            153 -> {
                npc("It only holds power over those with poor self-control,", "something which I have an abundance of.")
                stage = 154
            }

            154 -> {
                player("I see.")
                stage = 119
            }
        }
    }

    val experience: Double
        get() {
            var total = 0.0
            for (juiceId in JUICES) {
                val def = SeasonDefinitions.forJuiceId(juiceId) ?: continue
                val amount: Int = player.inventory.getAmount(Item(juiceId))
                total += amount * def.osmanExp
                player.inventory.remove(Item(juiceId, amount))
            }
            player.inventory.refresh()
            return total
        }

    private fun hasSqirkFruit(): Boolean {
        for (i in FRUITS) {
            if (inInventory(player, i, 1)) {
                return true
            }
        }
        return false
    }

    private fun hasSqirkJuice(): Boolean {
        for (i in JUICES) {
            if (inInventory(player, i, 1)) {
                return true
            }
        }
        return false
    }

    private fun hasSqirks(): Boolean = hasSqirkFruit() || hasSqirkJuice()

    override fun getIds(): IntArray = intArrayOf(NPCs.OSMAN_924, NPCs.MAISA_5282)

    companion object {
        private const val KEY_PRINT = Items.KEY_PRINT_2423
        private const val BRONZE_BAR = Items.BRONZE_BAR_2349
        private const val ROPE = Items.ROPE_954
        private const val SKIRT = Items.PINK_SKIRT_1013
        private const val YELLOW_WIG = Items.WIG_2419
        private const val PASTE = Items.PASTE_2424
        private val JUICES = intArrayOf(
            Items.SPRING_SQIRKJUICE_10848,
            Items.SUMMER_SQIRKJUICE_10849,
            Items.AUTUMN_SQIRKJUICE_10850,
            Items.WINTER_SQIRKJUICE_10851,
        )
        private val FRUITS = intArrayOf(
            Items.SPRING_SQIRK_10844,
            Items.SUMMER_SQIRK_10845,
            Items.AUTUMN_SQIRK_10846,
            Items.WINTER_SQIRK_10847,
        )
    }
}
