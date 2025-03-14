package content.region.kandarin.dialogue.ooglog

import core.api.quest.hasRequirement
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ThumpDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                if (!hasRequirement(player, Quests.AS_A_FIRST_RESORT)) {
                    npcl(FaceAnim.CHILD_EVIL_LAUGH, "RAAAAAAAGH!").also { stage = 3 }
                } else {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "C'mere, human. Me need practice at dis massage thing.",
                    ).also { stage++ }
                }
            1 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Me not sure how to do it without breaking spine of small, puny creatures.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.FRIENDLY, "I...think I'll take a rain check on that.").also { stage = END_DIALOGUE }
            3 ->
                sendNPCDialogue(player, NPCs.GNOME_66, "Send...help...!", FaceAnim.OLD_ALMOST_CRYING).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ThumpDialogue(player)
    }

    override fun getIds(): IntArray = intArrayOf(7101)
}
