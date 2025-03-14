package content.region.misthalin.dialogue.barb

import content.region.kandarin.quest.scorpcatcher.dialogue.PeksaDialogueFile
import core.api.interaction.openNpcShop
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PeksaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Are you interested in buying or selling a helmet?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (player.getQuestRepository().hasStarted(Quests.SCORPION_CATCHER)) {
                    options(
                        "I could be, yes.",
                        "No, I'll pass on that.",
                        "I've heard you have a small scorpion in your possession.",
                    ).also {
                        stage++
                    }
                } else {
                    options("I could be, yes.", "No, I'll pass on that.").also { stage++ }
                }
            1 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, NPCs.PEKSA_538) }
                    2 -> player(FaceAnim.HALF_GUILTY, "No, I'll pass on that.").also { stage++ }
                    3 -> end().also { openDialogue(player, PeksaDialogueFile()) }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "Well, come back if you change your mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return PeksaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PEKSA_538)
    }
}
