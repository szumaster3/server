package content.region.kandarin.quest.grandtree.dialogue

import core.api.openDialogue
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ShipyardWorkerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (getQuestStage(player, Quests.THE_GRAND_TREE) == 55) {
                    end().also { openDialogue(player, ShipyardWorkerGTDialogue(), NPC(NPCs.SHIPYARD_WORKER_675)) }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                }

            1 -> npcl(FaceAnim.FRIENDLY, "Hello matey!").also { stage++ }
            2 -> playerl(FaceAnim.ASKING, "How are you?").also { stage++ }
            3 -> npcl(FaceAnim.NEUTRAL, "Tired!").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "You shouldn't work so hard!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHIPYARD_WORKER_675, NPCs.SHIPYARD_WORKER_38, NPCs.SHIPYARD_WORKER_39)
    }
}
