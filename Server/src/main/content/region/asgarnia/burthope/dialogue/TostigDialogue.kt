package content.region.asgarnia.burthope.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Tostig dialogue.
 */
class TostigDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "Hi, what ales are you serving?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Well ${if (player!!.isMale) "mister" else "miss"}, our speciality is Asgarnian Ale, we also serve Wizard's Mind Bomb and Dwarven Stout.").also { stage++ }
            2 -> npcl(FaceAnim.HALF_ASKING, "Would you like to buy a drink?").also { stage++ }
            3 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Yes, please.", 10),
                Topic(FaceAnim.FRIENDLY, "No, thanks.", 20)
            )
            10 -> {
                end()
                openNpcShop(player!!, NPCs.TOSTIG_1079)
            }
            20 -> npcl(FaceAnim.HALF_ASKING, "Ah well... so um... does the grey squirrel sing in the grove?").also { stage++ }
            21 -> showTopics(
                Topic(FaceAnim.HALF_ASKING, "Huh?", 30),
                Topic(FaceAnim.FRIENDLY, "Yes, and the black cat dances in the sink.", 40),
                Topic(FaceAnim.FRIENDLY, "No, squirrels can't sing.", 50),
            )
            30 -> npcl(FaceAnim.STRUGGLE, "Er... nevermind.").also { stage = END_DIALOGUE }
            40 -> npcl(FaceAnim.FRIENDLY, "Ah you'll be wanting the trapdoor behind the bar.").also { stage = END_DIALOGUE }
            50 -> npcl(FaceAnim.FRIENDLY, "No... of course they can't...").also { stage = END_DIALOGUE }
        }
    }
}
