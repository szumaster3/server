package content.region.kandarin.quest.drunkmonk.dialogue

import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.quest.updateQuestTab
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class BrotherOmadDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.MONKS_FRIEND)
        when (questStage) {
            0 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello there. What's wrong?").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.SAD,
                            "*yawn*...oh, hello...*yawn* I'm sorry! I'm just so tired! I haven't slept in a week!",
                        ).also { stage++ }

                    2 ->
                        options(
                            "Why can't you sleep, what's wrong?",
                            "Sorry! I'm too busy to hear your problems!",
                        ).also { stage++ }

                    3 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "Why can't you sleep, what's wrong?").also { stage++ }
                            2 ->
                                playerl(FaceAnim.FRIENDLY, "Sorry! I'm too busy to hear your problems!").also {
                                    stage = END_DIALOGUE
                                }
                        }

                    4 ->
                        npcl(
                            FaceAnim.SAD,
                            "It's brother Androe's son! With his constant: Waaaaaah! Waaaaaaaaah! Androe said it's natural, but it's so annoying!",
                        ).also { stage++ }

                    5 -> playerl(FaceAnim.NEUTRAL, "I suppose that's what kids do.").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.SAD,
                            "He was fine, up until last week! Thieves broke in! They stole his favourite sleeping blanket!",
                        ).also { stage++ }

                    7 ->
                        npcl(
                            FaceAnim.SAD,
                            "Now he won't rest until it's returned... ...and that, means neither can I!",
                        ).also { stage++ }

                    8 ->
                        options(
                            "Can I help at all?",
                            "I'm sorry to hear that! I hope you find his blanket.",
                        ).also { stage++ }

                    9 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "Can I help at all?").also { stage++ }
                            2 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "I'm sorry to hear that! I hope you find his blanket.",
                                ).also { stage = END_DIALOGUE }
                        }

                    10 ->
                        npcl(
                            FaceAnim.HALF_WORRIED,
                            "Please do. We won't be able to help you as we are peaceful men but we would be grateful for your help!",
                        ).also { stage++ }

                    11 -> playerl(FaceAnim.HALF_ASKING, "Where are they?").also { stage++ }
                    12 ->
                        npcl(
                            FaceAnim.SAD,
                            "They hide in a secret cave in the forest. It's hidden under a ring of stones. Please, bring back the blanket!",
                        ).also {
                            player!!.questRepository.getQuest(Quests.MONKS_FRIEND).start(player)
                            updateQuestTab(player!!)
                            stage = END_DIALOGUE
                        }
                }
            }

            10 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello.").also { stage = 20 }
                    20 -> npcl(FaceAnim.SAD, "*yawn*...oh, hello again...*yawn*").also { stage++ }
                    21 -> npcl(FaceAnim.SAD, "Please tell me you have the blanket.").also { stage++ }
                    22 ->
                        if (player!!.inventory.containItems(Items.CHILDS_BLANKET_90)) {
                            playerl(
                                FaceAnim.HAPPY,
                                "Yes! I've recovered it from the clutches of the evil thieves!",
                            ).also { stage = 30 }
                        } else {
                            playerl(FaceAnim.SAD, "I'm afraid not.").also { stage++ }
                        }

                    23 -> npcl(FaceAnim.SAD, "I need some sleep!").also { stage = END_DIALOGUE }
                    30 -> {
                        sendItemDialogue(player!!, Items.CHILDS_BLANKET_90, "You hand the monk the childs blanket.")
                        stage = 31
                    }

                    31 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "Really, that's excellent, well done! Maybe now I will be able to get some rest.",
                        ).also { stage++ }

                    32 ->
                        npcl(FaceAnim.SAD, "*yawn*..I'm off to bed! Farewell brave traveller!").also {
                            player!!.inventory.remove(Item(Items.CHILDS_BLANKET_90))
                            setQuestStage(player!!, Quests.MONKS_FRIEND, 20)
                            stage = END_DIALOGUE
                        }
                }
            }

            20 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello, how are you?").also { stage = 30 }
                    30 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "Much better now I'm sleeping well! Now I can organise the party.",
                        ).also { stage++ }

                    31 -> playerl(FaceAnim.HAPPY, "Ooh! What party?").also { stage++ }
                    32 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "The son of Brother Androe's birthday party. He's going to be one year old!",
                        ).also { stage++ }

                    33 -> playerl(FaceAnim.HAPPY, "That's sweet!").also { stage++ }
                    34 -> npcl(FaceAnim.HAPPY, "It's also a great excuse for a drink!").also { stage++ }
                    35 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "We just need Brother Cedric to return with the wine.",
                        ).also { stage++ }

                    36 -> options("Who's Brother Cedric?", "Enjoy it! I'll see you soon!").also { stage++ }
                    37 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "Who's Brother Cedric?").also { stage++ }
                            2 -> playerl(FaceAnim.FRIENDLY, "Enjoy it! I'll see you soon!").also { stage = 998 }
                        }

                    998 -> npcl(FaceAnim.NEUTRAL, "Take care traveller.").also { stage = END_DIALOGUE }
                    38 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Cedric is a member of the order too. We sent him out three days ago to collect wine. But he didn't return!",
                        ).also { stage++ }

                    39 -> npcl(FaceAnim.NEUTRAL, "He most probably got drunk and lost in the forest!").also { stage++ }
                    40 ->
                        options(
                            "I've no time for that, sorry.",
                            "Where should I look?",
                            "Can I come to the party?",
                        ).also { stage++ }

                    41 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.NEUTRAL, "I've no time for that, sorry.").also { stage = 996 }
                            2 -> playerl(FaceAnim.HALF_ASKING, "Where should I look?").also { stage++ }
                            3 -> playerl(FaceAnim.HALF_ASKING, "Can I come to the party?").also { stage = 997 }
                        }

                    996 -> npcl(FaceAnim.NEUTRAL, "Okay traveller, take care.").also { stage = END_DIALOGUE }
                    997 ->
                        npcl(FaceAnim.NEUTRAL, "Of course, but we need the wine first.").also {
                            stage = END_DIALOGUE
                        }

                    42 -> npcl(FaceAnim.FRIENDLY, "Oh, he won't be far. Probably out in the forest.").also { stage++ }
                    43 ->
                        playerl(FaceAnim.FRIENDLY, "Ok, I'll go and find him.").also {
                            setQuestStage(player!!, Quests.MONKS_FRIEND, 30)
                            stage = END_DIALOGUE
                        }
                }
            }

            30 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello brother Omad.").also { stage = 50 }
                    50 -> npcl(FaceAnim.NEUTRAL, "Hello adventurer, have you found Brother Cedric?").also { stage++ }
                    51 -> playerl(FaceAnim.SAD, "Not yet.").also { stage++ }
                    52 ->
                        npcl(FaceAnim.FRIENDLY, "Well, keep looking, we need that wine!").also {
                            stage = END_DIALOGUE
                        }
                }
            }

            40 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.HAPPY, "Hello adventurer, have you found Brother Cedric?").also { stage = 60 }
                    60 -> playerl(FaceAnim.NEUTRAL, "Yes I've seen him, he's a bit drunk!").also { stage++ }
                    61 ->
                        npcl(FaceAnim.FRIENDLY, "Well, try your best to get him back here!").also {
                            stage = END_DIALOGUE
                        }
                }
            }

            41, 42 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello again brother Omad.").also { stage = 70 }
                    70 -> npcl(FaceAnim.NEUTRAL, "Hello adventurer, where's Brother Cedric?").also { stage++ }
                    71 -> npcl(FaceAnim.NEUTRAL, "He's having a bit of trouble with his cart.").also { stage++ }
                    72 -> npcl(FaceAnim.NEUTRAL, "Hmmm! Maybe you could help?").also { stage = END_DIALOGUE }
                }
            }

            50 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hi Omad, Brother Cedric is on his way!").also { stage = 80 }
                    80 -> npcl(FaceAnim.HAPPY, "Good! Good! Now we can party!").also { stage++ }
                    81 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "I have little to repay you with, but I'd like to offer you some rune stones. But first, let's party!",
                        ).also { stage++ }

                    82 -> sendDialogue(player!!, "Brother Omad gives you 8 Law Runes").also { stage++ }
                    83 -> playerl(FaceAnim.HAPPY, "Thanks Brother Omad!").also { stage++ }
                    84 -> {
                        commenceMonkParty(true)
                        end()
                    }
                }
            }

            100 -> {
                commenceMonkParty(false)
            }
        }
    }

    private fun commenceMonkParty(questComplete: Boolean) {
        val brotherOmad: NPC? = RegionManager.getNpc(Location(2604, 3209, 0), 279, 6)
        val monk: NPC? = RegionManager.getNpc(Location(2609, 3207, 0), 281, 6)

        Pulser.submit(
            object : Pulse(1) {
                var count = 0

                override fun pulse(): Boolean {
                    when (count) {
                        1 -> {
                            brotherOmad!!.sendChat("Party!")
                            brotherOmad.animator.animate(Animation(866))
                        }

                        3 -> {
                            player!!.sendChat("Woop!")
                            player!!.animator.animate(Animation(866))
                        }

                        5 -> {
                            monk!!.sendChat("Yeah!")
                            monk.animator.animate(Animation(2106))
                        }

                        7 -> brotherOmad!!.sendChat("Let's boogie!")
                        9 -> player!!.sendChat("Oh, baby!")
                        11 -> monk!!.sendChat("GO!")
                        13 -> {
                            brotherOmad!!.sendChat("Get down!")
                            brotherOmad.animator.animate(Animation(2108))
                        }

                        15 -> player!!.sendChat("Feel the rhythm!")
                        17 -> monk!!.sendChat("Dance!")
                        19 -> brotherOmad!!.sendChat("Oh my!")
                        21 -> {
                            player!!.sendChat("Watch me go!")
                            player!!.animator.animate(Animation(861))
                        }

                        23 -> {
                            monk!!.sendChat("You go!")
                            monk.animator.animate(Animation(2109))
                        }

                        25 ->
                            if (questComplete) {
                                finishQuest(player!!, Quests.MONKS_FRIEND)
                            }
                    }
                    count++
                    return false
                }
            },
        )
    }
}
