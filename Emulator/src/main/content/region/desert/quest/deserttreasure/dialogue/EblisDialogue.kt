package content.region.desert.quest.deserttreasure.dialogue

import content.region.desert.quest.deserttreasure.DesertTreasure
import content.region.desert.quest.deserttreasure.handlers.DTUtils
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class EblisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id in NPCs.EBLIS_1924..NPCs.EBLIS_1925) {
            when {
                getQuestStage(player, Quests.DESERT_TREASURE) == 8 -> {
                    npc("Ah, so you got here at last.")
                    stage = 1
                }

                getQuestStage(player, Quests.DESERT_TREASURE) in 9..10 -> {
                    if (DTUtils.completedAllSubStages(player)) {
                        player(FaceAnim.THINKING, "So I have all four of these Diamonds of Azzanadra, now", "what?")
                        stage = 9
                    } else {
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "So can you give me any help on where to find these warriors and their diamonds?",
                        )
                        stage = 18
                    }
                }

                else -> {
                    player("Hello again.")
                    stage = 26
                }
            }
        } else {
            end()
            openDialogue(player!!, EblisDialogueFile(), npc)
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "As you may have noticed, I have made the mirrors for the spell, and cast the enchantment upon them.",
                )
                stage++
            }

            2 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "By simply looking into each mirror, you will be able to see the area where the trace magics from the Diamonds of Azzanadra are emanating from.",
                )
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Unfortunately, I cannot narrow the search closer with this kind of spell, but if you search the areas shown to you, you may be able to find some clues leading you to the evil warriors of Zamorak who stole the diamonds in",
                )
                stage++
            }

            4 -> {
                npcl(FaceAnim.FRIENDLY, "the first place.")
                stage++
            }

            5 -> {
                player(
                    FaceAnim.THINKING,
                    "So you can't be any more specific about where to look for these warriors and their diamonds?",
                )
                stage++
            }

            6 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm afraid not, other than the direction that the mirrow is facing will be approximately the direction you will need to head in.",
                )
                stage++
            }

            7 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Make sure to come and speak to me when you have retrieved all four diamonds.",
                )
                stage++
            }

            8 -> {
                end()
                setQuestStage(player, Quests.DESERT_TREASURE, 9)
            }

            9 -> {
                npc(
                    "Azzanadra was our greatest ever hero.",
                    "He was unkillable, and the cowardly traitors who stole",
                    "our lands did not know what to do with him, for his",
                    "hatred for them was as strong as his magics.",
                )
                stage++
            }

            10 -> {
                npc(
                    FaceAnim.SAD,
                    "In the end, they cast a spell upon him, to trap him in",
                    "the stone structure to the South of here.",
                )
                stage++
            }

            11 -> {
                npc(
                    FaceAnim.ANNOYED,
                    "They stole his very life force, the essence of his power,",
                    "and trapped it within four crystals - the very same",
                    "Four Diamonds which you have now recovered from",
                    "the brigands who stole from us.",
                )
                stage++
            }

            12 -> {
                npc(
                    FaceAnim.ANNOYED,
                    "The four pillars surrounding the structure are keeping",
                    "the containment spell intact.",
                    "By placing a diamond into each, you will breach the",
                    "magical defenses and begin to restore Azzanadra's",
                )
                stage++
            }

            13 -> {
                npcl(FaceAnim.ANNOYED, "power, and be able to enter the structure.")
                stage++
            }

            14 -> {
                npcl(FaceAnim.FRIENDLY, "Go, place the diamonds, and free my lord Azzanadra!")
                stage++
            }

            15 -> {
                npc(
                    FaceAnim.FRIENDLY,
                    "The path will be hard, for his prison is full of traps",
                    "and danger to prevent his rescue, but he will reward you",
                    "beyond your wildest dreams when freed!",
                )
                stage++
            }

            16 -> {
                npc(
                    "Quickly...",
                    "After all these centuries, Lord Azzanadra is nearly free!",
                    "You must spare no time, place the Diamonds upon the",
                    "pillars and enter the pyramid so that you may free him!",
                )
                stage++
            }

            17 -> end()

            18 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "No, the magic used in this spell is powerful, but inaccurate. The direction the scrying glass faces is roughly the direction you will find the warrior, but I'm afraid I",
                )
                stage++
            }

            19 -> {
                npc("can't be any more help than that.")
                stage++
            }

            20 -> {
                playerl(
                    FaceAnim.STRUGGLE,
                    "I don't understand why there are six mirrors when there are only four diamonds...",
                )
                stage++
            }

            21 -> {
                npc("As I say, the enchantment is very inaccurate.")
                stage++
            }

            22 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I can only focus upon the aura the diamonds have left behind them, so any place where the Diamonds were present for a significant period of time will still be shown - such as the Bandit Camp where I make my home.",
                )
                stage++
            }

            23 -> {
                npcl(FaceAnim.HALF_GUILTY, "My apologies, but magic is an inaccurate art in many respects.")
                stage++
            }

            24 -> {
                npcl(FaceAnim.NEUTRAL, "Don't forget to come back here when you have collected all four diamonds.")
                stage++
            }

            25 -> end()

            26 ->
                if (!inInventory(player, Items.ANCIENT_STAFF_4675) && isQuestComplete(player, Quests.DESERT_TREASURE)
                ) {
                    npc("So have you spoken to my Lord Azzanadra yet?")
                    stage++
                } else {
                    npc(
                        "Greetings. I await the return of my Lord Azzanadra and of our god.",
                        "I do not know why, but I feel this spot has some significance...",
                    )
                    stage = 25
                }

            27 -> {
                player("Yes I have.")
                stage++
            }

            28 -> {
                npcl(FaceAnim.NEUTRAL, "And what words did he have for his followers?")
                stage++
            }

            29 -> {
                playerl(
                    FaceAnim.STRUGGLE,
                    "Er... He didn't really mention you at all, but he did teach me some cool new magic spells.",
                )
                stage++
            }

            30 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "It is understandable perhaps... His poor mind must be addled after all of those years of confinement, he would not willingly ignore his followers...",
                )
                stage++
            }

            31 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Anyway, if he has taught you our ancient magics, you may be interested in purchasing an ancient heirloom that was passed down to me. My ancestor fought in the ancient battles using the",
                )
                stage++
            }

            32 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "magic of our god. This heirloom will help you with the speed of your spell-casting.",
                )
                stage++
            }

            33 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Normally I could not bear to part with such a priceless relic, but for your help in freeing my Lord Azzanadra, I will be prepared to sell it to you for a mere 80,000 gold.",
                )
                stage++
            }

            34 -> {
                npc("Are you interested?")
                stage++
            }

            35 -> {
                options("Yes please", "No thanks")
                stage++
            }

            36 ->
                when (buttonId) {
                    1 ->
                        if (!removeItem(player, Item(Items.COINS_995, 80000))) {
                            sendDialogue(player, "You don't have enough money to buy that.")
                            stage = 25
                        } else {
                            addItemOrDrop(player, Items.ANCIENT_STAFF_4675)
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Take care of it, it is the only heirloom from those times I possess, although rumour has it many of our ancient warriors were buried with identical weapons so that they could continue to fight for my Lord in their deaths.",
                            )
                            stage = 25
                        }

                    2 -> {
                        player("No, not really.")
                        stage = 37
                    }
                }

            37 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "As you wish. Bear my offer in mind should you ever change your decision, I will remain here.",
                )
        }
        return false
    }

    override fun newInstance(player: Player?): Dialogue = EblisDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.EBLIS_1923, NPCs.EBLIS_1924, NPCs.EBLIS_1925)
}

class EblisDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.DESERT_TREASURE, 0, 1, 2, 3, 4)
            .npcl("Leave us to our fate. We care nothing for the world that betrayed us, or those that come from it.")
            .end()

        b
            .onQuestStages(Quests.DESERT_TREASURE, 5, 6)
            .playerl(
                "Hello. I represent the Museum of Varrock, and I have reason to believe there may be some kinds of artefacts of historical significance in the nearby area...",
            ).npcl(
                "Ah yes. The only time people care about our existence is when they think they have something to gain from us.",
            ).npcl("I have nothing to say to you. You and your kind are not welcome here.")
            .playerl("Please, if I can just have a few minutes of your time to ask some questions...?")
            .npcl("(sigh) I suppose I can spare you that. What do you wish to know about?")
            .options()
            .let { optionBuilder ->
                optionBuilder
                    .option("Why is this village so hostile?")
                    .playerl(
                        "Why are all of the people here so hostile? You would think I was asking you for money instead of just for answers to a few questions...",
                    ).npcl(
                        "It is a long story, and I doubt you have much interest in hearing it. Your sort never are, you just take what you can of ours, and then abandon us once more to the desert.",
                    ).options()
                    .let { optionBuilder2 ->
                        optionBuilder2
                            .option("No, I want to hear this story.")
                            .playerl(
                                "Actually, I'd be quite interested to hear what it is you have to say to excuse the attitude everybody in this village seems to have.",
                            ).npcl(
                                "Ah, it all begun many generations ago, when our ancestors were the proud rulers of these lands...",
                            ).npcl(
                                "My ancestors lived far to the North of here, and our lands stretched from the sea in the East to the river Lum, and the mountain of ice. From coast to coast, North to South, our domain was absolute.",
                            ).npcl(
                                "Our god was kind to us, and blessed us with prosperity and happiness, and in return we were merciless to his enemies wherever we found them.",
                            ).npcl("Then came the betrayal.")
                            .npcl("Our god was banished, leaving us helpless to our fates.")
                            .npcl(
                                "Without his protection, we were forced to fend for ourselves once more, against the enemies that sought to destroy us through their petty jealousies.",
                            ).npcl(
                                "But we did not succumb without fighting! The spiteful Saradomin and pathetic Zamorak warred with each other, but the hatred they had for each other was as nothing to the hatred they held towards us!",
                            ).npcl(
                                "With each battle they waged, we lost more and more land, unable to fight on all fronts, and were pushed further and further South into this gods-forsaken desert.",
                            ).npcl(
                                "Our greatest hero, Azzanadra, was finally trapped in a strange stone structure to the South of here, and bound within by terrible powers...",
                            ).npcl(
                                "And with that our lands, our homes, our very lives were stolen from us! Too weak to reclaim what was rightfully ours, we made our homes here, knowing that someday Azzanadra will",
                            ).npcl("return with his magnificent power, and bring us back to our former glory...")
                            .playerl("So you're upset because of something that happened hundreds of years ago?")
                            .playerl("Seems to me like maybe you should find some closure, and let the past go...")
                            .npcl(
                                "The insults heaped upon my race will never be forgotten, will never be forgiven and will never again be overlooked.",
                            ).npcl(
                                "Someday, a harsh wind will blow upon this land, uncovering the wrongs of the past, and we will get back what is rightfully ours. Until such a day we will bide our time here, and will",
                            ).npcl("always be ready with our blades for our righteous vengeance.")
                            .end()

                        optionBuilder2
                            .option("I don't care about your story.")
                            .playerl(
                                "I don't really care what your story is to be honest, there is no excuse for such rudeness or hostility.",
                            ).playerl(
                                "I have done nothing wrong to you, but everybody here treats me like I have committed some great crime against the village.",
                            ).npcl("That is because, from our point of view, you have.")
                            .playerl("What? Just because I entered your village?")
                            .npcl(
                                "You have no right to be here! You have no right to the life you have, for it was taken at our expense!",
                            ).playerl(
                                "Whatever... No wonder all you loonies live out here in the desert by yourselves.",
                            ).end()
                    }

                optionBuilder
                    .option("Do you know anything about treasure near here?")
                    .playerl("I was wondering if you knew anything about some treasure somewhere around here?")
                    .playerl(
                        "I have some evidence that there might be some kind of treasure hidden very close to this village...",
                    ).npcl(
                        "If I knew of any treasure I would not choose to spend my life in this gods-forsaken desert.",
                    ).end()

                optionBuilder
                    .option("Do you know anything about a fortress near here?")
                    .playerl(
                        "Do you know anything about some kind of fortress nearby? I have reason to believe there is, or at least used to be, some kind of fortress very close to here...",
                    ).npcl("Nobody would build anything in this wasteland unless they were forced to, to survive.")
                    .npcl(
                        "I know of no fortress, I know of no reason why anyone would ever bother doing anything out here in the desert.",
                    ).end()

                optionBuilder
                    .optionIf("Tell me of the four diamonds of Azzanadra.") { player ->
                        return@optionIf getQuestStage(player, Quests.DESERT_TREASURE) == 6
                    }.playerl("So tell me... Did you ever hear of something called the Diamonds of Azzanadra?")
                    .npcl("This is the treasure which you seek???")
                    .npcl(
                        "Please accept my apologies noble @g[sir,madam]! I thought you were but some opportunistic thief, looking to steal what heritage we have left! Now I see that you are in fact a brave adventurer,",
                    ).npcl("looking to restore our glories back upon us!")
                    .playerl("Uh... yeah... So anyway, you have heard of them?")
                    .npcl(
                        "Heard of them? Of course I have heard of them! They are the legacy of the great Mahjarrat hero, Azzanadra!",
                    ).playerl(
                        "So... do you have any idea where they might be? I have a feeling they will be very valuable.",
                    ).playerl("Uh, valuable as historical artefacts I mean, obviously.")
                    .npcl(
                        "They were stolen by warriors of the false god Zamorak generations ago. When you find the warriors, you will find the diamonds.",
                    ).npcl("I suspect they will not willingly part with such objects of power however.")
                    .npc(
                        "Beware too, for these warriors are very powerful;",
                        "they have taken the powers of the diamonds into themselves!",
                    ).playerl("How do you mean?")
                    .npcl("Each diamond has an elemental quality...")
                    .npcl(
                        "There is the Diamond of Blood, the Diamond of Ice, the Diamond of Smoke and the Diamond of Shadow.",
                    ).npcl("You should expect the warriors to have taken some aspect of these diamonds as their own...")
                    .playerl("Do you have any idea how I could track down these warriors somehow, then?")
                    .npcl(
                        "There is an ancient spell I know of that may spy upon such power... But it will require a few ingredients for it to work.",
                    ).npcl(
                        "Should you be willing to get these ingredients for me, I will be able to locate the rough area where each of these warriors has taken refuge. The spell is imprecise, but it should help you get on the",
                    ).npcl("right track in your search.")
                    .npcl(
                        "Is your desire for our freedom strong enough? Will you gather the ingredients for this spell for me?",
                    ).options()
                    .let { optionBuilder2 ->

                        optionBuilder2
                            .option("Yes")
                            .playerl("Sure, what do you need?")
                            .npcl(
                                "For this spell, I will need to make some scrying glasses. I will need enough so that we can view the realm in its entirety.",
                            ).npcl(
                                "When enchanted, the scrying glass will be able to let us view any area that has been influenced by the presence of the Diamonds of Azzanadra.",
                            ).playerl("Okay, but what exactly do you need for this spell?")
                            .npcl(
                                "Well, six scrying glasses should be sufficient. For each scrying glass, I will need two magic logs, a steel bar and some molten glass. This makes a total of 12 magic logs, 6 pieces of molten",
                            ).npcl("glass, and 6 steel bars.")
                            .npcl(
                                "In addition, for the actual spell to enchant the glasses, I will require one set of normal bones, some ash, some charcoal and a single blood rune.",
                            ).npcl("Do you understand me, adventurer?")
                            .playerl("Quick question; what kind of bones do you need?")
                            .npcl("Standard bones. Other types of bones are of no use to me in this spell.")
                            .options()
                            .let { optionBuilder3 ->
                                optionBuilder3
                                    .option("Yes, I will go get those for you.")
                                    .playerl(
                                        "It's a slightly odd collection of ingredients, but I shouldn't have too much trouble getting those for you.",
                                    ).endWith { _, player ->
                                        if (getQuestStage(player, Quests.DESERT_TREASURE) == 6) {
                                            setQuestStage(player, Quests.DESERT_TREASURE, 7)
                                        }
                                    }

                                optionBuilder3
                                    .option("No, please repeat those ingredients.")
                                    .npcl("Before I can complete the spell I will still need the following items;")
                                    .npc("12 magic logs", "6 steel bars", "6 molten glass")
                                    .npc(
                                        "1 bones,",
                                        "1 ashes,",
                                        "1 charcoal",
                                        "and 1 blood rune.",
                                    ).endWith { _, player ->
                                        if (getQuestStage(player, Quests.DESERT_TREASURE) == 6) {
                                            setQuestStage(player, Quests.DESERT_TREASURE, 7)
                                        }
                                    }
                            }

                        optionBuilder2
                            .option("No")
                            .playerl("Actually I don't feel like going on a shopping trip for you right now.")
                            .npcl(
                                "As you wish. I should have known not to get my hopes up that our long cursed life may soon be at an end...",
                            ).end()
                    }

                optionBuilder
                    .option("Nothing thanks.")
                    .playerl("Actually, there was nothing I really wanted to ask you about.")
                    .npcl("Yes, it is exactly like your sort to waste my time in such a way.")
                    .end()
            }

        b
            .onQuestStages(Quests.DESERT_TREASURE, 7)
            .branch { player ->
                return@branch if (DTUtils.checkGivenItem(player)) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(1)
                    .npcl("Excellent! Those are all the ingredients I need to create the scrying glasses.")
                    .npcl(
                        "I will find a suitable spot in the desert to the East of here, and set them up. When you are ready to begin your search, please come and find me there, I will show you how to utilise the",
                    ).npcl("mirrors to find the diamonds.")
                    .endWith { _, player ->
                        if (getQuestStage(player, Quests.DESERT_TREASURE) == 7) {
                            setQuestStage(player, Quests.DESERT_TREASURE, 8)
                        }
                    }

                branch
                    .onValue(0)
                    .npcl("Before I can complete the spell I will still need the following items;")
                    .manualStage { df, player, _, _ ->
                        df.interpreter!!.sendDialogues(
                            npc!!.id,
                            FaceAnim.NEUTRAL,
                            "" + (12 - getAttribute(player, DesertTreasure.magicLogsAmount, 0)) + " magic logs",
                            "" + (6 - getAttribute(player, DesertTreasure.steelBarsAmount, 0)) + " steel bars",
                            "" + (
                                6 -
                                    getAttribute(
                                        player,
                                        DesertTreasure.moltenGlassAmount,
                                        0,
                                    )
                            ) + " molten glass",
                        )
                    }.manualStage { df, player, _, _ ->
                        df.interpreter!!.sendDialogues(
                            npc!!.id,
                            FaceAnim.NEUTRAL,
                            "" + (1 - getAttribute(player, DesertTreasure.bonesAmount, 0)) + " bones,",
                            "" + (1 - getAttribute(player, DesertTreasure.ashesAmount, 0)) + " ashes,",
                            "" + (1 - getAttribute(player, DesertTreasure.charcoalAmount, 0)) + " charcoal",
                            "and " + (
                                1 -
                                    getAttribute(
                                        player,
                                        DesertTreasure.bloodRunesAmount,
                                        0,
                                    )
                            ) + " blood rune.",
                        )
                    }.end()
            }

        b
            .onQuestStages(Quests.DESERT_TREASURE, 8, 9, 10)
            .npcl(
                "Meet me again in the desert East of here, I will use these ingredients to create a scrying glass for you.",
            ).end()

        b
            .onQuestStages(Quests.DESERT_TREASURE, 100)
            .npcl("Meet me again in the desert East of here.")
            .end()
    }
}
