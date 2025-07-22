package content.region.misthalin.digsite.quest.itexam.dialogue

import content.region.misthalin.digsite.quest.itexam.TheDigSite
import core.api.*
import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class StudentGreenDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StudentGreenDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.STUDENT_615)
}

class StudentGreenDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.THE_DIG_SITE, 6, 7, 8, 9, 10, 11, 12, 13, 100)
            .npcl(
                FaceAnim.FRIENDLY,
                " Oh, hi again. News of your find has spread fast; you are quite famous around here now.",
            )

        b
            .onQuestStages(Quests.THE_DIG_SITE, 5)
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "I need more help with the exam.")
            .npcl(FaceAnim.FRIENDLY, "Well, okay, this is what I have learned since I last spoke to you...")
            .npcl(FaceAnim.FRIENDLY, "Specimen brush use: Brush carefully and slowly using short strokes.")
            .playerl(FaceAnim.FRIENDLY, "Okay, I'll remember that. Thanks for all your help.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrGreenExam3ObtainAnswer, true)
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 4)
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "I need more help with the exam.")
            .npcl(FaceAnim.FRIENDLY, "Well, okay, this is what I have learned since I last spoke to you...")
            .npcl(
                FaceAnim.FRIENDLY,
                "Correct rock pick usage: Always handle with care; strike the rock cleanly on its cleaving point.",
            ).playerl(FaceAnim.FRIENDLY, "Okay, I'll remember that.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrGreenExam2ObtainAnswer, true)
            }
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 3 &&
                    getAttribute(
                        player,
                        TheDigSite.attrGreenExam1ObtainAnswer,
                        false,
                    )
            }.playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "I need more help with the exam.")
            .npcl(FaceAnim.FRIENDLY, "Well, okay, this is what I have learned since I last spoke to you...")
            .npcl(
                FaceAnim.FRIENDLY,
                "The study of Earth Sciences is: The study of the earth, its contents and history.",
            ).playerl(FaceAnim.FRIENDLY, "Okay, I'll remember that.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrGreenExam1ObtainAnswer, true)
            }
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 3 &&
                    getAttribute(
                        player,
                        TheDigSite.attrGreenExam1Talked,
                        false,
                    )
            }.branch { player ->
                return@branch if (inInventory(player, Items.ANIMAL_SKULL_671)) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(0)
                    .playerl(FaceAnim.FRIENDLY, "Hello there. How's the study going?")
                    .npcl(FaceAnim.FRIENDLY, "Very well, thanks. Have you found my animal skull yet?")
                    .playerl(FaceAnim.FRIENDLY, "No, sorry, not yet.")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Oh well, I am sure it's been picked up. Couldn't you try looking through some pockets?",
                    ).end()
                branch
                    .onValue(1)
                    .playerl(FaceAnim.FRIENDLY, "Hello there. How's the study going?")
                    .npcl(FaceAnim.FRIENDLY, "Very well, thanks. Have you found my animal skull yet?")
                    .betweenStage { _, player, _, _ ->
                        if (inInventory(player, Items.ANIMAL_SKULL_671)) {
                            removeItem(player, Items.ANIMAL_SKULL_671)
                        }
                    }.npcl(
                        "Oh wow! You've found it! Thank you so much. I'll be glad to tell you what I know about the exam. The study of Earth Sciences is: The study of the earth, its contents and history.",
                    ).endWith { _, player ->
                        setAttribute(player, TheDigSite.attrGreenExam1ObtainAnswer, true)
                    }
                return@let branch
            }
        b
            .onQuestStages(Quests.THE_DIG_SITE, 3)
            .playerl(FaceAnim.FRIENDLY, "Hello there. Can you help me with the Earth Sciences exams at all?")
            .npcl(FaceAnim.FRIENDLY, "Well... Maybe I will if you help me with something.")
            .playerl(FaceAnim.FRIENDLY, "What's that?")
            .npcl(FaceAnim.FRIENDLY, "I have lost my recent good find.")
            .playerl(FaceAnim.FRIENDLY, "What does it look like?")
            .npcl(FaceAnim.FRIENDLY, "Err... Like an animal skull!")
            .playerl(
                FaceAnim.FRIENDLY,
                "Well, that's not too helpful, there are lots of those around here. Can you remember where you last had it?",
            ).npcl(FaceAnim.FRIENDLY, "It was around here for sure. Maybe one of the workmen picked it up?")
            .playerl(FaceAnim.FRIENDLY, "Okay, I'll have a look for you.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrGreenExam1Talked, true)
            }
        b
            .onPredicate { _ -> true }
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "Oh, hi. I'm studying hard for an exam.")
            .playerl(FaceAnim.FRIENDLY, "What exam is that?")
            .npcl(FaceAnim.FRIENDLY, "It's the Earth Sciences exam.")
            .playerl(FaceAnim.FRIENDLY, "Interesting....")
            .end()
    }
}

