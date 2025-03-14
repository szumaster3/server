package content.region.asgarnia.dialogue.rimmington

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RimmingtonShopDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Can I help you at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes, please. What are you selling?",
                    "How should I use your shop?",
                    "No, thanks.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, npc.id) }
                    2 ->
                        npc(
                            FaceAnim.HAPPY,
                            "I'm glad you ask! You can buy as many of the items",
                            "stocked as you wish. You can also sell most items to the",
                            "shop.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    3 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOP_ASSISTANT_531, NPCs.SHOPKEEPER_530)
    }
}
