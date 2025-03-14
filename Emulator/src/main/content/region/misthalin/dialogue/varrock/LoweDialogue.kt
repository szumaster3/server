package content.region.misthalin.dialogue.varrock

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LoweDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Welcome to Lowe's Archery Emporium. Do you want", "to see my wares?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, I prefer to bash things close up.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.LOWE_550)
                    }

                    2 -> player(FaceAnim.EVIL_LAUGH, "No, I prefer to bash things close up.").also { stage++ }
                }

            2 -> npc(FaceAnim.ANNOYED, "Humph, philistine.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return LoweDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOWE_550)
    }
}
