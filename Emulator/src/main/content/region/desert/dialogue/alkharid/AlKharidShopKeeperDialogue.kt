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
class AlKharidShopKeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_ASKING, "Can I help you at all?")
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
                    1 -> {
                        end()
                        openNpcShop(player, npc.id)
                    }

                    2 -> playerl(FaceAnim.ASKING, "How should I use your shop?").also { stage++ }
                    3 -> playerl(FaceAnim.NEUTRAL, "No, thanks.").also { stage = END_DIALOGUE }
                }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm glad you ask! You can buy as many of the items stocked as you wish. You can also sell most items to the shop.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AlKharidShopKeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOPKEEPER_524, NPCs.SHOP_ASSISTANT_525)
    }
}
