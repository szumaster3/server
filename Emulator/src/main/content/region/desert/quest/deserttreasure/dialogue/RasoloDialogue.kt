package content.region.desert.quest.deserttreasure.dialogue

import content.region.desert.quest.deserttreasure.handlers.DTUtils
import content.region.desert.quest.deserttreasure.DesertTreasure
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class RasoloDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (DTUtils.getSubStage(player, DesertTreasure.shadowStage) in 1..2 &&
            getQuestStage(
                player,
                Quests.DESERT_TREASURE,
            ) >= 9
        ) {
            npcl(FaceAnim.HALF_ASKING, "Have you retrieved my gilded cross for me yet?")
            stage = 37
            return true
        }
        if (DTUtils.getSubStage(player, DesertTreasure.shadowStage) == 3) {
            npcl(
                FaceAnim.HALF_ASKING,
                "So how goes your quest? Did you managed to find the Diamond you were looking for yet?",
            )
            stage = 59
            return true
        }
        if (DTUtils.getSubStage(player, DesertTreasure.shadowStage) >= 3 &&
            getQuestStage(player, Quests.DESERT_TREASURE) >= 9 ||
            getQuestStage(player, Quests.DESERT_TREASURE) >= 10 ||
            isQuestComplete(player, Quests.DESERT_TREASURE)
        ) {
            npcl(
                FaceAnim.HAPPY,
                "Many thanks for returning my heirloom to me, adventurer. Would you like to buy something?",
            )
            stage = 61
        } else {
            npc("Greetings friend.", "I am Rasolo, the famous merchant.", "Would you care to see my wares?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                showTopics(
                    Topic("Yes", 2),
                    Topic("No", 3),
                    IfTopic(
                        "Ask about the Diamonds of Azzanadra",
                        5,
                        getQuestStage(player, Quests.DESERT_TREASURE) >= 9,
                    ),
                )

            2 -> {
                end()
                openNpcShop(player, npc!!.id)
            }

            3 -> {
                player("No, not really.")
                stage++
            }

            4 -> {
                npcl(FaceAnim.FRIENDLY, "As you wish. I will travel wherever the business takes me.")
                stage = END_DIALOGUE
            }

            5 -> {
                player("No, actually I was looking for something specific...")
                stage++
            }

            6 -> {
                npcl(FaceAnim.FRIENDLY, "Hmmmm? And what would that be?")
                stage++
            }

            7 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "I am looking for one of the Diamonds of Azzanadra. I have reason to believe it may be somewhere around here...",
                )
                stage++
            }

            8 -> {
                npcl(FaceAnim.FRIENDLY, "Ahhh... The Shadow Diamond...")
                stage++
            }

            9 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I know the object of which you speak. It is guarded by a fearsome warrior known as Damis, they say, who lives in the shadows, invisible to prying eyes...",
                )
                stage++
            }

            10 -> {
                playerl(FaceAnim.HALF_ASKING, "How can I find this 'Damis' then?")
                stage++
            }

            11 -> {
                npcl(FaceAnim.FRIENDLY, "Well now... perhaps we can help each other out here.")
                stage++
            }

            12 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I have in my possession a small trinket, a ring, that allows its wearer to see the unseen...",
                )
                stage++
            }

            13 -> {
                player("How much do you want for it?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.FRIENDLY, "Ah, but it is not for sale... As such...")
                stage++
            }

            15 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I am offering to trade it for an item that was rightfully mine, but that was stolen by a bandit named Laheeb.",
                )
                stage++
            }

            16 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "The item in question is a gilded cross, that has some sentimental value to myself.",
                )
                stage++
            }

            17 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I wish for you to recover this item for me, and I will happily let you have my ring of visibility.",
                )
                stage++
            }

            18 -> {
                playerl(FaceAnim.HALF_ASKING, "Where can I find this Laheeb?")
                stage++
            }

            19 -> {
                npcl(FaceAnim.NEUTRAL, "Well, as a travelling merchant I have roamed these lands for many years...")
                stage++
            }

            20 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "To the far east of here there is an area that is dry and barren like the desert... it is called...",
                )
                stage++
            }

            21 -> {
                playerl(FaceAnim.HALF_ASKING, "Al Kharid?")
                stage++
            }

            22 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "...Yes, Al Kharid. Now to the south of Al Kharid there is a passageway, it is called the...",
                )
                stage++
            }

            23 -> {
                playerl(FaceAnim.FRIENDLY, "Shantay Pass.")
                stage++
            }

            24 -> {
                npcl(FaceAnim.NEUTRAL, "...Yes. I didn't realise you had travelled there yourself.")
                stage++
            }

            25 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Anyway, when you have gone through the Shantay Pass, you will find yourself in a hostile desert... You will need to bring water with you to keep your life. Now, to the south-west of this pass, you will find a small",
                )
                stage++
            }

            26 -> {
                npcl(FaceAnim.THINKING, "village...")
                stage++
            }

            27 -> {
                playerl(FaceAnim.NEUTRAL, "The Bedabin camp.")
                stage++
            }

            28 -> {
                npc(FaceAnim.ANNOYED, "If you know where Laheeb lives, why did you ask me?")
                stage++
            }

            29 -> {
                playerl(FaceAnim.HALF_GUILTY, "I don't. Sorry, please continue.")
                stage++
            }

            30 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, okay then. Anyway, south of this encampment there is an area where few have ever been... It is a village of murderous bandits, and treacherous",
                )
                stage++
            }

            31 -> {
                npcl(FaceAnim.NEUTRAL, "thieves... This is where Laheeb makes his home.")
                stage++
            }

            32 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "He will have hidden his stolen treasure somewhere in that village, I am sure of it. When you find his loot, you will find my gilded cross. Return it to me, and I will reward you with my ring",
                )
                stage++
            }

            33 -> {
                npcl(FaceAnim.NEUTRAL, "of visibility, so that you may find Damis. Does this seem fair to you?")
                stage++
            }

            34 -> {
                options("Yes", "No")
                stage++
            }

            35 ->
                when (buttonId) {
                    1 -> {
                        playerl(FaceAnim.FRIENDLY, "Not a problem. I'll be back with your cross before you know it.")
                        DTUtils.setSubStage(player, DesertTreasure.shadowStage, 1)
                        stage = END_DIALOGUE
                    }

                    2 -> {
                        playerl(FaceAnim.NEUTRAL, "Sounds like too much effort to me. I'll find this Damis by myself.")
                        stage++
                    }
                }

            36 -> {
                npc("As you wish.")
                stage = END_DIALOGUE
            }

            37 -> {
                if (DTUtils.getSubStage(player, DesertTreasure.shadowStage) == 2 &&
                    inInventory(
                        player,
                        Items.GILDED_CROSS_4674,
                    )
                ) {
                    player("Yes I have!")
                    stage = 56
                } else {
                    player("No, not yet...")
                    stage++
                }
            }

            38 -> {
                npc("Well what seems to be the problem?")
                stage++
            }

            39 -> {
                options("Where can I find Laheeb?", "Can't I just buy your ring?", "Is Damis near here, then?")
                stage++
            }

            40 ->
                when (buttonId) {
                    1 -> player("Where can I find Laheeb?").also { stage++ }
                    2 -> player("Can't I just buy your ring?").also { stage = 54 }
                    3 -> player("Is Damis near here, then?").also { stage = 55 }
                }

            41 -> {
                npcl(FaceAnim.NEUTRAL, "Well, as a travelling merchant I have roamed these lands for many years...")
                stage++
            }

            42 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "To the far east of here there is an area that is dry and barren like the desert... it is called...",
                )
                stage++
            }

            43 -> {
                player("Al Kharid?")
                stage++
            }

            44 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "...Yes, Al Kharid. Now to the south of Al Kharid there is a passageway, it is called the...",
                )
                stage++
            }

            45 -> {
                player("Shantay Pass.")
                stage++
            }

            46 -> {
                npcl(FaceAnim.FRIENDLY, "...Yes. I didn't realise you had travelled there yourself.")
                stage++
            }

            47 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Anyway, when you have gone through the Shantay Pass, you will find yourself in a hostile desert... You will need to bring water with you to keep your life. Now, to the south-west of this pass, you will find a small",
                )
                stage++
            }

            48 -> {
                npcl(FaceAnim.NEUTRAL, "village...")
                stage++
            }

            49 -> {
                playerl(FaceAnim.FRIENDLY, "The Bedabin camp.")
                stage++
            }

            50 -> {
                npcl(FaceAnim.NEUTRAL, "If you know where Laheeb lives, why did you ask me?")
                stage++
            }

            51 -> {
                playerl(FaceAnim.STRUGGLE, "I don't. Sorry, please continue.")
                stage++
            }

            52 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, okay then. Anyway, south of this encampment there is an area where few have ever been... It is a village of murderous bandits, and treacherous",
                )
                stage++
            }

            53 -> {
                npcl(FaceAnim.NEUTRAL, "thieves... This is where Laheeb makes his home.")
                stage++
            }

            54 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "No, it is not for sale. Some things are more important than money, and the return of my gilded cross is one of them.",
                )
                stage = END_DIALOGUE
            }

            55 -> {
                npcl(FaceAnim.NEUTRAL, "You would be surprised to know just how close he is...")
                stage = END_DIALOGUE
            }

            56 -> {
                npcl(FaceAnim.NEUTRAL, "Excellent, excellent. Here, take this ring.")
                stage++
            }

            57 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "While you wear it, you will be able to see the things that live in shadows...And you will be able to find the entrance to Damis' lair.",
                )
                stage++
            }

            58 ->
                if (removeItem(player, Items.GILDED_CROSS_4674)) {
                    DTUtils.setSubStage(player, DesertTreasure.shadowStage, 3)
                    addItemOrDrop(player, Items.RING_OF_VISIBILITY_4657)
                }

            59 -> {
                player("Not yet...")
                stage++
            }

            60 -> {
                npcl(FaceAnim.NEUTRAL, "Well, his lair is very close to here. I suggest you look around for it.")
                stage = END_DIALOGUE
            }

            61 -> {
                val ringOfVisibility = hasAnItem(player, Items.RING_OF_VISIBILITY_4657).container != null
                showTopics(
                    Topic("Yes", 2),
                    Topic("No", 3),
                    IfTopic("I lost that ring you gave me...", 62, !ringOfVisibility),
                )
            }

            62 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have enough space in your inventory.")
                    return true
                }
                npcl(
                    FaceAnim.NEUTRAL,
                    "Then by all means, take another. Only a foolish merchant would give away his only stock!",
                )
                addItem(player, Items.RING_OF_VISIBILITY_4657)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RasoloDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RASOLO_1972)
    }
}
