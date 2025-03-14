package content.region.tirannwn.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class KelynDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Huh... Oh sorry, you made me jump. I was miles away, day dreaming.",
                ).also { stage++ }

            1 -> player(FaceAnim.FRIENDLY, "About what may I ask?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "I was thinking about the crystal spires of Prifddinas.").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "It must be beautiful, I've only seen the city walls.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I have never seen it, all I know are the stories. I hope that changes one day.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return KelynDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KELYN_2367)
    }
}
