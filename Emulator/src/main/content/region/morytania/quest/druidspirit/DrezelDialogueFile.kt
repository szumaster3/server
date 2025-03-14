package content.region.morytania.quest.druidspirit

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class DrezelDialogueFile : DialogueFile() {
    var questStage = 0

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        questStage = getQuestStage(player!!, Quests.NATURE_SPIRIT)
        if (questStage <= 5) {
            when (stage) {
                0 -> options("Sorry, not interested...", "Well, what is it, I may be able to help?").also { stage++ }
                1 ->
                    when (buttonID) {
                        1 -> playerl(FaceAnim.NEUTRAL, "Sorry, not interested.").also { stage = END_DIALOGUE }
                        2 -> playerl(FaceAnim.FRIENDLY, "Well, what is it, I may be able to help?").also { stage++ }
                    }

                2 ->
                    npcl(
                        FaceAnim.HALF_THINKING,
                        "There's a man called Filliman who lives in Mort Myre, I wonder if you could look for him? The swamps of Mort Myre are dangerous though, they're infested with Ghasts!",
                    ).also {
                        stage++
                    }

                3 ->
                    options(
                        "Who is this Filliman?",
                        "Where's Mort Myre?",
                        "What's a Ghast?",
                        "Yes, I'll go and look for him.",
                        "Sorry, I don't think I can help.",
                    ).also {
                        stage++
                    }

                4 ->
                    when (buttonID) {
                        1 ->
                            npcl(
                                FaceAnim.NEUTRAL,
                                "Filliman Tarlock is his full name and he's a Druid. He lives in Mort Myre much like a hermit, but there's many a traveller who he's helped.",
                            ).also {
                                stage--
                            }
                        2 ->
                            npcl(
                                FaceAnim.NEUTRAL,
                                "Mort Myre is a decayed and dangerous swamp to the south. It was once a beautiful forest but has since become filled with vile emanations from within Morytania.",
                            ).also {
                                stage =
                                    6
                            }
                        3 ->
                            npcl(
                                FaceAnim.NEUTRAL,
                                "A Ghast is a poor soul who died in Mort Myre. They're undead of a special class, they're untouchable as far as I'm aware!",
                            ).also {
                                stage =
                                    5
                            }
                        4 -> playerl(FaceAnim.FRIENDLY, "Yes, I'll go and look for him.").also { stage = 10 }
                        5 -> playerl(FaceAnim.NEUTRAL, "Sorry, I don't think I can help.").also { stage = END_DIALOGUE }
                    }
                5 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Filliman knew how to tackle them, but I've not heard from him in a long time. Ghasts, when they attack, will devour any food you have. If you have no food, they'll draw their nourishment from you!",
                    ).also {
                        stage =
                            3
                    }
                6 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        " We put a fence around it to stop unwary travellers going in. Anyone who dies in the swamp is forever cursed to haunt it as a Ghast. Ghasts attack travellers, turning food to rotten filth.",
                    ).also {
                        stage =
                            3
                    }
                10 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "That's great, but it is very dangerous. Are you sure you want to do this?",
                    ).also {
                        stage++
                    }
                11 -> options("Yes, I'm sure.", "Sorry, I don't think I can help.").also { stage++ }
                12 ->
                    when (buttonID) {
                        1 -> playerl(FaceAnim.FRIENDLY, "Yes, I'm sure.").also { stage = 20 }
                        2 -> playerl(FaceAnim.NEUTRAL, "Sorry, I don't think I can help.").also { stage = END_DIALOGUE }
                    }

                20 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "That's great! Many Thanks! Now then, please be aware of the Ghasts, you cannot attack them, only Filliman knew how to take them on.",
                    ).also {
                        stage++
                    }

                21 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Just run from them if you can. If you start to get lost, try to make your way back to the temple.",
                    ).also {
                        stage++
                    }

                22 -> {
                    sendDoubleItemDialogue(
                        player!!,
                        Items.MEAT_PIE_2327,
                        Items.APPLE_PIE_2323,
                        "The cleric hands you some food.",
                    )
                    if (questStage == 0) {
                        repeat(3) { addItemOrDrop(player!!, Items.MEAT_PIE_2327, 1) }
                        repeat(3) { addItemOrDrop(player!!, Items.APPLE_PIE_2323, 1) }
                        player!!.questRepository.getQuest(Quests.NATURE_SPIRIT).setStage(player!!, 5)
                    }
                    stage++
                }
                23 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Please take this food to Filliman, he'll probably appreciate a bit of cooked food. Now, he's never revealed where he lives in the swamps but I guess he'd be to the south, search for him won't you?",
                    ).also {
                        stage++
                    }
                24 ->
                    playerl(
                        FaceAnim.FRIENDLY,
                        "I'll do my very best, don't worry, if he's in there and he's still alive I'll definitely find him.",
                    ).also {
                        stage = END_DIALOGUE
                        player!!.questRepository.getQuest(Quests.NATURE_SPIRIT).start(player!!)
                    }
            }
        } else if (questStage == 15) {
            when (stage) {
                0 ->
                    playerl(
                        FaceAnim.HALF_GUILTY,
                        "I've found Filliman and you should prepare for some sad news.",
                    ).also { stage++ }

                1 -> npcl(FaceAnim.HALF_GUILTY, "You mean... he's dead?").also { stage++ }
                2 ->
                    playerl(
                        FaceAnim.NEUTRAL,
                        "Well, er sort of. I got to his camp and I encountered a spirit of some kind. I don't think it was a Ghast, it tried to communicate with me, but made no sense, it was all 'ooooh' this and 'oooh' that.",
                    ).also {
                        stage++
                    }

                3 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Hmmm, that's very interesting, I seem to remember Father Aereck in Lumbridge and his predecessor Father Urhney having a similar issue. Though this is probably not related to your problem.",
                    ).also {
                        stage++
                    }
                4 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        " I will pray that it wasn't the spirit of my friend Filliman, but some lost soul who needs some help. Please do let me know how you get on with it.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        } else if (questStage == 35) {
            when (stage) {
                0 ->
                    playerl(
                        FaceAnim.FRIENDLY,
                        "Hello again! I'm helping Filliman, he plans to become a nature spirit. I have a spell to cast but first I need to be blessed. Can you bless me?",
                    ).also {
                        stage++
                    }
                1 -> npcl(FaceAnim.NEUTRAL, "But you haven't sneezed!").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "You're so funny! But can you bless me?").also { stage++ }
                3 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Very well my friend, prepare yourself for the blessings of Saradomin. Here we go!",
                    ).also {
                        stage++
                    }
                4 -> {
                    end()
                    player!!.lock()
                    submitIndividualPulse(player!!, BlessingPulse(npc!!, player!!))
                }
            }
        } else if (questStage == 40) {
            npcl(
                FaceAnim.NEUTRAL,
                "There you go my friend, you're now blessed. It's funny, now I look at you, there seems to be something of the faith about you. Anyway, good luck with your quest!",
            ).also {
                stage = END_DIALOGUE
                player!!.questRepository.getQuest(Quests.NATURE_SPIRIT).setStage(player!!, 45)
            }
        } else if (questStage == 100) {
            when (stage) {
                0 ->
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Greetings again adventurer. How go your travels in Morytania? Is it as evil as I have heard?",
                    ).also {
                        stage++
                    }

                1 -> playerl(FaceAnim.FRIENDLY, "I've lost my wolfbane dagger.").also { stage++ }
                2 ->
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Yes, I know! Luckily for you it washed up on the banks of the Salve earlier!",
                    ).also {
                        stage++
                    }

                3 ->
                    if (freeSlots(player!!) == 0) {
                        end()
                        sendDialogue(player!!, "You don't have enough inventory space.")
                    } else {
                        npcl(FaceAnim.FRIENDLY, "Here, take it again, but please try and be more careful this time.")
                        addItem(player!!, Items.WOLFBANE_2952)
                        stage = 4
                    }

                4 -> npcl(FaceAnim.FRIENDLY, "It's a family heirloom after all!").also { stage++ }
                5 -> playerl(FaceAnim.FRIENDLY, "Thanks for that!").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Hello, friend, how goes your quest with Filliman?").also { stage++ }
                1 -> playerl(FaceAnim.NEUTRAL, "Still working at it.").also { stage++ }
                2 ->
                    npcl(FaceAnim.NEUTRAL, "Well enough! Do let me know when something develops!").also {
                        stage = END_DIALOGUE
                    }
            }
        }
    }
}
