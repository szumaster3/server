package content.region.fremennik.dialogue.miscellania

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class GreengrocerMiscDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Welcome, Sir.", "I sell only the finest and freshest vegetables!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                end()
                openNpcShop(player, NPCs.GREENGROCER_1394)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GreengrocerMiscDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GREENGROCER_1394)
    }
}
