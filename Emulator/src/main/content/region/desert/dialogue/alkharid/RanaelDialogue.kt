package content.region.desert.dialogue.alkharid

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RanaelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HAPPY,
            "Do you want to buy any armoured skirts? Designed",
            "especially for ladies who like to fight.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thank you, that's not my scene.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.RANAEL_544)
                    }

                    2 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "No, thank you, that's not my scene.",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return RanaelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RANAEL_544)
    }
}
