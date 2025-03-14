package content.global.skill.slayer

import content.global.skill.slayer.SlayerManager.Companion.getInstance
import content.global.skill.slayer.SlayerMaster.Companion.forId
import content.global.skill.slayer.SlayerMaster.Companion.hasSameTask
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SlayerMasterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var master: SlayerMaster? = null
    private var quest: Quest? = null
    private var isDiary = false
    private val level = 2

    override fun open(vararg args: Any): Boolean {
        if (args[0] is NPC) {
            npc = args[0] as NPC
        }
        master = forId(if (args[0] is NPC) (args[0] as NPC).id else args[0] as Int)
        quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)

        if (master == SlayerMaster.DURADEL) {
            if (isMaster(player, Skills.SLAYER)) {
                options("Ask about Skillcape", "Something else")
                stage = 900
                return true
            }
        } else {
            interpreter.sendDialogues(master!!.npc, getExpression(master), "'Ello, and what are you after, then?")
            stage =
                if (master == SlayerMaster.VANNAKA) {
                    -1
                } else {
                    0
                }
        }
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
                    npc(
                        getExpression(master),
                        "I can imagine! I expect you'll be wanting a reward for",
                        "your hard efforts, eh?",
                    )
                    stage++
                }

                441 -> {
                    player("Yes, please.")
                    stage++
                }

                442 -> {
                    Diary.grantReplacement(player, DiaryType.VARROCK, level)
                    npc(
                        getExpression(master),
                        "I'm happy to say that you've done well, so I will reward",
                        "you suitably for your work.",
                    )
                    stage++
                }

                443 -> {
                    player("Great! Thanks.")
                    stage++
                }

                444 -> {
                    sendDialogue(
                        "Vannaka takes the Varrock armour and carves some symbols into it.",
                        "He waves his hands over the symbols and the armour appears to glow",
                        "brilliantly.",
                    )
                    stage++
                }

                450 -> {
                    Diary.grantReplacement(player, DiaryType.VARROCK, level)
                    npc(getExpression(master), "You better be more careful this time.")
                    stage = -1
                }

                410 -> {
                    npc(
                        getExpression(master),
                        "It's a diary that helps you keep track of particular",
                        "achievements. Here in Varrock it can help you",
                        "discover some quite useful things. Eventually, with",
                        "enough exploration, the people of Varrock will reward",
                    )
                    stage++
                }

                411 -> {
                    npc(getExpression(master), "you.")
                    stage++
                }

                412 -> {
                    npc(
                        getExpression(master),
                        "You can see what tasks you have listed by clicking on",
                        "the green button in the Quest List.",
                    )
                    stage = -1
                }

                420 -> {
                    npc(
                        getExpression(master),
                        "Well, there's three different levels of Varrock Armour,",
                        "which match up with the three levels of difficulty. Each",
                        "has the same rewards as the previous level, and an",
                        "additional one too... but I won't spoil your surprise.",
                    )
                    stage++
                }

                421 -> {
                    npc(
                        getExpression(master),
                        "Rest assured, the people of Varrock are happy to see",
                        "you visiting the land.",
                    )
                    stage = -1
                }

                430 -> {
                    npc(
                        getExpression(master),
                        "Just complete the tasks so they're all ticked off, then",
                        "you can claim your reward. Most of them are",
                        "straightforward; you might find some require quests to",
                        "be started, if not finished.",
                    )
                    stage++
                }

                431 -> {
                    npc(
                        getExpression(master),
                        "To claim the different Varrock Armour, speak to Vannaka",
                        "Rat Burgis, and myself.",
                    )
                    stage = -1
                }

                50 -> end()
            }
            return true
        }
        when (stage) {
            999 -> end()
            -1 ->
                if (!getInstance(player).hasStarted()) {
                    options(
                        "Who are you?",
                        "Do you have anything for trade?",
                        "Er...nothing...",
                        "I have a question about my Achievement Diary.",
                    )
                    stage = 1
                } else {
                    options(
                        "I need another assignment.",
                        "Do you have anything for trade?",
                        "Er...nothing...",
                        "I have a question about my Achievement Diary.",
                    )
                    stage = 690
                }

            0 -> {
                if (master == SlayerMaster.TURAEL) {
                    if (quest!!.getStage(player) == 30) {
                        options(
                            "I need another assignment.",
                            "Do you have anything for trade?",
                            "Er...nothing...",
                            "I'm here about a quest.",
                        )
                        stage = 700
                    } else if (quest!!.getStage(player) == 31) {
                        options(
                            "I need another assignment.",
                            "Do you have anything for trade?",
                            "Er...nothing...",
                            "Hello, I'm here about those trees again.",
                        )
                        stage = 700
                    }
                }
                if (!getInstance(player).hasStarted()) {
                    options("Who are you?", "Do you have anything for trade?", "Er...nothing...")
                    stage = 1
                } else {
                    options("I need another assignment.", "Do you have anything for trade?", "Er...nothing...")
                    stage = 700
                }
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Who are you?")
                        stage = 10
                    }

                    2 -> {
                        player("Do you have anything for trade?")
                        stage = 20
                    }

                    3 -> {
                        player("Er...nothing...")
                        stage = 999
                    }

                    4 -> {
                        player("I have a question about my Achievement Diary.")
                        stage = 691
                    }
                }

            10 -> {
                interpreter.sendDialogues(
                    master!!.npc,
                    getExpression(master),
                    "I'm " +
                        (
                            if (master!!.npc ==
                                8273
                            ) {
                                "the lowest level Slayer Master available."
                            } else {
                                "one of the elite Slayer Masters."
                            }
                        ),
                    if (master!!.npc ==
                        8273
                    ) {
                        "The other Slayer Masters are spread around the world."
                    } else {
                        "I can teach you about the ways of the Slayer."
                    },
                )
                stage = 11
            }

            11 -> {
                options("What's a Slayer?", "Never heard of you...")
                stage = 12
            }

            12 ->
                when (buttonId) {
                    1 -> {
                        player("What's a Slayer?")
                        stage = 100
                    }

                    2 -> {
                        player("Never heard of you...")
                        stage = 2000
                    }
                }

            20 -> {
                interpreter.sendDialogues(
                    master!!.npc,
                    getExpression(master),
                    "I have a wide selection of Slayer equipment; take a look!",
                )
                stage = 21
            }

            21 -> {
                end()
                if (npc != null) {
                    npc.openShop(player)
                }
            }

            100 -> {
                interpreter.sendDialogues(
                    master!!.npc,
                    getExpression(master),
                    "Oh dear, what do they teach you in school?",
                )
                stage = 101
            }

            101 -> {
                player("Well....er...")
                stage = 102
            }

            102 -> {
                interpreter.sendDialogues(
                    master!!.npc,
                    getExpression(master),
                    "I suppose I'll have to educate you, then. A Slayer is",
                    "someone who is trained to fight specific creatures. They",
                    "know those creatures' every weakenss and strength. As",
                    "you can guess, it makes killing those creatures a lot",
                )
                stage = 103
            }

            103 -> {
                interpreter.sendDialogues(master!!.npc, getExpression(master), "easier.")
                stage = 104
            }

            104 -> {
                options("Wow, can you teach me?", "Sounds useless to me.")
                stage = 105
            }

            105 ->
                when (buttonId) {
                    1 -> {
                        player("Wow, can you teach me?")
                        stage = 500
                    }

                    2 -> {
                        player("Sounds useless to me.")
                        stage = 1000
                    }
                }

            500 -> {
                interpreter.sendDialogues(master!!.npc, getExpression(master), "Hmmm, well, I'm not so sure...")
                stage = 501
            }

            501 -> {
                player("Pleeeaasssse! I'll be your best friend!")
                stage = 502
            }

            502 -> {
                if (!master!!.hasRequirements(player)) {
                    interpreter.sendDialogues(
                        master!!.npc,
                        getExpression(master),
                        "Sorry, but you're not strong enough to be taught by",
                        "me.",
                    )

                    stage = 999
                } else {
                    interpreter.sendDialogues(
                        master!!.npc,
                        getExpression(master),
                        "Oh, okay then; you twisted my arm. You'll have to",
                        "train against specific groups of creatures.",
                    )
                    stage = 503
                }
            }

            503 -> {
                player("Okay then, what's first?")
                stage = 504
            }

            504 ->
                if (player.inventory.freeSlots() == 0) {
                    player("Sorry, I don't have enough inventory space.")
                    stage = 999
                } else {
                    player.inventory.add(GEM)
                    getInstance(player).generate(master!!)
                    interpreter.sendDialogues(
                        master!!.npc,
                        getExpression(master),
                        "We'll start you off hunting " + getInstance(player).taskName + "'s, you'll need to",
                        "kill " + getInstance(player).amount + " of them.",
                    )
                    stage = 510
                }

            510 -> {
                interpreter.sendDialogues(
                    master!!.npc,
                    getExpression(master),
                    "You'll also need this enchanted gem. It allows Slayer",
                    "Masters like myself to contact you and update you on",
                    "your progress. Don't worry if you lose it; you can buy",
                    "another from any Slayer Master.",
                )
                stage = 511
            }

            511 -> {
                player("Okay, great!")
                stage = 999
            }

            1000 -> {
                interpreter.sendDialogues(master!!.npc, getExpression(master), "That's what you think..")
                stage = 999
            }

            2000 -> {
                interpreter.sendDialogues(
                    master!!.npc,
                    getExpression(master),
                    "I am one of the greatest Slayer masters!",
                )
                stage = 999
            }

            690 ->
                when (buttonId) {
                    1 -> {
                        player("I need another assignment.")
                        stage = 701
                    }

                    2 -> {
                        player("Do you have anything for trade?")
                        stage = 20
                    }

                    3 -> {
                        player("Er...nothing...")
                        stage = 30
                    }

                    4 -> {
                        player("I have a question about my Achievement Diary.")
                        stage = 691
                    }
                }

            691 -> sendDiaryDialogue()
            700 ->
                when (buttonId) {
                    1 -> {
                        player("I need another assignment.")
                        stage = 701
                    }

                    2 -> {
                        player("Do you have anything for trade?")
                        stage = 20
                    }

                    3 -> {
                        player("Er...nothing...")
                        stage = 30
                    }

                    4 ->
                        if (quest!!.getStage(player) == 30) {
                            player(
                                "I'm here about a quest. Ava said she saw you hanging",
                                "around the moving trees near Draynor Manor.",
                            )
                            stage = 8000
                        } else if (quest!!.getStage(player) == 31) {
                            player("Hello, I'm here about those trees again.")
                            stage = 8006
                        }
                }

            8000 -> {
                npc(
                    getExpression(master),
                    "Ahh, you came to the right man, odd things, those trees.",
                    "What is it you are needing exactly?",
                )
                stage++
            }

            8001 -> {
                player("I think I need some of the wood from them, but my", "axe just bounced off the trunk.")
                stage++
            }

            8002 -> {
                npc(
                    getExpression(master),
                    "Sounds like you need a blessed axe. No one really",
                    "makes them, though these days.",
                )
                stage++
            }

            8003 -> {
                npc(
                    getExpression(master),
                    "If you can give me a mithril axe and a holy symbol of",
                    "Saradomin I can let you have my axe. I'll make myself",
                    "a new one when no one is pestering me for Slayer",
                    "tasks.",
                )
                stage++
            }

            8004 -> {
                player("Okay, so I'll see whether I can spare an axe and a", "symbol. Thanks.")
                stage++
            }

            8005 -> {
                quest!!.setStage(player, 31)
                end()
            }

            8006 -> {
                if (player.hasItem(Item(10491))) {
                    npc(getExpression(master), "You already have an axe.")
                    stage = 999
                } else {
                    npc(
                        getExpression(master),
                        "I can make an axe for you now, if you wish.",
                        "Remember, it will be no use for normal wooducutting",
                        "after I have added the silver edge.",
                    )
                    stage++
                }
            }

            8007 -> {
                player("I'd love one, thanks.")
                stage++
            }

            8008 -> {
                if (!player.inventory.containsItem(MITHRIL_AXE)) {
                    npc(
                        getExpression(master),
                        "You'll need to hand over both a mithril axe and a holy",
                        "symbol of Saradomin. You don't have an axe in your",
                        "pack, so I'm not able to help.",
                    )
                    stage = 999
                } else if (!player.inventory.containsItem(HOLY_SYMBOL)) {
                    npc(
                        getExpression(master),
                        "You'll need to hand over both a mithril axe and a holy",
                        "symbol of Saradomin. You don't have a holy symbol in",
                        "your pack, so I'm not able to help.",
                    )
                    stage = 999
                } else if (player.inventory.remove(MITHRIL_AXE, HOLY_SYMBOL)) {
                    player.inventory.add(Item(10491))
                    npc(getExpression(master), "Here's a new axe; may it serve you well.")
                    stage = 999
                }
            }

            701 -> {
                if (!master!!.hasRequirements(player)) {
                    interpreter.sendDialogues(
                        master!!.npc,
                        getExpression(master),
                        "Sorry, but you're not strong enough to be taught by",
                        "me.",
                    )
                    stage = 999
                } else if (!getInstance(player).hasTask()) {
                    getInstance(player).generate(master!!)
                    if (getInstance(player).task == Tasks.JAD) {
                        interpreter.sendDialogues(
                            master!!.npc,
                            getExpression(master),
                            "Excellent, you're doing great. Your new task is to",
                            "defeat the almighty TzTok-Jad.",
                        )
                    } else {
                        interpreter.sendDialogues(
                            master!!.npc,
                            getExpression(master),
                            "Excellent, you're doing great. Your new task is to kill",
                            getInstance(player).amount.toString() + " " + getInstance(player).taskName + "s.",
                        )
                    }
                    stage = 844
                } else if (hasSameTask(master!!, player)) {
                    interpreter.sendDialogues(
                        master!!.npc,
                        getExpression(master),
                        "You're still hunting " + getInstance(player).taskName + ", you have ",
                        getInstance(player).amount.toString() + " to go. Come back when you've finished your task.",
                    )
                } else {
                    getInstance(player).flags.taskStreak = 0
                    getInstance(player).generate(master!!)
                    if (getInstance(player).task == Tasks.JAD) {
                        interpreter.sendDialogues(
                            master!!.npc,
                            getExpression(master),
                            "Excellent, you're doing great. Your new task is to",
                            "defeat the almighty TzTok-Jad.",
                        )
                    } else {
                        interpreter.sendDialogues(
                            master!!.npc,
                            getExpression(master),
                            "Excellent, you're doing great. Your new task is to kill",
                            getInstance(player).amount.toString() + " " + getInstance(player).taskName + "'s.",
                        )
                    }
                }
                stage = 844
            }

            844 -> {
                options("Got any tips for me?", "Okay, great!")
                stage++
            }

            845 ->
                when (buttonId) {
                    1 -> {
                        player("Got any tips for me?")
                        stage = 860
                    }

                    2 -> {
                        player("Okay, great!")
                        stage = 999
                    }
                }

            860 -> {
                interpreter.sendDialogues(master!!.npc, getExpression(master), *getInstance(player).task!!.tip)
                stage = 861
            }

            861 -> {
                player("Great, thanks!")
                stage = 999
            }

            900 ->
                when (buttonId) {
                    1 -> {
                        player("Can I buy a Skillcape of Slayer?")
                        stage = 901
                    }

                    2 -> {
                        interpreter.sendDialogues(
                            master!!.npc,
                            FaceAnim.HALF_GUILTY,
                            "'Ello, and what are you after, then?",
                        )
                        stage = 0
                    }
                }

            901 -> {
                interpreter.sendDialogues(
                    SlayerMaster.DURADEL.npc,
                    FaceAnim.HALF_GUILTY,
                    "Certainly! Right when you give me 99000 coins.",
                )
                stage = 902
            }

            902 -> {
                options("Okay, here you go.", "No, thanks.")
                stage = 903
            }

            903 ->
                when (buttonId) {
                    1 -> {
                        player("Okay, here you go.")
                        stage = 904
                    }

                    2 -> end()
                }

            904 -> {
                if (purchase(player, Skills.SLAYER)) {
                    interpreter.sendDialogues(SlayerMaster.DURADEL.npc, FaceAnim.HALF_GUILTY, "There you go! Enjoy.")
                    stage = 999
                }
                stage = 999
            }

            906 ->
                when (buttonId) {
                    1 -> {
                        player("May I buy a Quest Point cape?")
                        stage = 907
                    }

                    2 -> {
                        interpreter.sendDialogues(
                            master!!.npc,
                            FaceAnim.HALF_GUILTY,
                            "'Ello, and what are you after, then?",
                        )
                        stage = 0
                    }
                }

            907 -> {
                npc(getExpression(master), "You bet, " + player.username + "! Right when you give me 99000 coins.")
                stage = 908
            }

            908 -> {
                options("Okay, here you go.", "No, thanks.")
                stage = 909
            }

            909 ->
                when (buttonId) {
                    1 -> {
                        player("Okay, here you go.")
                        stage = 910
                    }

                    2 -> end()
                }

            910 -> {
                if (player.inventory.freeSlots() < 2) {
                    player("I don't seem to have enough inventory space.")
                    stage = 999
                } else if (!player.inventory.containsItem(COINS)) {
                    player("I don't seem to have enough coins with", "me at this time.")
                    stage = 999
                    return true
                } else if (player.inventory.remove(COINS) && player.inventory.add(*ITEMS)) {
                    npc(getExpression(master), "Have fun with it.")
                    stage = 999
                } else {
                    player("I don't seem to have enough coins with", "me at this time.")
                    stage = 999
                }
            }
        }
        return true
    }

    private fun getExpression(master: SlayerMaster?): FaceAnim {
        if (master == SlayerMaster.CHAELDAR) {
            return FaceAnim.OLD_NORMAL
        }
        return FaceAnim.HALF_GUILTY
    }

    private fun sendDiaryDialogue() {
        isDiary = true
        if (Diary.canClaimLevelRewards(player, DiaryType.VARROCK, level)) {
            player(
                "I've completed all the hard tasks in my Varrock",
                "Achievement Diary and, let me tell you, it wasn't an",
                "easy job.",
            )
            stage = 440
            return
        } else if (Diary.canReplaceReward(player, DiaryType.VARROCK, level)) {
            player("I've seemed to have lost my armour...")
            stage = 460
            return
        } else {
            options(
                "What is the Achievement Diary?",
                "What are the rewards?",
                "How do I claim the rewards?",
                "See you later.",
            )
            stage = 0
        }
    }

    override fun newInstance(player: Player?): Dialogue {
        return SlayerMasterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.CHAELDAR_1598,
            NPCs.SANIBOCH_1596,
            NPCs.VANNAKA_1597,
            NPCs.CHAELDAR_1599,
            NPCs.SUMONA_7780,
            NPCs.DURADEL_8275,
            NPCs.TURAEL_8273,
            NPCs.MAZCHNA_8274,
        )
    }

    companion object {
        private val GEM = Item(Items.ENCHANTED_GEM_4155, 1)
        private val MITHRIL_AXE = Item(Items.MITHRIL_AXE_1355)
        private val HOLY_SYMBOL = Item(Items.HOLY_SYMBOL_1718)
        private val ITEMS = arrayOf(Item(Items.QUEST_POINT_CAPE_9813), Item(Items.QUEST_POINT_HOOD_9814))
        private val COINS = Item(Items.COINS_995, 99000)
    }
}
