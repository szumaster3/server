package content.region.misthalin.dialogue.digsite

import content.region.misthalin.quest.itexam.TheDigSite
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ExaminerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, ExaminerDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ExaminerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EXAMINER_618, NPCs.EXAMINER_4566, NPCs.EXAMINER_4567)
    }
}

class ExaminerDialogueFile : DialogueBuilderFile() {
    companion object;

    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.THE_DIG_SITE, 100)
            .npcl(
                FaceAnim.HAPPY,
                "Hello there! My colleague tells me you helped to uncover a hidden altar to the god Zaros.",
            ).npcl(FaceAnim.HAPPY, "A great scholar and archaeologist indeed! Good health and prosperity to you.")
            .options()
            .let { optionBuilder ->
                optionBuilder
                    .option("Thanks.")
                    .player(FaceAnim.HAPPY, "Thanks!")
                    .end()
                optionBuilder
                    .option_playerl("I have lost my trowel.")
                    .npcl("Deary me... That was a good one as well. It's a good job I have another. Here you go...")
                    .endWith { _, player ->
                        addItemOrDrop(player, Items.TROWEL_676)
                    }
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 6, 7, 8, 9, 10, 11, 12)
            .npcl(FaceAnim.FRIENDLY, "Well, what are you doing here? Get digging!")

        b
            .onQuestStages(Quests.THE_DIG_SITE, 5)
            .playerl(FaceAnim.FRIENDLY, "Hello.")
            .npcl(FaceAnim.FRIENDLY, "Ah, hello again.")
            .options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder
                    .option_playerl("I am ready for the last exam...")
                    .goto(continuePath)
                optionBuilder
                    .option_playerl("I am stuck on a question.")
                    .npcl(
                        "Well, well, have you not been doing any studies? I am not giving you the answers, talk to the other students and remember the answers.",
                    ).end()
                optionBuilder
                    .option_playerl("Sorry, I didn't mean to disturb you.")
                    .npcl("Oh, no problem at all.")
                    .end()
                optionBuilder
                    .option_playerl("I have lost my trowel.")
                    .branch { player ->
                        if (inInventory(player, Items.TROWEL_676)) {
                            0
                        } else {
                            1
                        }
                    }.let { branch ->
                        branch
                            .onValue(0)
                            .npcl("Really? Look in your backpack and make sure first.")
                            .end()
                        branch
                            .onValue(1)
                            .npcl(
                                "Deary me. That was a good one as well. It's a good job I have another. Here you go...",
                            ).endWith { _, player ->
                                addItemOrDrop(player, Items.TROWEL_676)
                            }
                    }
                return@let continuePath.builder()
            }.npcl(
                FaceAnim.NEUTRAL,
                "Attention, this is the final part of the Earth Sciences exam: Earth Sciences level 3 - Advanced.",
            ).npcl(FaceAnim.NEUTRAL, "Question 1 - Sample preparation. Can you tell me how we prepare samples?")
            .options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeFirstQuestion)
                optionBuilder
                    .optionIf("Samples may be mixed together safely.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrPurpleExam3ObtainAnswer, false)
                    }.playerl("Samples may be mixed together safely.")
                    .goto(continuePath)
                optionBuilder
                    .optionIf("Samples cleaned, and carried only in specimen jars.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrPurpleExam3ObtainAnswer, false)
                    }.playerl("Samples cleaned, and carried only in specimen jars.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Sample types catalogued and carried by hand only.").goto(continuePath)
                optionBuilder.option_playerl("Samples to be spread thickly with mashed banana.").goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, next question...")
            .npcl(
                FaceAnim.NEUTRAL,
                "Earth Sciences level 3, question 2 - Specimen brush use. What is the proper way to use a specimen brush?",
            ).options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeSecondQuestion)
                optionBuilder
                    .optionIf("Brush quickly using a wet brush.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrGreenExam3ObtainAnswer, false)
                    }.playerl("Brush quickly using a wet brush.")
                    .goto(continuePath)
                optionBuilder
                    .optionIf("Brush carefully and slowly using short strokes.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrGreenExam3ObtainAnswer, false)
                    }.playerl("Brush carefully and slowly using short strokes.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Dipped in glue and stuck to a sheep's back.").goto(continuePath)
                optionBuilder.option_playerl("Brush quickly and with force.").goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, next question...")
            .npcl(
                FaceAnim.NEUTRAL,
                "Earth Sciences level 3, question 3 - Advanced techniques. Can you describe the technique for handling bones?",
            ).options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeThirdQuestion)
                optionBuilder
                    .optionIf("Bones must not be taken from the site.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrBrownExam3ObtainAnswer, false)
                    }.playerl("Bones must not be taken from the site.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Feed to hungry dogs.").goto(continuePath)
                optionBuilder.option_playerl("Bones to be ground and tested for mineral content.").goto(continuePath)
                optionBuilder
                    .optionIf("Handle bones very carefully and keep them away from other samples.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrBrownExam3ObtainAnswer, false)
                    }.playerl("Handle bones very carefully and keep them away from other samples.")
                    .goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, that concludes the level 3 Earth Sciences exam.")
            .npcl(FaceAnim.HAPPY, "Let me add up the results...")
            .branch { player ->
                var ansCount = 0
                if (getAttribute(player, TheDigSite.attributeFirstQuestion, -1) == 0 &&
                    getAttribute(player, TheDigSite.attrPurpleExam3ObtainAnswer, false)
                ) {
                    ansCount++
                }
                if (getAttribute(player, TheDigSite.attributeSecondQuestion, -1) == 0 &&
                    getAttribute(player, TheDigSite.attrGreenExam3ObtainAnswer, false)
                ) {
                    ansCount++
                }
                if (getAttribute(player, TheDigSite.attributeThirdQuestion, -1) == 2 &&
                    getAttribute(player, TheDigSite.attrBrownExam3ObtainAnswer, false)
                ) {
                    ansCount++
                }
                return@branch ansCount
            }.let { branch ->

                branch
                    .onValue(0)
                    .npcl(
                        FaceAnim.ANGRY,
                        "I cannot believe this! Absolutely none right at all. I doubt you did any research before you took this exam...",
                    ).playerl(FaceAnim.SAD, "Ah... Yes... Erm.... I think I had better go and revise first!")
                    .end()

                branch
                    .onValue(1)
                    .npcl(FaceAnim.FRIENDLY, "You got one question correct. Try harder!")
                    .playerl(FaceAnim.FRIENDLY, "Oh bother!")
                    .end()

                branch
                    .onValue(2)
                    .npcl(FaceAnim.FRIENDLY, "You got two questions correct. A little more study and you will pass it.")
                    .playerl(FaceAnim.FRIENDLY, "I'm nearly there...")
                    .end()

                branch
                    .onValue(3)
                    .npcl(FaceAnim.FRIENDLY, "You got all the questions correct, well done!")
                    .playerl(FaceAnim.FRIENDLY, "Hooray!")
                    .betweenStage { _, player, _, _ ->

                        addItemOrDrop(player, Items.LEVEL_3_CERTIFICATE_693)
                    }.npcl(
                        FaceAnim.FRIENDLY,
                        "Congratulations! You have now passed the Earth Sciences level 3 exam. Here is your level 3 certificate.",
                    ).playerl(FaceAnim.FRIENDLY, "I can dig wherever I want now!")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Perhaps you should use your newfound skills to find an artefact on the digsite that will impress the archaeological expert.",
                    ).endWith { _, player ->
                        if (getQuestStage(player, Quests.THE_DIG_SITE) == 5) {
                            setQuestStage(player, Quests.THE_DIG_SITE, 6)
                        }
                        openInterface(player, 444)
                        sendString(player, player.username, 444, 5)
                    }
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 4)
            .playerl(FaceAnim.FRIENDLY, "Hello.")
            .npcl(FaceAnim.FRIENDLY, "Hello again.")
            .options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder
                    .option_playerl("I am ready for the next exam.")
                    .goto(continuePath)
                optionBuilder
                    .option_playerl("I am stuck on a question.")
                    .npcl(
                        "Well, well, have you not been doing any studies? I am not giving you the answers, talk to the other students and remember the answers.",
                    ).end()
                optionBuilder
                    .option_playerl("Sorry, I didn't mean to disturb you.")
                    .npcl("Oh, no problem at all.")
                    .end()
                optionBuilder
                    .option_playerl("I have lost my trowel.")
                    .branch { player ->
                        if (inInventory(player, Items.TROWEL_676)) {
                            0
                        } else {
                            1
                        }
                    }.let { branch ->
                        branch
                            .onValue(0)
                            .npcl("Really? Look in your backpack and make sure first.")
                            .end()
                        branch
                            .onValue(1)
                            .npcl(
                                "Deary me. That was a good one as well. It's a good job I have another. Here you go...",
                            ).endWith { _, player ->
                                addItemOrDrop(player, Items.TROWEL_676)
                            }
                    }
                return@let continuePath.builder()
            }.npcl(
                FaceAnim.NEUTRAL,
                "Okay, this is the next part of the Earth Sciences exam: Earth Sciences level 2 - Intermediate.",
            ).npcl(FaceAnim.NEUTRAL, "Question 1 - Sample transportation. Can you tell me how we transport samples?")
            .options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeFirstQuestion)
                optionBuilder
                    .optionIf("Samples cut and cleaned before transportation.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrBrownExam2ObtainAnswer, false)
                    }.playerl("Samples cut and cleaned before transportation.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Samples ground and suspended in an acid solution.").goto(continuePath)
                optionBuilder.option_playerl("Samples to be given to the melon-collecting monkey. ").goto(continuePath)
                optionBuilder
                    .optionIf("Samples taken in rough form; kept only in sealed containers.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrBrownExam2ObtainAnswer, false)
                    }.playerl("Samples taken in rough form; kept only in sealed containers.")
                    .goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, next question...")
            .npcl(
                FaceAnim.NEUTRAL,
                "Earth Sciences level 2, question 2 - Handling of finds. What is the proper way to handle finds?",
            ).options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeSecondQuestion)
                optionBuilder
                    .optionIf("Finds must not be handled by anyone.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrPurpleExam2ObtainAnswer, false)
                    }.playerl("Finds must not be handled by anyone.")
                    .goto(continuePath)
                optionBuilder
                    .optionIf("Finds must be carefully handled.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrPurpleExam2ObtainAnswer, false)
                    }.playerl("Finds must be carefully handled.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Finds to be given to the site workmen.").goto(continuePath)
                optionBuilder.option_playerl("Drop them on the floor and jump on them.").goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, next question...")
            .npcl(
                FaceAnim.NEUTRAL,
                "Earth Sciences level 2, question 3 - Rock pick usage. Can you tell me the proper use for a rock pick?",
            ).options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeThirdQuestion)
                optionBuilder
                    .optionIf("Strike rock repeatedly until powdered.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrGreenExam2ObtainAnswer, false)
                    }.playerl("Strike rock repeatedly until powdered.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Rock pick must be used flat and with strong force.").goto(continuePath)
                optionBuilder
                    .optionIf("Always handle with care; strike cleanly on its cleaving point.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrGreenExam2ObtainAnswer, false)
                    }.playerl("Always handle with care; strike cleanly on its cleaving point.")
                    .goto(continuePath)
                optionBuilder
                    .option_playerl("Protective clothing to be worn; tools kept away from site.")
                    .goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, that covers the level 2 Earth Sciences exam.")
            .npcl(FaceAnim.HAPPY, "Let me add up your total...")
            .branch { player ->
                var ansCount = 0
                if (getAttribute(player, TheDigSite.attributeFirstQuestion, -1) == 2 &&
                    getAttribute(player, TheDigSite.attrBrownExam2ObtainAnswer, false)
                ) {
                    ansCount++
                }
                if (getAttribute(player, TheDigSite.attributeSecondQuestion, -1) == 0 &&
                    getAttribute(player, TheDigSite.attrPurpleExam2ObtainAnswer, false)
                ) {
                    ansCount++
                }
                if (getAttribute(player, TheDigSite.attributeThirdQuestion, -1) == 1 &&
                    getAttribute(player, TheDigSite.attrGreenExam2ObtainAnswer, false)
                ) {
                    ansCount++
                }
                return@branch ansCount
            }.let { branch ->

                branch
                    .onValue(0)
                    .npcl(FaceAnim.ANGRY, "No, no, no! This will not do. They are all wrong. Start again!")
                    .playerl(FaceAnim.SAD, "Oh no!")
                    .npcl("More studying for you my @g[boy,girl].")
                    .end()

                branch
                    .onValue(1)
                    .npcl(FaceAnim.FRIENDLY, "You got one question correct. At least it's a start.")
                    .playerl(FaceAnim.FRIENDLY, "Oh well..")
                    .npcl(FaceAnim.FRIENDLY, "Get out and explore the site, talk to people and learn!")
                    .end()

                branch
                    .onValue(2)
                    .npcl(FaceAnim.FRIENDLY, "You got two questions correct. Not too bad, but you can do better...")
                    .playerl(FaceAnim.FRIENDLY, "Nearly got it.")
                    .end()

                branch
                    .onValue(3)
                    .npcl(FaceAnim.FRIENDLY, "You got all the questions correct, well done!")
                    .playerl(FaceAnim.FRIENDLY, "Great, I'm getting good at this.")
                    .betweenStage { _, player, _, _ ->
                        addItemOrDrop(player, Items.LEVEL_2_CERTIFICATE_692)
                    }.npcl(
                        FaceAnim.FRIENDLY,
                        "You have now passed the Earth Sciences level 2 intermediate exam. Here is your certificate. Of course, you'll want to get studying for your next exam now!",
                    ).endWith { _, player ->
                        if (getQuestStage(player, Quests.THE_DIG_SITE) == 4) {
                            setQuestStage(player, Quests.THE_DIG_SITE, 5)
                        }
                        openInterface(player, 441)
                        sendString(player, player.username, 441, 5)
                    }
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 1, 2, 3)
            .branch { player ->
                if (getQuestStage(player, Quests.THE_DIG_SITE) == 2 && !inInventory(player, Items.SEALED_LETTER_683)) {
                    return@branch 1
                }
                return@branch getQuestStage(player, Quests.THE_DIG_SITE)
            }.let { branch ->
                val continuePath = b.placeholder()
                branch
                    .onValue(1)
                    .playerl(FaceAnim.FRIENDLY, "Hello.")
                    .npcl(FaceAnim.FRIENDLY, "Hello again.")
                    .npcl(FaceAnim.FRIENDLY, "I am still waiting for your letter of recommendation.")
                    .options()
                    .let { optionBuilder ->
                        optionBuilder
                            .option_playerl("I have lost the letter you gave me.")
                            .branch { player ->
                                if (inInventory(player, Items.UNSTAMPED_LETTER_682)) {
                                    return@branch 0
                                } else if (inBank(player, Items.UNSTAMPED_LETTER_682)) {
                                    return@branch 1
                                } else {
                                    return@branch 2
                                }
                            }.let { branch ->
                                branch
                                    .onValue(0)
                                    .npcl("Oh now come on. You have it with you!")
                                    .end()
                                branch
                                    .onValue(1)
                                    .npcl("You already have the letter in your bank.")
                                    .end()
                                branch
                                    .onValue(2)
                                    .npcl("That was foolish. Take this one and don't lose it!")
                                    .endWith { _, player ->
                                        addItemOrDrop(player, Items.UNSTAMPED_LETTER_682)
                                    }
                                return@let branch
                            }
                        optionBuilder
                            .option_playerl("Alright I'll try and get it.")
                            .npcl("I am sure you won't get any problems. Speak to the Curator of Varrock's museum.")
                            .end()
                    }
                branch
                    .onValue(2)
                    .playerl(FaceAnim.FRIENDLY, "Hello.")
                    .npcl(FaceAnim.FRIENDLY, "Hello again.")
                    .playerl(FaceAnim.FRIENDLY, "Here is the stamped letter you asked for.")
                    .betweenStage { _, player, _, _ ->
                        if (inInventory(player, Items.SEALED_LETTER_683)) {
                            removeItem(player, Items.SEALED_LETTER_683)
                        }
                        if (getQuestStage(player, Quests.THE_DIG_SITE) == 2) {
                            setQuestStage(player, Quests.THE_DIG_SITE, 3)
                        }
                    }.npcl(FaceAnim.NEUTRAL, "Good, good. We will begin the exam...")
                    .goto(continuePath)
                branch
                    .onValue(3)
                    .playerl(FaceAnim.FRIENDLY, "Hello.")
                    .npcl(FaceAnim.FRIENDLY, "Hello again. Are you ready for another shot at the exam?")
                    .options()
                    .let { optionBuilder ->
                        val continuePath2 = b.placeholder()
                        optionBuilder
                            .option_playerl("Yes, I certainly am. ")
                            .goto(continuePath)
                        optionBuilder
                            .option_playerl("No, not at the moment.")
                            .npcl(FaceAnim.NEUTRAL, "Okay, take your time if you wish.")
                            .end()
                        return@let continuePath2.builder()
                    }

                return@let continuePath.builder()
            }.npcl(FaceAnim.NEUTRAL, "Okay, we will start with the first exam: Earth Sciences level 1 - Beginner.")
            .npcl(FaceAnim.NEUTRAL, "Question 1 - Earth Sciences overview. Can you tell me what Earth Sciences is?")
            .options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeFirstQuestion)
                optionBuilder
                    .optionIf("The study of gardening, planing and fruiting vegetation.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrGreenExam1ObtainAnswer, false)
                    }.playerl("The study of gardening, planing and fruiting vegetation.")
                    .goto(continuePath)
                optionBuilder
                    .optionIf("The study of the earth, its contents and history.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrGreenExam1ObtainAnswer, false)
                    }.playerl("The study of the earth, its contents and history.")
                    .goto(continuePath)
                optionBuilder.option_playerl("The study of planets and the history of worlds.").goto(continuePath)
                optionBuilder.option_playerl("The combination of archaeology and vegetarianism.").goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, next question...")
            .npcl(
                FaceAnim.NEUTRAL,
                "Earth Sciences level 1, question 2 - Eligibility. Can you tell me which people are allowed to use the digsite?",
            ).options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeSecondQuestion)
                optionBuilder
                    .optionIf("Magic users, miners and their escorts.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrBrownExam1ObtainAnswer, false)
                    }.playerl("Magic users, miners and their escorts.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Professors, students and workmen only.").goto(continuePath)
                optionBuilder.option_playerl("Local residents, contractors and small pink fish.").goto(continuePath)
                optionBuilder
                    .optionIf("All that have passed the appropriate Earth Sciences exam.") { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrBrownExam1ObtainAnswer, false)
                    }.playerl("All that have passed the appropriate Earth Sciences exam.")
                    .goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, next question...")
            .npcl(
                FaceAnim.NEUTRAL,
                "Earth Sciences level 1, question 3 - Health and safety. Can you tell me the proper safety points when working on a digsite?",
            ).options()
            .let { optionBuilder ->
                val continuePath = b.placeholder()
                optionBuilder.recordAttribute(TheDigSite.attributeThirdQuestion)
                optionBuilder
                    .optionIf("Heat-resistant clothing to be worn at all times.") { player ->
                        return@optionIf !getAttribute(player, TheDigSite.attrPurpleExam1ObtainAnswer, false)
                    }.playerl("Heat-resistant clothing to be worn at all times.")
                    .goto(continuePath)
                optionBuilder.option_playerl("Rubber chickens to be worn on the head at all times.").goto(continuePath)
                optionBuilder
                    .optionIf(
                        "Gloves and boots to be warn at all times; proper tools must be used.",
                    ) { player ->
                        return@optionIf getAttribute(player, TheDigSite.attrPurpleExam1ObtainAnswer, false)
                    }.playerl("Gloves and boots to be warn at all times; proper tools must be used.")
                    .goto(continuePath)
                optionBuilder
                    .option_playerl("Protective clothing to be worn; tools kept away from site.")
                    .goto(continuePath)
                return@let continuePath.builder()
            }.npcl("Okay, that covers the level 1 Earth Sciences exam.")
            .npcl(FaceAnim.HAPPY, "Let's see how you did...")
            .branch { player ->
                var ansCount = 0
                if (getAttribute(player, TheDigSite.attributeFirstQuestion, -1) == 0 &&
                    getAttribute(player, TheDigSite.attrGreenExam1ObtainAnswer, false)
                ) {
                    ansCount++
                }
                if (getAttribute(player, TheDigSite.attributeSecondQuestion, -1) == 2 &&
                    getAttribute(player, TheDigSite.attrBrownExam1ObtainAnswer, false)
                ) {
                    ansCount++
                }
                if (getAttribute(player, TheDigSite.attributeThirdQuestion, -1) == 1 &&
                    getAttribute(player, TheDigSite.attrPurpleExam1ObtainAnswer, false)
                ) {
                    ansCount++
                }
                return@branch ansCount
            }.let { branch ->

                branch
                    .onValue(0)
                    .npcl(
                        FaceAnim.ANGRY,
                        "Oh dear me! This is appalling, none correct at all! I suggest you go and study properly.",
                    ).playerl(FaceAnim.SAD, "Oh dear...")
                    .npcl(
                        "Why don't you use the resources here? There are books and the researchers... and you could even ask other students who are also studying for these exams.",
                    ).end()

                branch
                    .onValue(1)
                    .npcl(FaceAnim.FRIENDLY, "You got one question correct. Better luck next time.")
                    .playerl(FaceAnim.FRIENDLY, "Oh bother!")
                    .npcl(FaceAnim.FRIENDLY, "Do some more research. I'm sure other students could help you out.")
                    .end()

                branch
                    .onValue(2)
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "You got two questions correct. Not bad, just a little more revision needed.",
                    ).playerl(FaceAnim.FRIENDLY, "Oh well...")
                    .end()

                branch
                    .onValue(3)
                    .npcl(FaceAnim.FRIENDLY, "You got all the questions correct. Well done!")
                    .playerl(FaceAnim.FRIENDLY, "Hey! Excellent!")
                    .betweenStage { _, player, _, _ ->
                        addItemOrDrop(player, Items.TROWEL_676)
                        addItemOrDrop(player, Items.LEVEL_1_CERTIFICATE_691)
                    }.npcl(
                        FaceAnim.FRIENDLY,
                        "You have now passed the Earth Sciences level 1 general exam. Here is your certificate to prove it. You also get a decent trowel to dig with. Of course, you'll want to get studying for your next exam now!",
                    ).endWith { _, player ->

                        if (getQuestStage(player, Quests.THE_DIG_SITE) == 3) {
                            setQuestStage(player, Quests.THE_DIG_SITE, 4)
                        }
                        openInterface(player, 440)
                        sendString(player, player.username, 440, 5)
                    }
            }

        b
            .onPredicate { _ -> true }
            .playerl(FaceAnim.FRIENDLY, "Hello.")
            .npcl(
                FaceAnim.FRIENDLY,
                "Ah hello there! I am the resident lecturer on antiquities and artefacts. I also set the Earth Sciences exams.",
            ).playerl(FaceAnim.FRIENDLY, "Earth Sciences?")
            .npcl(
                FaceAnim.FRIENDLY,
                "That is right dear, the world of ${GameWorld.settings!!.name} holds many wonders beneath its surface. Students come to me to take exams so that they may join in on the archaeological dig going on just north of here.",
            ).playerl(FaceAnim.FRIENDLY, "So if they don't pass the exams they can't dig at all?")
            .npcl(
                FaceAnim.FRIENDLY,
                "That's right! We have to make sure that students know enough to be able to dig safely and not damage the artefacts.",
            ).options()
            .let { optionBuilder ->
                optionBuilder
                    .option_playerl("Can I take an exam?")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "You can if you get this letter stamped by the Curator of Varrock's museum.",
                    ).betweenStage { _, player, _, _ ->
                        addItemOrDrop(player, Items.UNSTAMPED_LETTER_682)
                    }.playerl(FaceAnim.FRIENDLY, "Why's that then?")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Because he is a very knowledgeable man and employs our archaeological expert. I'm sure he knows a lot about your exploits and can judge whether you'd make a good archaeologist or not.",
                    ).npcl(FaceAnim.FRIENDLY, "Besides, the museum contributes funds to the dig.")
                    .playerl(FaceAnim.FRIENDLY, "But why are you writing the letter? Shouldn't he?")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "He's also a very busy man, so I write the letters and he justs stamps them if he approves.",
                    ).playerl(
                        FaceAnim.FRIENDLY,
                        "Oh, I see. I'll ask him if he'll approve me, and bring my stamped letter back here. Thanks.",
                    ).endWith { _, player ->
                        if (getQuestStage(player, Quests.THE_DIG_SITE) == 0) {
                            setQuestStage(player, Quests.THE_DIG_SITE, 1)
                        }
                    }
                optionBuilder
                    .option_playerl("Interesting...")
                    .npcl(FaceAnim.FRIENDLY, "You could gain much with an understanding of the world below.")
                    .end()
            }
    }
}
