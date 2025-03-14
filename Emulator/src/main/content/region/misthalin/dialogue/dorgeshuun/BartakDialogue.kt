package content.region.misthalin.dialogue.dorgeshuun

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BartakDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DISTRESSED, "Oh no! What's broken?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.ASKING, "What? Nothing's broken?").also { stage++ }
            1 -> npc(FaceAnim.OLD_DISTRESSED, "I'm sorry. I'm just a bit jumpy.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.OLD_DISTRESSED,
                    "I'm in charge of all the metalworking of Dorgesh-Kaan. It's a big responsibility!",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.OLD_DISTRESSED,
                    "If something metal breaks I have to fix it. And lots of things are made of metal!",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.FRIENDLY, "Don't worry, I'm sure you're up to the task!").also { stage++ }
            5 -> npc(FaceAnim.OLD_DISTRESSED, "I hope you're right.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BartakDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTAK_5778)
    }
}
