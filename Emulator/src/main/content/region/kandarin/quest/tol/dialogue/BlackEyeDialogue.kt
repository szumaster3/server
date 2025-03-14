package content.region.kandarin.quest.tol.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BlackEyeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.TOWER_OF_LIFE)) {
            0 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "What's going on?").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ah, we be building this 'ere tower. Lookin' good ain't it?",
                        ).also { stage++ }

                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "It does look pretty impressive, but what are you doing hanging around?",
                        ).also { stage++ }

                    3 -> npcl(FaceAnim.FRIENDLY, "Tea break, naturally.").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Naturally. Not sure why I asked.").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "Fancy some?").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "Tea? Sure.").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "You'll find a kettle in the box to the-").also { stage++ }
                    8 -> playerl(FaceAnim.FRIENDLY, "Erm...yeah...no...sorry...gotta go.").also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Say, that's a nice helmet").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Why, thanks.").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I was just wondering if you might be able to help me out with one?",
                        ).also { stage++ }

                    3 -> npcl(FaceAnim.FRIENDLY, "Always glad to help a budding builder.").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "But first, prove your Construction knowledge by answering some questions.",
                        ).also { stage++ }

                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "How many nails does it take to make a rocking chair?",
                        ).also { stage++ }
                    6 -> options("One", "Five", "Three").also { stage++ }
                    7 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "One").also { stage = 8 }
                            2 -> playerl(FaceAnim.FRIENDLY, "Five").also { stage = 9 }
                            3 -> playerl(FaceAnim.FRIENDLY, "Three").also { stage = 10 }
                        }

                    8 ->
                        npcl(FaceAnim.FRIENDLY, "One? How do you suppose to use a single nail?").also {
                            stage = END_DIALOGUE
                        }

                    9 -> npcl(FaceAnim.FRIENDLY, "Nope. Keep guessing!").also { stage = END_DIALOGUE }
                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Bingo! Okay, now what takes 3 planks, 3 cloths, and 3 nails to make, and help remove light from a room?",
                        ).also { stage++ }

                    11 -> options("Torn curtains", "A brown rug", "Opulent curtains").also { stage++ }
                    12 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Torn curtains").also { stage = 15 }
                            2 -> playerl(FaceAnim.FRIENDLY, "A brown rug").also { stage = 13 }
                            3 -> playerl(FaceAnim.FRIENDLY, "Opulent curtains").also { stage = 14 }
                        }

                    13 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Of course, because that's what everyone needs - a rug up at their window.",
                        ).also { stage = END_DIALOGUE }

                    14 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Those curtains are not going to be in any way opulent, matey.",
                        ).also { stage = END_DIALOGUE }

                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Nice one. Last question: I like fish and I want to put some in my garden, but I need a special water feature. What materials would I require?",
                        ).also { stage++ }

                    16 ->
                        options(
                            "8 clay pieces and 3 limestone bricks",
                            "10 clay pieces",
                            "10 limestone bricks",
                        ).also { stage++ }

                    17 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "8 clay pieces and 3 limestone bricks").also { stage = 18 }
                            2 -> playerl(FaceAnim.FRIENDLY, "10 clay pieces").also { stage = 20 }
                            3 -> playerl(FaceAnim.FRIENDLY, "10 limestone bricks").also { stage = 19 }
                        }

                    18 -> npcl(FaceAnim.FRIENDLY, "Oh, and you were so close!").also { stage = END_DIALOGUE }
                    19 -> npcl(FaceAnim.FRIENDLY, "Guess again, babe.").also { stage = END_DIALOGUE }
                    20 -> npcl(FaceAnim.FRIENDLY, "That's it!").also { stage++ }
                    21 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You seem to know your stuff. I got a spare helmet from a builder that died on... I mean, err, had to leave the job for greener pastures.",
                        ).also { stage++ }

                    22 -> playerl(FaceAnim.FRIENDLY, "Greener pastures?").also { stage++ }
                    23 -> {
                        if (!inInventory(player, Items.HARD_HAT_10862)) {
                            addItemOrDrop(player, Items.HARD_HAT_10862)
                            sendMessage(player, "Try the beckon emote while wearing an item of builders' clothing!")
                        }
                        playerl(FaceAnim.FRIENDLY, "Fair enough.").also { stage++ }
                    }

                    24 -> playerl(FaceAnim.FRIENDLY, "Wait a minute! Is there blood on this helmet?").also { stage++ }
                    25 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Erm, no, that's just, erm, paint. Yes, paint! That's what it is!",
                        ).also { stage++ }

                    26 -> playerl(FaceAnim.FRIENDLY, "Oh, okay. Thanks. Bye.").also { stage++ }
                    27 -> npcl(FaceAnim.FRIENDLY, "Ha, gullible fool.").also { stage++ }
                    28 -> playerl(FaceAnim.FRIENDLY, "What?").also { stage++ }
                    29 -> npcl(FaceAnim.FRIENDLY, "Nothing.").also { stage++ }
                    30 -> {
                        end()
                        sendMessage(player, "Try the beckon emote while wearing an item of builders' clothing!")
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLACK_EYE_5589)
    }
}
