package content.region.asgarnia.dialogue.dwarvenmines

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class DwarvenShopkeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_HAPPY, "Can I help you at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes please, what are you selling?",
                    "How should I use your shop?",
                    "No thanks.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.DWARF_582)
                    }
                    2 -> player("How should I use your shop?").also { stage++ }
                    3 -> player("No, thanks.").also { stage = 3 }
                }
            2 ->
                npc(
                    FaceAnim.OLD_HAPPY,
                    "I'm glad you ask! You can buy as many of the items",
                    "stocked as you wish. You can also sell most items to",
                    "the shop.",
                ).also {
                    stage =
                        0
                }
            3 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DwarvenShopkeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARF_582)
    }
}
