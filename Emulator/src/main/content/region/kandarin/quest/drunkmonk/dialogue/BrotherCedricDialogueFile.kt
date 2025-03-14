package content.region.kandarin.quest.drunkmonk.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class BrotherCedricDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.MONKS_FRIEND)
        when {
            questStage < 30 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello.").also { stage++ }
                    1 -> npcl(FaceAnim.DRUNK, "Honey, money, woman and wine!").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Are you ok?").also { stage++ }
                    3 -> npcl(FaceAnim.DRUNK, "Yesshh...hic up...beautiful!").also { stage++ }
                    4 -> playerl(FaceAnim.NEUTRAL, "Take care old monk.").also { stage++ }
                    5 -> npcl(FaceAnim.DRUNK, "La..di..da..hic..up!").also { stage = END_DIALOGUE }
                }
            }

            questStage == 30 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Brother Cedric are you okay?").also { stage++ }
                    1 -> npcl(FaceAnim.DRUNK, "Yeesshhh, I'm very, very drunk..hic..up..").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "Brother Omad needs the wine for the party.").also { stage++ }
                    3 ->
                        npcl(FaceAnim.SAD, "Oh dear, oh dear, I knew I had to do something!").also {
                            setQuestStage(player!!, Quests.MONKS_FRIEND, 40)
                            stage = END_DIALOGUE
                        }
                }
            }

            questStage == 40 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Are you okay?").also { stage++ }
                    1 -> npcl(FaceAnim.DRUNK, "Hic up! Oh my head! I need a jug of water.").also { stage++ }
                    2 ->
                        if (player!!.inventory.containItems(Items.JUG_OF_WATER_1937)) {
                            playerl(FaceAnim.FRIENDLY, "Cedric! Here, drink! I have some water.").also { stage = 10 }
                        } else {
                            playerl(FaceAnim.NEUTRAL, "I'll see if I can get some.").also { stage = 3 }
                        }

                    3 -> npcl(FaceAnim.DRUNK, "Thanks! *hic*").also { stage = END_DIALOGUE }
                    10 -> npcl(FaceAnim.DRUNK, "Good stuff, my head's spinning!").also { stage++ }
                    11 -> {
                        sendItemDialogue(player!!, Items.JUG_OF_WATER_1937, "You hand the monk a jug of water.")
                        stage = 0
                        player!!.inventory.remove(Item(Items.JUG_OF_WATER_1937))
                        setQuestStage(player!!, Quests.MONKS_FRIEND, 41)
                    }
                }
            }

            questStage == 41 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.HAPPY, "Aah! That's better!").also { stage++ }
                    1 -> npcl(FaceAnim.HAPPY, "Now I just need to fix this cart and we can go party.").also { stage++ }
                    2 -> npcl(FaceAnim.NEUTRAL, "Could you help?").also { stage++ }
                    3 -> options("No, I've helped enough monks today!", "Yes, I'd be happy to!").also { stage++ }
                    4 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.ANGRY, "No, I've helped enough monks today!").also { stage++ }
                            2 -> playerl(FaceAnim.FRIENDLY, "Yes, I'd be happy to!").also { stage = 10 }
                        }

                    5 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "In that case I'd better drink more wine! It helps me think.",
                        ).also { stage = END_DIALOGUE }

                    10 -> npcl(FaceAnim.HAPPY, "Excellent, I just need some wood.").also { stage++ }
                    11 ->
                        playerl(FaceAnim.NEUTRAL, "Ok, I'll see what I can find.").also {
                            setQuestStage(player!!, Quests.MONKS_FRIEND, 42)
                            stage = END_DIALOGUE
                        }
                }
            }

            questStage == 42 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.HALF_ASKING, "Did you manage to get some wood?").also { stage++ }
                    1 ->
                        if (player!!.inventory.containItems(Items.LOGS_1511)) {
                            sendItemDialogue(player!!, Items.LOGS_1511, "You hand Cedric some logs.")
                            stage = 2
                        } else {
                            playerl(FaceAnim.SAD, "Not yet, I'm afraid.").also { stage = END_DIALOGUE }
                        }

                    2 -> playerl(FaceAnim.HAPPY, "Here you go!").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "Well done! Now I'll fix this cart. You head back to Brother Omad and tell him I'll be there soon.",
                        ).also { stage++ }

                    4 ->
                        playerl(FaceAnim.HAPPY, "Ok! I'll see you later!").also {
                            stage = END_DIALOGUE
                            player!!.inventory.remove(Item(Items.LOGS_1511))
                            setQuestStage(player!!, Quests.MONKS_FRIEND, 50)
                        }
                }
            }

            questStage == 50 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.HAPPY, "Hello Cedric.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Hi, I'm almost done here. Could you tell Omad that I'll be back soon?",
                        ).also { stage = END_DIALOGUE }
                }
            }

            questStage == 100 -> {
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Brother Omad sends you his thanks! He won't be in a fit state to thank you in person.",
                        ).also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
