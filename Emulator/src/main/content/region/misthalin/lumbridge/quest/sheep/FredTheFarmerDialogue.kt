package content.region.misthalin.lumbridge.quest.sheep

import core.api.getAttribute
import core.api.inInventory
import core.api.quest.finishQuest
import core.api.quest.setQuestStage
import core.api.quest.startQuest
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class FredTheFarmerDialogue(
    val questStage: Int,
) : DialogueFile() {
    companion object {
        const val STAGE_BEGIN_QUEST = 1000
        const val STAGE_PENGUIN_SHEEP_SHEARED = 3000
        const val STAGE_CANT_SPIN_WOOL = 20000
        const val STAGE_CAN_SPIN_WOOL = 20100
        const val STAGE_DELIVER_BALLS_OF_WOOL = 30000
        const val STAGE_FINISH_QUEST = 30301
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            START_DIALOGUE ->
                if (questStage == 10) {
                    if (getAttribute(player!!, SheepShearer.ATTR_IS_PENGUIN_SHEEP_SHEARED, false)) {
                        npc(FaceAnim.ANGRY, "What are you doing on my land?").also {
                            stage = STAGE_PENGUIN_SHEEP_SHEARED
                        }
                    } else {
                        npc(FaceAnim.NEUTRAL, "How are you doing getting those balls of wool?").also {
                            stage = STAGE_DELIVER_BALLS_OF_WOOL
                        }
                    }
                } else if (questStage == 90) {
                    npc(FaceAnim.SAD, "I guess I'd better pay you then.").also { stage = STAGE_FINISH_QUEST }
                } else {
                    npc(
                        FaceAnim.NEUTRAL,
                        "You're after a quest, you say? Actually I could do with",
                        "a bit of help.",
                    ).also {
                        stage =
                            STAGE_BEGIN_QUEST
                    }
                }
            STAGE_BEGIN_QUEST ->
                npc(
                    FaceAnim.NEUTRAL,
                    "My sheep are getting mighty woolly. I'd be much",
                    "obliged if you could shear them. And while you're at it",
                    "spin the wool for me too.",
                ).also {
                    stage++
                }
            1001 ->
                npc(
                    FaceAnim.HAPPY,
                    "Yes, that's it. Bring me 20 balls of wool. And I'm sure",
                    "I could sort out some sort of payment. Of course,",
                    "there's the small matter of The Thing.",
                ).also {
                    stage++
                }
            1002 ->
                options(
                    "Yes okay. I can do that.",
                    "That doesn't sound a very exciting quest.",
                    "What do you mean, The Thing?",
                ).also {
                    stage++
                }
            1003 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HAPPY, "Yes okay. I can do that.").also { stage = 2000 }
                    2 -> player(FaceAnim.HALF_GUILTY, "That doesn't sound a very exciting quest.").also { stage = 1100 }
                    3 -> player(FaceAnim.ASKING, "What do you mean, The Thing?").also { stage = 1200 }
                }
            1100 ->
                npc(FaceAnim.HALF_GUILTY, "Well what do you expect if you ask a farmer for a", "quest?").also {
                    stage =
                        END_DIALOGUE
                }
            1200 ->
                npc(
                    FaceAnim.SUSPICIOUS,
                    "Well now, no one has ever seen The Thing.  That's",
                    "why we call it The Thing, 'cos we don't know what it is.",
                ).also {
                    stage++
                }
            1201 ->
                npc(
                    FaceAnim.SCARED,
                    "Some say it's a black hearted shapeshifter, hungering for",
                    "the souls of hard working decent folk like me.  Others",
                    "say it's just a sheep.",
                ).also {
                    stage++
                }
            1202 ->
                npc(
                    FaceAnim.ANGRY,
                    "Well I don't have all day to stand around and gossip.",
                    "Are you going to shear my sheep or what!",
                ).also {
                    stage++
                }
            1203 -> options("Yes okay. I can do that.", "Erm I'm a bit worried about this Thing.").also { stage++ }
            1204 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HAPPY, "Yes okay. I can do that.").also { stage = 2000 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Erm I'm a bit worried about this Thing.").also { stage = 1300 }
                }
            1300 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm sure it's nothing to worry about. Just because",
                    "my last shearer was seen bolting out of the field",
                    "screaming for his life doesn't mean anything.",
                ).also {
                    stage++
                }
            1301 -> player(FaceAnim.HALF_GUILTY, "I'm not convinced.").also { stage = END_DIALOGUE }
            2000 -> {
                startQuest(player!!, Quests.SHEEP_SHEARER)
                npc(
                    FaceAnim.NEUTRAL,
                    "Good! Now one more thing, do you actually know how",
                    "to shear a sheep?",
                ).also { stage++ }
            }
            2001 -> options("Of course!", "Err. No, I don't know actually.").also { stage++ }
            2002 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HAPPY, "Of course!").also { stage = 2100 }
                    2 -> player(FaceAnim.NEUTRAL, "Err. No, I don't know actually.").also { stage = 2200 }
                }
            2100 -> npc(FaceAnim.NEUTRAL, "And you know how to spin wool into balls?").also { stage++ }
            2101 ->
                options(
                    "I'm something of an expert actually!",
                    "I don't know how to spin wool, sorry.",
                ).also { stage++ }
            2102 ->
                when (buttonID) {
                    1 ->
                        player(FaceAnim.HAPPY, "I'm something of an expert actually!").also {
                            stage =
                                STAGE_CAN_SPIN_WOOL
                        }
                    2 ->
                        player(FaceAnim.NEUTRAL, "I don't know how to spin wool, sorry.").also {
                            stage =
                                STAGE_CANT_SPIN_WOOL
                        }
                }
            2200 -> {
                if (inInventory(player!!, Items.SHEARS_1735)) {
                    npc(
                        FaceAnim.HAPPY,
                        "Well, you're halfway there already! You have a set of",
                        "shears in your inventory. Just use those on a Sheep to",
                        "shear it.",
                    ).also {
                        stage =
                            2300
                    }
                } else {
                    npc(
                        FaceAnim.NEUTRAL,
                        "Well, first things first, you need a pair of shears, there's",
                        "a pair in the house on the table.",
                    ).also {
                        stage =
                            2400
                    }
                }
            }
            2300 -> player(FaceAnim.NEUTRAL, "That's all I have to do?").also { stage++ }
            2301 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Well once you've collected some wool you'll need to spin",
                    "it into balls.",
                ).also {
                    stage++
                }
            2302 -> npc(FaceAnim.ASKING, "Do you know how to spin wool?").also { stage++ }
            2303 ->
                options(
                    "I don't know how to spin wool, sorry.",
                    "I'm something of an expert actually!",
                ).also { stage++ }
            2304 ->
                when (buttonID) {
                    1 ->
                        player(FaceAnim.NEUTRAL, "I don't know how to spin wool, sorry.").also {
                            stage =
                                STAGE_CANT_SPIN_WOOL
                        }
                    2 ->
                        player(FaceAnim.HAPPY, "I'm something of an expert actually!").also {
                            stage =
                                STAGE_CAN_SPIN_WOOL
                        }
                }
            2400 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Or you could buy your own pair from the General",
                    "Store in Lumbridge.",
                ).also { stage++ }
            2401 -> npc(FaceAnim.NEUTRAL, "To get to Lumbridge travel east on the road outside.").also { stage++ }
            2402 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Once you get some shears use them on the sheep in",
                    "my field.",
                ).also { stage++ }
            2403 -> player(FaceAnim.HAPPY, "Sounds easy!").also { stage++ }
            2404 -> npc(FaceAnim.LAUGH, "That's what they all say!").also { stage++ }
            2405 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Some of the sheep don't like it and will run away from",
                    "you.  Persistence is the key.",
                ).also {
                    stage++
                }
            2406 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Once you've collected some wool you can spin it into",
                    "balls.",
                ).also { stage++ }
            2407 -> npc(FaceAnim.NEUTRAL, "Do you know how to spin wool?").also { stage++ }
            2408 ->
                options(
                    "I don't know how to spin wool, sorry.",
                    "I'm something of an expert actually!",
                ).also { stage++ }
            2409 ->
                when (buttonID) {
                    1 ->
                        player(FaceAnim.NEUTRAL, "I don't know how to spin wool, sorry.").also {
                            stage = STAGE_CANT_SPIN_WOOL
                        }

                    2 ->
                        player(FaceAnim.HAPPY, "I'm something of an expert actually!").also {
                            stage =
                                STAGE_CAN_SPIN_WOOL
                        }
                }
            STAGE_PENGUIN_SHEEP_SHEARED -> options("I'm back!", "Fred! Fred! I've seen The Thing!").also { stage++ }
            3001 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HAPPY, "I'm back!").also { stage = 3100 }
                    2 -> player(FaceAnim.AMAZED, "Fred! Fred! I've seen The Thing!").also { stage = 3200 }
                }
            3100 ->
                npc(FaceAnim.NEUTRAL, "How are you doing getting those balls of wool?").also {
                    stage =
                        STAGE_DELIVER_BALLS_OF_WOOL
                }
            3200 -> npc(FaceAnim.SCARED, "You ... you actually saw it?").also { stage++ }
            3201 ->
                npc(
                    FaceAnim.SCARED,
                    "Run for the hills! ${player!!.username} grab as many chickens as",
                    "you can!  We have to ...",
                ).also {
                    stage++
                }
            3202 -> player(FaceAnim.AMAZED, "Fred!").also { stage++ }
            3203 ->
                npc(
                    FaceAnim.SCARED,
                    "... flee! Oh, woe is me! The shapeshifter is coming!",
                    "We're all ...",
                ).also { stage++ }
            3204 -> player(FaceAnim.ANGRY, "FRED!").also { stage++ }
            3205 -> npc(FaceAnim.HALF_CRYING, "... doomed. What!").also { stage++ }
            3206 -> player(FaceAnim.NEUTRAL, "It's not a shapeshifter or any other kind of monster!").also { stage++ }
            3207 -> npc(FaceAnim.ASKING, "Well then what is it boy?").also { stage++ }
            3208 ->
                player(
                    FaceAnim.THINKING,
                    "Well ... it's just two Penguins; Penguins disguised as a",
                    "sheep.",
                ).also { stage++ }
            3209 -> npc(FaceAnim.THINKING, "...").also { stage++ }
            3210 -> npc(FaceAnim.AMAZED, "Have you been out in the sun too long?").also { stage = END_DIALOGUE }
            STAGE_CANT_SPIN_WOOL -> npc(FaceAnim.NEUTRAL, "Don't worry, it's quite simple!").also { stage++ }
            20001 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "The nearest Spinning Wheel can be found on the first",
                    "floor of Lumbridge Castle.",
                ).also {
                    stage++
                }
            20002 -> npc(FaceAnim.NEUTRAL, "To get to Lumbridge Castle just follow the road east.").also { stage++ }
            20003 -> player(FaceAnim.HAPPY, "Thank you!").also { stage = END_DIALOGUE }
            STAGE_CAN_SPIN_WOOL ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Well you can stop grinning and get to work then.",
                ).also { stage++ }
            20101 -> npc(FaceAnim.ANGRY, "I'm not paying you by the hour!").also { stage = END_DIALOGUE }
            STAGE_DELIVER_BALLS_OF_WOOL -> {
                if (inInventory(player!!, Items.BALL_OF_WOOL_1759)) {
                    player(FaceAnim.HAPPY, "I have some.").also { stage = 30100 }
                } else {
                    player(FaceAnim.ASKING, "How many more do I need to give you?").also { stage = 31000 }
                }
            }
            30100 -> npc(FaceAnim.NEUTRAL, "Give 'em here then.").also { stage++ }
            30101 -> {
                val ballsOfWoolDelivered = SheepShearer.deliverBallsOfWool(player!!)
                if (SheepShearer.getBallsOfWoolRequired(player!!) == 0) {
                    setQuestStage(player!!, Quests.SHEEP_SHEARER, 90)
                    player(FaceAnim.HAPPY, "That's the last of them.").also { stage = 30300 }
                } else {
                    sendDialogue(player!!, "You give Fred $ballsOfWoolDelivered balls of wool").also { stage = 30200 }
                }
            }
            30200 -> player(FaceAnim.NEUTRAL, "That's all I've got so far.").also { stage++ }
            30201 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "I need ${SheepShearer.getBallsOfWoolRequired(player!!)} more before I can pay you.",
                ).also {
                    stage++
                }
            30202 -> player(FaceAnim.NEUTRAL, "Ok I'll work on it.").also { stage = END_DIALOGUE }
            30300 -> npc(FaceAnim.SAD, "I guess I'd better pay you then.").also { stage++ }
            STAGE_FINISH_QUEST -> finishQuest(player!!, Quests.SHEEP_SHEARER).also { stage = END_DIALOGUE }
            31000 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "You need to collect ${SheepShearer.getBallsOfWoolRequired(player!!)} more balls of wool.",
                ).also {
                    stage++
                }
            31001 -> {
                if (inInventory(player!!, Items.WOOL_1737)) {
                    player(
                        FaceAnim.NEUTRAL,
                        "Well I've got some wool. I've not managed to make it",
                        "into a ball though.",
                    ).also {
                        stage =
                            31100
                    }
                } else {
                    player(FaceAnim.HALF_GUILTY, "I haven't got any at the moment.").also { stage = 31200 }
                }
            }
            31100 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Well go find a spinning wheel then. You can find one",
                    "on the first floor of Lumbridge Castle, just walk east on",
                    "the road outside my house and you'll find Lumbridge.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            31200 -> npc(FaceAnim.HALF_GUILTY, "Ah well at least you haven't been eaten.").also { stage = END_DIALOGUE }
        }
    }
}
