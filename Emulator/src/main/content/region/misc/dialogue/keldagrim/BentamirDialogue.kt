package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BentamirDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_ANGRY1, "Do you mind? You're in my home.").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "I'm sorry, should I have knocked?").also { stage++ }
            2 -> npcl(FaceAnim.OLD_ANGRY1, "Very funny, human.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.OLD_ANGRY1,
                    "We can't all live in plush houses, you know. But that doesn't mean us mining dwarves don't work hard.",
                ).also {
                    stage++
                }
            4 -> playerl(FaceAnim.FRIENDLY, "Where do you do your mining?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.OLD_NOT_INTERESTED,
                    "Normally the coal mine to the north. We need a lot of coal to keep our steam engines going, you know.",
                ).also {
                    stage++
                }
            6 -> playerl(FaceAnim.FRIENDLY, "Can I do a bit of mining there as well?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.OLD_NOT_INTERESTED,
                    "I'm not sure, but as long as no one notices I don't think anyone is going to care.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BentamirDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BENTAMIR_2192)
    }
}
