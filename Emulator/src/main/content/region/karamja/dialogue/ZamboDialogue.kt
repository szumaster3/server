package content.region.karamja.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ZamboDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HALF_GUILTY,
            "Hey, are you wanting to try some of my fine wines",
            "and spririts? All brewed locally on Karamja island.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thank you").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.ZAMBO_568)
                    }

                    2 -> player(FaceAnim.HALF_GUILTY, "No, thank you.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAMBO_568)
    }
}
