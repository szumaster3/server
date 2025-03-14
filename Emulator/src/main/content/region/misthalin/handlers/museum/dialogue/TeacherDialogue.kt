package content.region.misthalin.handlers.museum.dialogue

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TeacherDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.location.z == 0) {
            sendNPCDialogue(
                player, NPCs.TEACHER_5950, "Stop pulling, we've plenty of time to see everything.", FaceAnim.ANGRY
            )
            stage = 0
        } else {
            sendNPCDialogue(
                player, NPCs.SCHOOLGIRL_5951, "That man over there talks funny, miss.", FaceAnim.HALF_GUILTY
            )
            stage = 1
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                sendNPCDialogue(
                    player, NPCs.SCHOOLGIRL_5951, "Aww, but miss, it's sooo exciting.", FaceAnim.CHILD_FRIENDLY
                )
                stage = END_DIALOGUE
            }

            1 -> {
                sendNPCDialogue(
                    player,
                    NPCs.TEACHER_5950,
                    "That's because he's an art critic, dear. They have some very funny ideas.",
                    FaceAnim.HALF_GUILTY
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TeacherDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TEACHER_AND_PUPIL_5947)
    }
}
