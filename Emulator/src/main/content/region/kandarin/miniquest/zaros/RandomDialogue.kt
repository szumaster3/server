package content.region.kandarin.miniquest.zaros

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the random dialogue of a mysterious ghost
 * when the player does not have the ghostspeak amulet.
 *
 * Relations:
 * - [Curse of Zaros miniquest][CurseOfZaros]
 */
class RandomDialogue : DialogueFile() {

    private var branch = -1

    override fun handle(componentID: Int, buttonID: Int) {
        if (branch == -1) {
            branch = (0..7).random()
        }

        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!"); stage = 1 }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Yeah, I heard about that, ha-ha-ha!"); stage = 2 }
                    2 -> { npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!"); stage = 3 }
                    3 -> { playerl(FaceAnim.FRIENDLY, "With a MACKEREL? Ouch!"); stage = 4 }
                    4 -> { npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!"); stage = 5 }
                    5 -> { playerl(FaceAnim.FRIENDLY, "Well, it was fun. Let's do it again sometime."); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!"); stage = 1 }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Hey, don't think you can talk to me like that!"); stage = 2 }
                    2 -> { npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!"); stage = 3 }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Are you threatening me?"); stage = 4 }
                    4 -> { npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!"); stage = 5 }
                    5 -> { playerl(FaceAnim.FRIENDLY, "Just because you're already dead, doesn't mean I can't find a way to hurt you ghosty!"); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!"); stage = 1 }
                    1 -> { playerl(FaceAnim.FRIENDLY, "Why, thank you very much!"); stage = 2 }
                    2 -> { npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!"); stage = 3 }
                    3 -> { playerl(FaceAnim.FRIENDLY, "I know, but what are you going to do?"); stage = 4 }
                    4 -> { npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!"); stage = 5 }
                    5 -> { playerl(FaceAnim.FRIENDLY, "Well, I guess that's always true in the long run. See you around, weird invisible dead person!"); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!"); stage = 1 }
                    1 -> { playerl(FaceAnim.FRIENDLY, "We get signal!"); stage = 2 }
                    2 -> { npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!"); stage = 3 }
                    3 -> { playerl(FaceAnim.FRIENDLY, "Somebody set up us the bomb!"); stage = 4 }
                    4 -> { npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!"); stage = 5 }
                    5 -> { playerl(FaceAnim.FRIENDLY, "You have no chance to survive make your time."); stage = 6 }
                    6 -> { npcl(FaceAnim.NEUTRAL, "Woo?"); stage = 7 }
                    7 -> { playerl(FaceAnim.FRIENDLY, "All your base are belong to us."); stage = END_DIALOGUE }
                }
            }
            4 -> {
                when (stage) {
                    0 -> npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!").also { stage = 1 }
                    1 -> playerl(FaceAnim.FRIENDLY, "No, I've never been there in my life and CERTAINLY didn't steal anything when I was!").also { stage = 2 }
                    2 -> npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!").also { stage = 3 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Are you calling me a liar?!?! I have never stolen a thing... Or cakes. Or fur. Repeatedly.").also { stage = 4 }
                    4 -> npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!").also { stage = 5 }
                    5 -> playerl(FaceAnim.FRIENDLY, "Well if you are going to take that attitude, then I have nothing further to say.").also { stage = END_DIALOGUE }
                }
            }
            5 -> {
                when (stage) {
                    0 -> npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!").also { stage = 1 }
                    1 -> playerl(FaceAnim.FRIENDLY, "Yeah, I don't want to brag, but seriously: I am SOOOO rich....").also { stage = 2 }
                    2 -> npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!").also { stage = 3 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Sometimes I just like to open my bank and look at my stuff...").also { stage = 4 }
                    4 -> npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!").also { stage = 5 }
                    5 -> playerl(FaceAnim.FRIENDLY, "Well, us rich alive people don't want to waste time with you dead ones!").also { stage = 6 }
                    6 -> playerl(FaceAnim.FRIENDLY, "See ya around ghosty!").also { stage = END_DIALOGUE }
                }
            }
            6 -> {
                when (stage) {
                    0 -> npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!").also { stage = 1 }
                    1 -> playerl(FaceAnim.FRIENDLY, "You don't say?").also { stage = 2 }
                    2 -> npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!").also { stage = 3 }
                    3 -> playerl(FaceAnim.FRIENDLY, "You don't say!").also { stage = 4 }
                    4 -> npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!").also { stage = 5 }
                    5 -> playerl(FaceAnim.FRIENDLY, "Well, I guess you didn't say.").also { stage = END_DIALOGUE }
                }
            }
            7 -> {
                when (stage) {
                    0 -> npc(FaceAnim.NEUTRAL, "Wooo?", "Woooo woo woooooo wooooo woooowooo wooo woooooo woo!").also { stage = 1 }
                    1 -> playerl(FaceAnim.FRIENDLY, "Yeah it's not bad, but I prefer cooked chicken.").also { stage = 2 }
                    2 -> npc(FaceAnim.NEUTRAL, "Woo!", "WOO WOOOOO WOOWOOWOO WOO WOOOOO!").also { stage = 3 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Maybe, but nothing beats a home cooked pie! Man, I love pie!").also { stage = 4 }
                    4 -> npc(FaceAnim.NEUTRAL, "Wooowoowoooooo....", "Woowoowoo? Woooo, wooowoo wooowooowooo!").also { stage = 5 }
                    5 -> playerl(FaceAnim.FRIENDLY, "You don't say? I never knew that. Well, I must be going, see you around.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
