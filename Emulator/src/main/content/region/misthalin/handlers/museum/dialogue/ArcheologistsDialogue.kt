package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ArcheologistsDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Hello there; I see you're qualified. Come to help us out?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return ID
    }

    override fun newInstance(player: Player): Dialogue {
        return ArcheologistsDialogue(player)
    }

    companion object {
        val ID = intArrayOf(
            NPCs.BARNABUS_HURMA_5932,
            NPCs.MARIUS_GISTE_5933,
            NPCs.CADEN_AZRO_5934,
            NPCs.THIAS_LEACKE_5935,
            NPCs.SINCO_DOAR_5936,
            NPCs.TINSE_TORPE_5937
        )
    }
}
