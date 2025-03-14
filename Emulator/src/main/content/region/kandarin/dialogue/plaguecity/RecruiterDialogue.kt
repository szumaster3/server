package content.region.kandarin.dialogue.plaguecity

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RecruiterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.FRIENDLY,
            "Citizens of West Ardougne. Who will join the Royal Army of Ardougne? It is a very noble cause. Fight alongside King Tyras, crusading in the darklands of the west!",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> sendNPCDialogue(player, NPCs.MAN_726, "Plague bringer!").also { stage++ }
            1 -> sendNPCDialogue(player, NPCs.MAN_726, "King Tyras is scum!").also { stage++ }
            2 ->
                npcl(FaceAnim.FRIENDLY, "Tyras will be informed of these words of treason!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RECRUITER_720)
    }
}
