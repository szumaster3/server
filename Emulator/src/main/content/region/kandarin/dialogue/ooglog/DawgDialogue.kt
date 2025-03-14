package content.region.kandarin.dialogue.ooglog

import core.api.hasRequirement
import core.api.quest.hasRequirement
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DawgDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                if (!hasRequirement(player, Quests.AS_A_FIRST_RESORT)) {
                    npcl(FaceAnim.CHILD_NEUTRAL, "Grrrr!").also { stage++ }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Hi there, um, puppy... cat... thing. Good Dawg.").also { stage = 3 }
                }
            }
            1 ->
                sendNPCDialogue(
                    player,
                    NPCs.CHIEF_TESS_7051,
                    "Watch out, human; Dawg like human for breakfast.",
                    FaceAnim.CHILD_NORMAL,
                ).also {
                    stage++
                }
            2 -> end()
            3 -> npc(FaceAnim.CHILD_NEUTRAL, "Grrrr!").also { stage++ }
            4 ->
                sendNPCDialogue(
                    player,
                    NPCs.CHIEF_TESS_7051,
                    "Huh, huh! Me think Dawg like you now.",
                    FaceAnim.CHILD_NORMAL,
                ).also {
                    stage++
                }
            5 -> playerl(FaceAnim.FRIENDLY, "He has a funny way of showing it!").also { stage = 2 }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DawgDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAWG_7104)
    }
}
