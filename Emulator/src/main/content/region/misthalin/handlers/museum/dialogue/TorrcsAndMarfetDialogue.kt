package content.region.misthalin.handlers.museum.dialogue

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TorrcsAndMarfetDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when ((1..5).random()) {
            1 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.HALF_GUILTY, "Hello!").also { stage++ }
                    1 -> playerl(FaceAnim.HALF_GUILTY, "Hi, have you seen the Dig Site displays?").also { stage++ }
                    2 -> npcl(FaceAnim.HALF_GUILTY, "Yes, good aren't they! Have you done the quest?").also { stage++ }
                    3 ->
                        if (!isQuestComplete(player, Quests.THE_DIG_SITE)) {
                            playerl(FaceAnim.HALF_GUILTY, "Not yet.").also { stage++ }
                        } else {
                            playerl(FaceAnim.HALF_GUILTY, "Yes, I did it all.").also { stage = 6 }
                        }

                    4 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh you should, it lets you get into that bit where they clean the finds, and you can join in!",
                        ).also {
                            stage++
                        }

                    5 ->
                        playerl(FaceAnim.HALF_GUILTY, "Sounds fun, I should probably check it out.").also {
                            stage =
                                END_DIALOGUE
                        }

                    6 -> playerl(FaceAnim.HALF_GUILTY, "Yes, I did it all.").also { stage++ }
                    7 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Did you know after the quest you can get the teddy bear back by stealing it from the female student?",
                        ).also {
                            stage++
                        }

                    8 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "I shall have to try that sometime.",
                        ).also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.HALF_GUILTY, "Hi!").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh, hello. I was just looking at this display here.",
                        ).also { stage++ }

                    2 -> playerl(FaceAnim.HALF_GUILTY, "Anything interesting?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Yes, one I just updated by talking to 'Historian' Minas. I'm a bit of a quester you see.",
                        ).also {
                            stage++
                        }

                    4 -> playerl(FaceAnim.HALF_GUILTY, "Okay, I'll let you get on then.").also { stage++ }
                    5 -> npcl(FaceAnim.HALF_GUILTY, "Hmm, yes. Okay.").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Don't forget to check out the Natural History exhibit - you could learn a lot.",
                        ).also {
                            stage++
                        }

                    7 -> playerl(FaceAnim.HALF_GUILTY, "Right, bye!").also { stage = END_DIALOGUE }
                }

            3 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.HALF_GUILTY, "Hi there!").also { stage++ }
                    1 -> playerl(FaceAnim.HALF_GUILTY, "Hello, what are you doing here?").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh, just taking a look at these displays. I'm a bit of a history buff.",
                        ).also {
                            stage++
                        }

                    3 -> playerl(FaceAnim.HALF_GUILTY, "Really? Tell me something I didn't know!").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Well, did you know that Bob the Cat is... no, no... you'd never believe me anyway.",
                        ).also {
                            stage++
                        }

                    5 -> playerl(FaceAnim.HALF_GUILTY, "He's what? Tell me!").also { stage = END_DIALOGUE }
                    6 ->
                        if (!isQuestComplete(player, Quests.A_TAIL_OF_TWO_CATS)) {
                            npcl(FaceAnim.HALF_GUILTY, "No, no. Couldn't possibly ruin the surprise.").also { stage++ }
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "He's a very famous hero! Robert the Strong!").also { stage = 9 }
                        }

                    7 -> playerl(FaceAnim.HALF_GUILTY, "ARG! Tell me!").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "If you really want to know, go talk to Unferth in his house in Burthorpe.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }

                    9 -> npcl(FaceAnim.HALF_GUILTY, "He's a very famous hero! Robert the Strong!").also { stage++ }
                    10 -> playerl(FaceAnim.HALF_GUILTY, "Oh, yes, I knew that.").also { stage++ }
                    11 -> npcl(FaceAnim.HALF_GUILTY, "Oh, I thought I was going to surprise you.").also { stage++ }
                    12 -> playerl(FaceAnim.HALF_GUILTY, "Nope, but thanks for sharing.").also { stage = END_DIALOGUE }
                }

            4 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.HALF_GUILTY, "Hello.").also { stage++ }
                    1 -> npcl(FaceAnim.HALF_GUILTY, "Hi there. So much to see here!").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "Yep, could be looking all day! Updated any displays yet?",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Yes, I just learned more about some priestly warriors and a staff of the gods.",
                        ).also {
                            stage++
                        }

                    4 -> playerl(FaceAnim.HALF_GUILTY, "Sounds fun, how did you do that?").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Ahh, I'm big on quests me, love questing. So when I've done one,",
                        ).also { stage++ }

                    6 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I check here with 'Historian' Minas to see if I can help the Museum out with some information.",
                        ).also {
                            stage++
                        }

                    7 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Sometimes they already know it, or don't want to know and sometimes it earns me a reward or two.",
                        ).also {
                            stage++
                        }

                    8 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "That sounds really good. Perhaps I should do the same!",
                        ).also { stage++ }

                    9 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "You should, as well as everything else there is here in the Museum.",
                        ).also { stage++ }

                    10 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Did you know they have a really fun-looking display down in the basement? I love monkeys.",
                        ).also {
                            stage++
                        }

                    11 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "I'll check it out. Got to get going now, nice talking to you.",
                        ).also { stage++ }

                    12 -> npcl(FaceAnim.HALF_GUILTY, "Have a good day!").also { stage = END_DIALOGUE }
                }

            5 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.HALF_GUILTY, "Hi!").also { stage++ }
                    1 -> npcl(FaceAnim.HALF_GUILTY, "Hmm... 3rd Age... No, no... maybe 4th...").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Hello?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Yes, that's it, just like that. Mustn't forget the map either... hmm... what else?",
                        ).also {
                            stage++
                        }

                    4 -> playerl(FaceAnim.HALF_GUILTY, "HELLO! Are you there?").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh, hello there. Sorry, was a bit engrossed in updating this display.",
                        ).also {
                            stage++
                        }

                    6 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I've just been out questing and thought I'd stop off here you see. It's quite rewarding too.",
                        ).also {
                            stage++
                        }

                    7 -> playerl(FaceAnim.HALF_GUILTY, "Yes, well, no need to ignore me.").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "No offence intended, my dear " + if (player.isMale) "chap" else "girl" + ".",
                        ).also { stage++ }

                    9 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Just very interested in this history stuff, that's all.",
                        ).also { stage++ }

                    10 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "Okay. Have you seen the Natural History exhibit in the basement?",
                        ).also { stage++ }

                    11 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh, yes, marvellous isn't it! I particularly like the wyvern, very interesting considering they've never found a live specimen.",
                        ).also {
                            stage++
                        }

                    12 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "And to think they could possibly have been flapping around at one time!",
                        ).also {
                            stage++
                        }

                    13 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "A bit like the crazy gnomes and their gliders. Have you seen the model they have upstairs?",
                        ).also {
                            stage++
                        }

                    14 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Yes, very intricate isn't it? Worrying to think some people risk their lives in them! You wouldn't catch me up in one.",
                        ).also {
                            stage++
                        }

                    15 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Had a chat with Jacques while I was up there, the art critic... he's a bit funny in the head.",
                        ).also {
                            stage++
                        }

                    16 -> playerl(FaceAnim.HALF_GUILTY, "That's not very nice!").also { stage++ }
                    17 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Well, he thinks he's going to get an entire portrait gallery to himself!",
                        ).also {
                            stage++
                        }

                    18 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "Who knows, the Museum just got really big and appears to have cash.",
                        ).also {
                            stage++
                        }

                    19 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "They might entertain him with his very own floor - we'll see.",
                        ).also { stage++ }

                    20 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I guess so. Wonder what poetry he'll come up with then! Still,",
                        ).also { stage++ }

                    21 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I must get on with this display. See you around I hope!",
                        ).also { stage++ }

                    22 -> playerl(FaceAnim.HALF_GUILTY, "Bye!").also { stage = END_DIALOGUE }
                }
        }
        return true
    }


    override fun newInstance(player: Player): Dialogue {
        return TorrcsAndMarfetDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TORRCS_5939, NPCs.MARFET_5940)
    }
}
