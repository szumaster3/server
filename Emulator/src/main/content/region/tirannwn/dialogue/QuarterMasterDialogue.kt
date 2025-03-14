package content.region.tirannwn.dialogue

import core.api.interaction.openNpcShop
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class QuarterMasterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.FRIENDLY,
            "Good day ${if (player.isMale) "Sir" else "Miss"}. I'm the quartermaster for King Tyras's camp. We have a little we could trade here. We have a new stock of dragon halberds. Would you like a look at what we have now?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please.", "No thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }

            2 -> {
                if (!hasRequirement(player, "Regicide")) {
                    end()
                } else {
                    end()
                    openNpcShop(player, NPCs.QUARTERMASTER_1208)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return QuarterMasterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.QUARTERMASTER_1208)
    }
}
