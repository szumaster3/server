package content.region.misthalin.quest.rag.dialogue

import content.region.misthalin.quest.rag.RagAndBoneMan
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class OddOldManDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.RAG_AND_BONE_MAN)) {
            0 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.FRIENDLY, "Can I help you with something?").also { stage++ }
                    1 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Well, err...who are you, and what are all these bones doing here?",
                        ).also { stage++ }

                    2 -> npcl(FaceAnim.FRIENDLY, "Errr...").also { stage++ }
                    3 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "I'm an archaeologist. I work with the museum.").also { stage++ }
                    5 -> playerl(FaceAnim.FRIENDLY, "An archaeologist?").also { stage++ }
                    6 -> npcl(FaceAnim.FRIENDLY, "Yes.").also { stage++ }
                    7 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Well that explains the bones...sort of...but what are you doing all the way out here?",
                        ).also {
                            stage++
                        }

                    8 -> npcl(FaceAnim.FRIENDLY, "Errr...").also { stage++ }
                    9 ->
                        npcl(
                            NPCs.BONES_3674,
                            "Sack",
                            FaceAnim.OLD_HAPPY,
                            "Mumblemumble. Mumblemumblemumble.",
                        ).also { stage++ }

                    10 -> npcl(FaceAnim.FRIENDLY, "I'm collecting bones for the museum.").also { stage++ }
                    11 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "They have asked me to rig up some displays of second and third age creatures using their bones, so that people can come and...well, look at them.",
                        ).also {
                            stage++
                        }

                    12 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I need to get them into some sort of order before I begin, but I've run into a bit of a snag.",
                        ).also {
                            stage++
                        }

                    13 -> playerl(FaceAnim.FRIENDLY, "What sort of a snag?").also { stage++ }
                    14 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, I need to have all the bones I'm going to use here, and placed into some sort of order.",
                        ).also {
                            stage++
                        }

                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "However, I seem to have discovered I am a few short.",
                        ).also { stage++ }

                    16 ->
                        showTopics(
                            Topic("Anything I can do to help?", 20, true),
                            Topic("Well, good luck with that.", 70),
                            Topic("Where is that mumbling coming from?", 80),
                        )

                    20 -> playerl(FaceAnim.FRIENDLY, "Anything I can do to help?").also { stage++ }
                    21 -> npcl(FaceAnim.FRIENDLY, "Errr...").also { stage++ }
                    22 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    23 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There is something you could do for me. I'm going to be busy...err...",
                        ).also { stage++ }

                    24 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumble").also { stage++ }
                    25 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Sorting, yes, sorting these bones out ready for the museum, but I need a few more.",
                        ).also {
                            stage++
                        }

                    26 -> npcl(FaceAnim.FRIENDLY, "Will you help me out by grabbing some?").also { stage++ }
                    27 ->
                        showTopics(
                            Topic(FaceAnim.FRIENDLY, "Yes", 28, true),
                            Topic(FaceAnim.FRIENDLY, "No", 60),
                            Topic("Where is that mumbling coming from?", 80),
                        )

                    28 -> playerl(FaceAnim.FRIENDLY, "Yes. I'll give you a hand.").also { stage++ }
                    29 -> npcl(FaceAnim.FRIENDLY, "You will? Excellent!").also { stage++ }
                    30 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Sniggersnigger").also { stage++ }
                    31 -> playerl(FaceAnim.FRIENDLY, "Where do you need me to dig?").also { stage++ }
                    32 -> npcl(FaceAnim.FRIENDLY, "Dig?").also { stage++ }
                    33 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    34 -> npcl(FaceAnim.FRIENDLY, "Oh, you must have got the wrong end of the stick.").also { stage++ }

                    35 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I need some fresh, whole bones to replace ones that have become damaged.",
                        ).also {
                            stage++
                        }

                    36 -> playerl(FaceAnim.FRIENDLY, "What?").also { stage++ }
                    37 -> npcl(FaceAnim.FRIENDLY, "Err...").also { stage++ }
                    38 ->
                        npcl(
                            NPCs.BONES_3674,
                            "Sack",
                            FaceAnim.OLD_HAPPY,
                            "Mumblemumblemumblemumblemumble. Mumblemumblemumblemumble. Mumble.",
                        ).also {
                            stage++
                        }

                    39 -> playerl(FaceAnim.FRIENDLY, "Excuse me...").also { stage++ }
                    40 -> npcl(FaceAnim.FRIENDLY, "Shhh!").also { stage++ }
                    41 -> playerl(FaceAnim.FRIENDLY, "Sorry...").also { stage++ }
                    42 ->
                        npcl(
                            NPCs.BONES_3674,
                            "Sack",
                            FaceAnim.OLD_HAPPY,
                            "Mumblemumble. Mumblemumblemumble.",
                        ).also { stage++ }

                    43 -> npcl(FaceAnim.FRIENDLY, "Ok, got it.").also { stage++ }
                    44 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ok, here is the thing. While sorting out what bones I do have I managed to lose or damage a few. If you can get me some fresh, unbroken bones to use as replacements then I can get on with things.",
                        ).also {
                            stage++
                        }

                    45 -> npcl(FaceAnim.FRIENDLY, "That make things clearer?").also { stage++ }
                    46 -> playerl(FaceAnim.FRIENDLY, "Well, it makes some sense I suppose...").also { stage++ }
                    47 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Great. If you can get me a bone from a Goblin, a Bear, a Big Frog, a Ram, a Unicorn, a Monkey, a Giant rat and a Giant Bat then I'll be able to move on with the...",
                        ).also {
                            stage++
                        }

                    48 -> npcl(FaceAnim.FRIENDLY, "Displays...").also { stage++ }
                    49 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "So you just want me to bring you these bones and that will be that?",
                        ).also { stage++ }

                    50 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, I wouldn't mind you sticking them in a pot and boiling them in vinegar first, if you don't mind.",
                        ).also {
                            stage++
                        }

                    51 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There is a Wine Merchant in Draynor called Fortunato that sells the stuff you'll need.",
                        ).also {
                            stage++
                        }

                    52 -> npcl(FaceAnim.FRIENDLY, "You can even use my pot-boiler if you want.").also { stage++ }
                    53 -> playerl(FaceAnim.FRIENDLY, "Why do I need to boil them in vinegar?").also { stage++ }
                    54 -> npcl(FaceAnim.FRIENDLY, "It gets them bright and sparking white.").also { stage++ }
                    55 -> npcl(FaceAnim.FRIENDLY, "It's an archaeologist thing.").also { stage++ }
                    56 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Just put the bone in a pot of vinegar, throw some logs on the fire, put the pot in the holder and light the logs.",
                        ).also {
                            stage++
                        }

                    57 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "It takes a while for the vinegar to evaporate, but the bone will be nice and clean in the end.",
                        ).also {
                            stage++
                        }

                    58 -> playerl(FaceAnim.FRIENDLY, "All right, I'll be back later.").also { stage++ }
                    59 ->
                        npcl(FaceAnim.FRIENDLY, "Bye!").also {
                            setQuestStage(player!!, Quests.RAG_AND_BONE_MAN, 1)
                            stage = END_DIALOGUE
                        }

                    60 -> npcl(FaceAnim.FRIENDLY, "Oh...I see.").also { stage++ }
                    61 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, never mind me young man, I'll just stagger over here under my massive burden, and continue my thankless task.",
                        ).also {
                            stage++
                        }

                    62 -> npcl(FaceAnim.FRIENDLY, "Unaided and alone...").also { stage++ }
                    63 -> playerl(FaceAnim.FRIENDLY, "Wow, trying a guilt trip much?").also { stage = END_DIALOGUE }
                    70 -> npcl(FaceAnim.FRIENDLY, "Thanks stranger!").also { stage++ }
                    71 -> npcl(FaceAnim.FRIENDLY, "What a polite young man...").also { stage++ }
                    72 -> npcl(FaceAnim.FRIENDLY, "Well, back to work!").also { stage = END_DIALOGUE }
                    80 -> npcl(FaceAnim.FRIENDLY, "Errr...").also { stage++ }
                    81 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    82 -> npcl(FaceAnim.FRIENDLY, "What mumbling?").also { stage++ }
                    83 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Anyway, I have enough problems of my own without dealing with a delusional adventurer.",
                        ).also { stage = 16 }
                }
            }

            in 1..3 -> {
                when (stage) {
                    START_DIALOGUE -> {
                        if (checkBonesInInventory(player!!)) {
                            playerl(FaceAnim.FRIENDLY, "I have some bones for you...").also { stage = 1 }
                        } else {
                            npcl(FaceAnim.FRIENDLY, "Have you brought me any bones?").also { stage = 20 }
                        }
                    }

                    1 -> npcl(FaceAnim.FRIENDLY, "Great! Let me take a look at them.").also { stage++ }

                    2 -> {
                        submitBonesInInventory(player!!)
                        if (hasAllBones(player!!)) {
                            npcl(FaceAnim.FRIENDLY, "That's the last of them!").also { stage = 4 }
                        } else {
                            npcl(FaceAnim.FRIENDLY, "Thanks the museum will be so pleased.").also { stage = 3 }
                        }
                    }

                    3 ->
                        npcl(FaceAnim.FRIENDLY, "Come and see me when you have the rest.").also {
                            stage = END_DIALOGUE
                        }

                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The museum will be thrilled to know I've completed the collection.",
                        ).also { stage++ }

                    5 -> playerl(FaceAnim.FRIENDLY, "Well I'm just glad I could help.").also { stage++ }
                    6 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "Well you've been a big help and no mistake.").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm always on the lookout for fresh bones, so if you see some bring them right over.",
                        ).also {
                            stage++
                        }

                    9 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "No problem, I'll be sure to bring anything you might like over if I find something.",
                        ).also {
                            stage++
                        }

                    10 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I can't wait to see the displays once they are finished.",
                        ).also { stage++ }

                    11 ->
                        finishQuest(player!!, Quests.RAG_AND_BONE_MAN).also {
                            end()
                        }

                    20 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Not at the moment. Can you just give me a run down on which bones I have left to get?",
                        ).also {
                            stage++
                        }

                    21 -> npcl(FaceAnim.FRIENDLY, "Sure.").also { stage = 30 }
                    30, 40, 50, 60, 70, 80, 90, 100, 110 -> {
                        if (!getAttribute(player!!, RagAndBoneMan.attributeGoblinBone, false) && stage <= 30) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 31
                        } else if (!getAttribute(player!!, RagAndBoneMan.attributeBearBone, false) && stage <= 40) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 41
                        } else if (!getAttribute(player!!, RagAndBoneMan.attributeBigFrogBone, false) && stage <= 50) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 51
                        } else if (!getAttribute(player!!, RagAndBoneMan.attributeRamBone, false) && stage <= 60) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 61
                        } else if (!getAttribute(player!!, RagAndBoneMan.attributeUnicornBone, false) && stage <= 70) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 71
                        } else if (!getAttribute(player!!, RagAndBoneMan.attributeMonkeyBone, false) && stage <= 80) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 81
                        } else if (!getAttribute(player!!, RagAndBoneMan.attributeGiantRatBone, false) && stage <= 90) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 91
                        } else if (!getAttribute(
                                player!!,
                                RagAndBoneMan.attributeGiantBatBone,
                                false,
                            ) &&
                            stage <= 100
                        ) {
                            npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble")
                            stage = 101
                        } else {
                            npcl(FaceAnim.FRIENDLY, "Did you get all that?")
                            stage = 111
                        }
                    }

                    31 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Goblin bone.").also { stage++ }
                    32 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Goblins are relatively common. I hear there is a house full of them around Lumbridge in fact.",
                        ).also { stage = 40 }

                    41 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Bear bone.").also { stage++ }
                    42 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I heard that there are some Bears over by the Legends' Guild, near Ardougne.",
                        ).also { stage = 50 }

                    51 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Big Frog bone.").also { stage++ }
                    52 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "This might be a little tricky as you will need to go into the Lumbridge Swamp caves. You will need a light source! Never forget your light source down there!",
                        ).also { stage = 60 }

                    61 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Ram bone.").also { stage++ }
                    62 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm sure you will be able to find a ram wherever there are sheep.",
                        ).also { stage = 70 }

                    71 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Unicorn bone.").also { stage++ }
                    72 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I seem to remember that there were Unicorns south of Varrock, I think they might be there still.",
                        ).also { stage = 80 }

                    81 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Monkey bone.").also { stage++ }
                    82 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Monkeys tend to live in Jungle areas, like Karamja. I think they are pretty plentiful there if I remember correctly.",
                        ).also { stage = 90 }

                    91 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Giant Rat bone.").also { stage++ }
                    92 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "If you can't find one in a sewer, then you might want to try looking in some caves.",
                        ).also { stage = 100 }

                    101 -> npcl(FaceAnim.FRIENDLY, "You still need to bring me a Giant Bat bone.").also { stage++ }
                    102 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Giant bats tend to live underground, but I have heard there are a few near the Coal Pits.",
                        ).also { stage = 110 }

                    111 -> playerl(FaceAnim.FRIENDLY, "Yes. I'll get right on it.").also { stage++ }
                    112 -> npcl(NPCs.BONES_3674, "Sack", FaceAnim.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    113 -> npcl(FaceAnim.FRIENDLY, "Don't forget to boil them in vinegar first.").also { stage++ }
                    114 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Just chuck some logs into the pit, put the bone in the pot of vinegar and drop it onto the pot-boiler. Then light the logs and wait for the the vinegar to boil away.",
                        ).also { stage++ }

                    115 -> playerl(FaceAnim.FRIENDLY, "Ok, I'll remember.").also { stage = END_DIALOGUE }
                }
            }
        }
    }

    private fun checkBonesInInventory(player: Player): Boolean {
        var hasBone = false
        RagAndBoneMan.requiredBonesMap.forEach {
            if (inInventory(player, it.key)) {
                hasBone = true
            }
        }
        return hasBone
    }

    private fun submitBonesInInventory(player: Player) {
        RagAndBoneMan.requiredBonesMap.forEach {
            if (!getAttribute(player, it.value, false) && removeItem(player, it.key)) {
                setAttribute(player, it.value, true)
            }
        }
    }

    private fun hasAllBones(player: Player): Boolean {
        var hasBoneAllBones = true
        RagAndBoneMan.requiredBonesMap.values.forEach {
            if (!getAttribute(player, it, false)) {
                hasBoneAllBones = false
            }
        }
        return hasBoneAllBones
    }
}
