package content.region.kandarin.dialogue.ooglog

import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GlumDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                if (!hasRequirement(player, Quests.AS_A_FIRST_RESORT)) {
                    npcl(FaceAnim.CHILD_NORMAL, "RAAAAAAAGH!").also { stage = 2 }
                } else {
                    npcl(FaceAnim.CHILD_NORMAL, "Do you know anything about this ship?").also { stage++ }
                }
            1 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "No, I just waiting here for de humans who make it go to come back. I wanna ride when dey open for business.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            2 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Stupid humans. Dey won't let me have a ride in de boat unless I gives dem lots of shiny pretties.",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.NEUTRAL, "That's so often the way, I'm afraid.").also { stage++ }
            4 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GlumDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GLUM_7077)
    }
}
