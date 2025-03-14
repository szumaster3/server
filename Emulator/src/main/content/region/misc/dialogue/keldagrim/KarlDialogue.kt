package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class KarlDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_DEFAULT, "Hello there, human! Come in, come in!").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Hi, nice to see you!").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "Tell me, what brings you here?").also { stage++ }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, first I came to this city just for the adventure and excitement, but then I knocked over this statue, you see.",
                ).also {
                    stage++
                }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, I didn't really knock it over myself, I was on this boat.",
                ).also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "A boat! Fascinating, go on!").also { stage++ }
            6 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yes, well, this boatman was a bit rubbish, or there was something wrong with the ship, I don't know. And then I got arrested by the Black Guard.",
                ).also {
                    stage++
                }
            7 -> npcl(FaceAnim.OLD_DEFAULT, "Not the Black Guard!").also { stage++ }
            8 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yes, and they took me to this Veldaban person, who pretended to be really angry but actually he was really nice and he asked me for my help.",
                ).also {
                    stage++
                }
            9 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "And after a whole lot of running around, I finally managed to get the statue to be rebuilt.",
                ).also {
                    stage++
                }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "Such an interesting tale!").also { stage++ }
            11 -> npcl(FaceAnim.OLD_DEFAULT, "Now how did you come to be in Keldagrim again?").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "Never mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KarlDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KARL_2195)
    }
}