@Initializable
class StudentPurpleDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StudentPurpleDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.STUDENT_617)
}

class StudentPurpleDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 5 &&
                    getAttribute(
                        player,
                        TheDigSite.attrPurpleExam3ObtainAnswer,
                        false,
                    )
            }.playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "I am stuck on some more exam questions.")
            .npcl(FaceAnim.FRIENDLY, "Okay, I'll tell you my latest notes...")
            .npcl(FaceAnim.FRIENDLY, "Sample preparation: Samples cleaned, and carried only in specimen jars.")
            .playerl(FaceAnim.FRIENDLY, "Great, thanks for your advice.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrPurpleExam3ObtainAnswer, true)
            }

        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 5 &&
                    getAttribute(
                        player,
                        TheDigSite.attrPurpleExam3Talked,
                        false,
                    )
            }.branch { player ->
                return@branch if (inInventory(player, Items.OPAL_1609) || inInventory(player, Items.UNCUT_OPAL_1625)) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(0)
                    .playerl(FaceAnim.FRIENDLY, "Hello there.")
                    .npcl(FaceAnim.FRIENDLY, "Oh, hi again. Did you bring me the opal?")
                    .playerl(FaceAnim.FRIENDLY, "I haven't found one yet.")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Oh, well, tell me when you do. Remember that they can be found around the site; perhaps try panning the river.",
                    ).end()
                branch
                    .onValue(1)
                    .playerl(FaceAnim.FRIENDLY, "Hello there.")
                    .npcl(FaceAnim.FRIENDLY, "Oh, hi again. Did you bring me the opal?")
                    .betweenStage { _, player, _, _ ->
                        if (inInventory(player, Items.UNCUT_OPAL_1625)) {
                            removeItem(player, Items.UNCUT_OPAL_1625)
                            return@betweenStage
                        }
                        if (inInventory(player, Items.OPAL_1609)) {
                            removeItem(player, Items.OPAL_1609)
                            return@betweenStage
                        }
                    }.playerl(FaceAnim.FRIENDLY, "Would an opal look like this by any chance?")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Wow, great, you've found one. This will look beautiful set in my necklace. Thanks for that; now I'll tell you what I know... Sample preparation: Samples cleaned, and carried only in specimen jars.",
                    ).playerl(FaceAnim.FRIENDLY, "Great, thanks for your advice.")
                    .endWith { _, player ->
                        setAttribute(player, TheDigSite.attrPurpleExam3ObtainAnswer, true)
                    }
                return@let branch
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 5)
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "What, you want more help?")
            .playerl(FaceAnim.FRIENDLY, "Err... Yes please!")
            .npcl(FaceAnim.FRIENDLY, "Well... it's going to cost you...")
            .playerl(FaceAnim.FRIENDLY, "Oh, well how much?")
            .npcl(
                FaceAnim.FRIENDLY,
                "I'll tell you what I would like: a precious stone. I don't find many of them. My favorite are opals; they are beautiful. Just like me! Tee hee hee!",
            ).playerl(FaceAnim.FRIENDLY, "Err... OK I'll see what I can do, but I'm not sure where I'd get one.")
            .npcl(FaceAnim.FRIENDLY, "Well, I have seen people get them from panning occasionally.")
            .playerl(FaceAnim.FRIENDLY, "OK, I'll see what I can turn up for you.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrPurpleExam3Talked, true)
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 4)
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "I am stuck on some more exam questions.")
            .npcl(FaceAnim.FRIENDLY, "Okay, I'll tell you my latest notes...")
            .npcl(FaceAnim.FRIENDLY, "Finds handling: Finds must be carefully handled.")
            .playerl(FaceAnim.FRIENDLY, "Great, thanks for your advice.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrPurpleExam2ObtainAnswer, true)
            }
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 3 &&
                    getAttribute(
                        player,
                        TheDigSite.attrPurpleExam1ObtainAnswer,
                        false,
                    )
            }.playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "I am stuck on some more exam questions.")
            .npcl(FaceAnim.FRIENDLY, "Okay, I'll tell you my latest notes...")
            .npcl(
                FaceAnim.FRIENDLY,
                "The proper health and safety points are: Leather gloves and boots to be warn at all times; proper tools must be used.",
            ).playerl(FaceAnim.FRIENDLY, "Great, thanks for your advice.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrPurpleExam1ObtainAnswer, true)
            }
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 3 &&
                    getAttribute(
                        player,
                        TheDigSite.attrPurpleExam1Talked,
                        false,
                    )
            }.branch { player ->
                return@branch if (inInventory(player, Items.TEDDY_673)) {
                    1
                } else {
                    0
                }
            }.let { branch ->
                branch
                    .onValue(0)
                    .npcl(FaceAnim.FRIENDLY, "Very well thanks. Have you found my lucky mascot yet?")
                    .playerl(FaceAnim.FRIENDLY, "No sorry, not yet.")
                    .npcl(FaceAnim.FRIENDLY, "I'm sure it's just outside the site somewhere...")
                    .end()
                branch
                    .onValue(1)
                    .playerl(FaceAnim.FRIENDLY, "Hello there.")
                    .betweenStage { _, player, _, _ ->
                        if (inInventory(player, Items.TEDDY_673)) {
                            removeItem(player, Items.TEDDY_673)
                        }
                    }.playerl(FaceAnim.FRIENDLY, "Guess what I found.")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Hey! My lucky mascot! Thanks ever so much. Let me help you with those questions now.",
                    ).npcl(
                        FaceAnim.FRIENDLY,
                        "The proper health and safety points are: Leather gloves and boots to be warn at all times; proper tools must be used.",
                    ).playerl(FaceAnim.FRIENDLY, "Great, thanks for your advice.")
                    .endWith { _, player ->
                        setAttribute(player, TheDigSite.attrPurpleExam1ObtainAnswer, true)
                    }
                return@let branch
            }
        b
            .onQuestStages(Quests.THE_DIG_SITE, 3)
            .playerl(FaceAnim.FRIENDLY, "Hello there. Can you help me with the Earth Sciences exams at all?")
            .npcl(FaceAnim.FRIENDLY, "I can if you help me...")
            .playerl(FaceAnim.FRIENDLY, "How can I do that?")
            .npcl(FaceAnim.FRIENDLY, "I have lost my teddy bear. He was my lucky mascot.")
            .playerl(FaceAnim.FRIENDLY, "Do you know where you dropped him?")
            .npcl(
                FaceAnim.FRIENDLY,
                "Well, I was doing a lot of walking that day... Oh yes, that's right - a few of us were studying that funny looking relic in the centre of the campus. Maybe I lost my lucky mascot around there, perhaps in a bush?",
            ).playerl(FaceAnim.FRIENDLY, "Leave it to me, I'll find it.")
            .npcl(FaceAnim.FRIENDLY, "Oh, great! Thanks!")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrPurpleExam1Talked, true)
            }
        b
            .onPredicate { _ -> true }
            .playerl("Hello there.")
            .npcl("Hi there. I'm studying for the Earth Sciences exam.")
            .playerl("Interesting... This exam seems to be a popular one!")
    }
}

