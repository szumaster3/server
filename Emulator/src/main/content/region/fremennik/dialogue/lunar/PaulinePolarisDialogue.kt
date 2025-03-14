package content.region.fremennik.dialogue.lunar

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PaulinePolarisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.ASKING, "Ah, a stranger to our island. How can I help?").also { stage++ }
            1 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, I've actually come here on a diplomatic mission. I want to try and settle some of the disputes between the Fremenniks and your clan.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The Rremenniks? Pah! They are just too ignorant and stubborn to listen to anything we have to say - how can we possibly associate with a race that won't listen?",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I think they are very eager to listen, but they feel like you are keeping secrets from them.",
                ).also {
                    stage++
                }
            4 -> npc(FaceAnim.HALF_THINKING, "Secrets?").also { stage++ }
            5 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Yes, as in your magic ways. To be honest, I think it is fear of the unknown.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, when the day comes that a Fremennik can prove they have the patience and interest in learning our ways, then we will perhaps share our secrets.",
                ).also {
                    stage++
                }
            7 -> player(FaceAnim.FRIENDLY, "I'm hoping I might be able to achieve that.").also { stage++ }
            8 -> npc(FaceAnim.FRIENDLY, "Good luck. It's far from simple.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return PaulinePolarisDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PAULINE_POLARIS_4514)
    }
}
