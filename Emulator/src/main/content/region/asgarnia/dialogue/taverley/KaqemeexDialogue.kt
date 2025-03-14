package content.region.asgarnia.dialogue.taverley

import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.startQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KaqemeexDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size >= 2) {
            npc("I will now explain the fundamentals of Herblore:")
            stage = 1000
            return true
        }
        player(FaceAnim.FRIENDLY, "Hello there.")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val questStage = getQuestStage(player, Quests.DRUIDIC_RITUAL)
        when (stage) {
            0 -> {
                when (questStage) {
                    100 -> {
                        npc("Hello again. How is the Herblore going?")
                        stage = 600
                    }

                    10 -> {
                        npc(FaceAnim.FRIENDLY, "Hello again.")
                        stage = 40
                    }

                    99 -> {
                        npc(
                            FaceAnim.FRIENDLY,
                            "I have word from Sanfew that you have been very",
                            "helpful in assisting him with his preparations for the",
                            "purification ritual. As promised I will now teach you the",
                            "ancient arts of Herblore.",
                        )
                        stage = 200
                    }

                    else -> {
                        npc(FaceAnim.FRIENDLY, "What brings you to our holy monument?")
                        stage = 1
                    }
                }
            }

            1 -> if (questStage == 10) {
                if (isMaster(player, Skills.HERBLORE)) {
                    options("Can I buy a Skillcape of Herblore?", "Who are you?", "Did you build this?")
                    stage = 800
                } else {
                    options("Who are you?", "Did you build this?")
                    stage = 500
                }
            } else {
                options("Who are you?", "I'm in search of a quest.", "Did you build this?")
                stage = 2
            }

            2 -> when (buttonId) {
                1 -> {
                    player(FaceAnim.ASKING, "Who are you?")
                    stage = 10
                }

                2 -> {
                    player(FaceAnim.FRIENDLY, "I'm in search of a quest.")
                    stage = 20
                }

                3 -> {
                    player(FaceAnim.ASKING, "Did you build this?")
                    stage = 30
                }
            }

            10 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "We are the druids of Guthix. We worship our god at",
                    "our famous stone circles. You will find them located",
                    "throughout these lands.",
                )
                stage = 11
            }

            11 -> {
                player(FaceAnim.NEUTRAL, "Well, I'll be on my way now.")
                stage = 12
            }

            12 -> {
                npc(FaceAnim.FRIENDLY, "Goodbye adventurer. I feel we shall meet again.")
                stage = 13
            }

            13 -> end()
            30 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "What, personally? No, ofcourse I didn't. However, our",
                    "four fathers did. The first Druids of Guthix built many",
                    "stone circles across these lands over eight hundred",
                    "years ago.",
                )
                stage = 31
            }

            31 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Unfortunately we only know of two remaining and of",
                    "those only one is usable by us anymore.",
                )
                stage = 32
            }

            32 -> {
                player(FaceAnim.NEUTRAL, "Well, I'll be on my way now.")
                stage = 12
            }

            20 -> {
                npc(
                    FaceAnim.HAPPY,
                    "Hmm. I think I may have a worthwhile quest for you",
                    "actually. I don't know if you are familiar with the stone",
                    "circle south of Varrock or not, but...",
                )
                stage = 21
            }

            21 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "That used to be our stone circle. Unfortunately,",
                    "many years ago, dark wizards cast a wicked spell",
                    "upon it so that they could corrupt its power for their",
                    "own evil ends.",
                )
                stage = 22
            }

            22 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When they cursed the rocks for their rituals, they made",
                    "them useless to us and our magics. We require a brave",
                    "adventurer to go on a quest for us to help purify the",
                    "circle of Varrock.",
                )
                stage = 23
            }

            23 -> {
                options("Ok, I will try and help.", "No, that doesn't sound very interesting.")
                stage = 24
            }

            24 -> when (buttonId) {
                1 -> {
                    player(FaceAnim.HALF_GUILTY, "Ok, I will try and help.")
                    stage = 26
                }

                2 -> {
                    player(FaceAnim.HALF_GUILTY, "No, that doesn't sound very interesting.")
                    stage = 25
                }
            }

            25 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I will not try and change your mind adventurer. Some",
                    "day when you have matured you may reconsider your",
                    "position. We will wait until then.",
                )
                stage = 13
            }

            26 -> {
                npc(
                    FaceAnim.HAPPY,
                    "Excellent. Go to the village south of this place and speak",
                    "to my fellow Sanfew who is working on the purification",
                    "ritual. He knows better than I what is required to",
                    "complete it.",
                )
                startQuest(player, Quests.DRUIDIC_RITUAL)
                stage = 27
            }

            27 -> {
                player(FaceAnim.HAPPY, "Will do.")
                stage = 28
            }

            28 -> end()
            40 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You will need to speak to my fellow druid Sanfew in",
                    "the village south of here to continue in your quest",
                    "adventurer.",
                )
                stage = 41
            }

            41 -> end()
            200 -> {
                end()
                finishQuest(player, Quests.DRUIDIC_RITUAL)
            }

            500 -> when (buttonId) {
                1 -> {
                    player(FaceAnim.HALF_ASKING, "Who are you?")
                    stage = 10
                }

                2 -> {
                    player(FaceAnim.HALF_ASKING, "Did you build this?")
                    stage = 30
                }
            }

            501 -> when (buttonId) {
                1 -> {
                    player(FaceAnim.HAPPY, "Can I buy a Skillcape of Herblore?")
                    stage = 800
                }

                2 -> {
                    player(FaceAnim.ASKING, "Who are you?")
                    stage = 10
                }

                3 -> {
                    player(FaceAnim.HALF_ASKING, "Did you build this?")
                    stage = 30
                }
            }

            600 -> {
                player("Good good!")
                stage = 601
            }

            601 -> if (isMaster(player, Skills.HERBLORE)) {
                options("Can I buy a Skillcape of Herblore?", "Who are you?", "Did you build this?")
                stage = 501
            } else {
                options("Who are you?", "Did you build this?")
                stage = 500
            }

            1000 -> {
                npc(
                    "Herblore is the skill of working with herbs and other",
                    "ingredients, to make useful potions and poison.",
                )
                stage = 1001
            }

            1001 -> {
                npc("First you will need a vial, which can be found or made", "with the crafting skill.")
                stage = 1002
            }

            1002 -> {
                npc("Then you must gather the herbs needed to make the", "potion you want.")
                stage = 1003
            }

            1003 -> {
                npc(
                    "Refer to the Council's instructions in the Skills section",
                    "of the website for the items needed to make a particular",
                    "kind of potion.",
                )
                stage = 1004
            }

            1004 -> {
                npc(
                    "You must fill your vial with water and add the",
                    "ingredients you need. There are normally 2 ingredients",
                    "to each type of potion.",
                )
                stage = 1005
            }

            1005 -> {
                npc("Bear in mind you must first identify each herb, to see", "what it is.")
                stage = 1006
            }

            1006 -> {
                npc(
                    "You may also have to grind some herbs before you can",
                    "use them. You will need a pestle and mortar in order",
                    "to do this.",
                )
                stage = 1007
            }

            1007 -> {
                npc("Herbs can be found on the ground, and are also", "dropped by some monsters when you kill them.")
                stage = 1008
            }

            1008 -> {
                npc("Mix these in your water-filled vial, and you will produce", "an Attack potion.")
                stage = 1009
            }

            1009 -> {
                npc("Drink this poition to increase your Attack level.")
                stage = 1010
            }

            1010 -> {
                npc("Different potions also require different Herblore levels", "before you can make them.")
                stage = 1011
            }

            1011 -> {
                npc(
                    "Once again, check the instructions found on the",
                    "Council's website for the levels needed to make a",
                    "particulur potion.",
                )
                stage = 1012
            }

            1012 -> {
                npc("Good luck with your Herblore practices, Good day", "Adventurer.")
                stage = 1013
            }

            1013 -> {
                player("Thanks for your help.")
                stage = 1014
            }

            1014 -> end()
            800 -> {
                npc("Certainly! Right when you give me 99000 coins.")
                stage = 801
            }

            801 -> {
                options("Yes, here you go,", "No, thanks.")
                stage = 802
            }

            802 -> when (buttonId) {
                1 -> {
                    player("Yes, here you go.")
                    stage = 803
                }

                2 -> end()
            }

            803 -> {
                if (purchase(player, Skills.HERBLORE)) {
                    npc("There you go! Enjoy.")
                }
                stage = 804
            }

            804 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KAQEMEEX_455)
    }
}