@Initializable
class StudentBrownDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StudentBrownDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): Dialogue = StudentBrownDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.STUDENT_616)
}

class StudentBrownDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onQuestStages(Quests.THE_DIG_SITE, 5)
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "There are more exam questions I'm stuck on.")
            .npcl(FaceAnim.FRIENDLY, "Hey, I'll tell you what I've learned. That may help.")
            .npcl(
                FaceAnim.FRIENDLY,
                "The proper technique for handling bones is: Handle bones carefully and keep them away from other samples.",
            ).playerl(FaceAnim.FRIENDLY, "Thanks for the information.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrBrownExam3ObtainAnswer, true)
            }

        b
            .onQuestStages(Quests.THE_DIG_SITE, 4)
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "There are more exam questions I'm stuck on.")
            .npcl(FaceAnim.FRIENDLY, "Hey, I'll tell you what I've learned. That may help.")
            .npcl(
                FaceAnim.FRIENDLY,
                "Correct sample transportation: Samples taken in rough form; kept only in sealed containers.",
            ).playerl(FaceAnim.FRIENDLY, "Thanks for the information.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrBrownExam2ObtainAnswer, true)
            }
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 3 &&
                    getAttribute(
                        player,
                        TheDigSite.attrBrownExam1ObtainAnswer,
                        false,
                    )
            }.playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "How's it going?")
            .playerl(FaceAnim.FRIENDLY, "There are more exam questions I'm stuck on.")
            .npcl(FaceAnim.FRIENDLY, "Hey, I'll tell you what I've learned. That may help.")
            .npcl(
                FaceAnim.FRIENDLY,
                "Correct sample transportation: Samples taken in rough form; kept only in sealed containers.",
            ).playerl(FaceAnim.FRIENDLY, "Thanks for the information.")
            .endWith { _, player ->
                setAttribute(player, TheDigSite.attrBrownExam1ObtainAnswer, true)
            }
        b
            .onPredicate { player ->
                getQuestStage(player, Quests.THE_DIG_SITE) == 3 &&
                    getAttribute(
                        player,
                        TheDigSite.attrBrownExam1Talked,
                        false,
                    )
            }.playerl(FaceAnim.FRIENDLY, "Hello there. How's the study going?")
            .npcl(FaceAnim.FRIENDLY, "I'm getting there. Have you found my special cup yet?")
            .branch { player ->
                if (inInventory(player, Items.SPECIAL_CUP_672)) {
                    removeItem(player, Items.SPECIAL_CUP_672)
                    return@branch 1
                } else {
                    return@branch 0
                }
            }.let { branch ->
                branch
                    .onValue(0)
                    .playerl(FaceAnim.FRIENDLY, "No, sorry, not yet.")
                    .npcl(
                        FaceAnim.FRIENDLY,
                        "Oh dear, I hope it didn't fall into the river. I might never find it again.",
                    ).end()
                branch
                    .onValue(1)
                    .npcl(
                        "Oh wow! You've found it! Thank you so much. I'll be glad to tell you what I know about the exam. The study of Earth Sciences is: The study of the earth, its contents and history.",
                    ).endWith { _, player ->
                        setAttribute(player, TheDigSite.attrBrownExam1ObtainAnswer, true)
                    }
                return@let branch
            }
        b
            .onQuestStages(Quests.THE_DIG_SITE, 3)
            .playerl(FaceAnim.FRIENDLY, "Hello there. Can you help me with the Earth Sciences exams at all?")
            .npcl(FaceAnim.FRIENDLY, "I can't do anything unless I find my special cup.")
            .playerl(FaceAnim.FRIENDLY, "Your what?")
            .npcl(FaceAnim.FRIENDLY, "My special cup. I won it for a particularly good find last month.")
            .playerl(FaceAnim.FRIENDLY, "Oh, right. So if I find it, you'll help me?")
            .npcl(FaceAnim.FRIENDLY, "I sure will!")
            .playerl(FaceAnim.FRIENDLY, "Any ideas where it may be?")
            .npcl(
                FaceAnim.FRIENDLY,
                "All I remember is that I was working near the panning area when I lost it.",
            ).playerl(FaceAnim.FRIENDLY, "Okay, I'll see what I can do.")
            .npcl(
                FaceAnim.FRIENDLY,
                "Yeah, maybe the panning guide saw it? I hope I didn't lose it in the water!",
            ).endWith { _, player ->
                setAttribute(player, TheDigSite.attrBrownExam1Talked, true)
            }
        b
            .onPredicate { _ -> true }
            .playerl(FaceAnim.FRIENDLY, "Hello there.")
            .npcl(FaceAnim.FRIENDLY, "Hello there. As you can see, I am a student.")
            .playerl(FaceAnim.FRIENDLY, "What are you doing here?")
            .npcl(FaceAnim.FRIENDLY, "I'm studying for the Earth Sciences exam.")
            .playerl(FaceAnim.FRIENDLY, "Interesting... Perhaps I should study for it as well.")
    }
}
