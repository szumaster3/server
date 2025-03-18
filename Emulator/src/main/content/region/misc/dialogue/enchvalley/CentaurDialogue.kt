package content.region.misc.dialogue.enchvalley

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CentaurDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when (npc.id) {
            4438 -> npc(FaceAnim.OLD_HAPPY, "Hello, human, welcome to our valley.").also { stage = END_DIALOGUE }
            else ->
                npc(FaceAnim.OLD_LAUGH1, "What a funny creature you are! Why, you", "only have 2 legs!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    override fun newInstance(player: Player?): Dialogue = CentaurDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CENTAUR_4438, NPCs.CENTAUR_4439)
}
