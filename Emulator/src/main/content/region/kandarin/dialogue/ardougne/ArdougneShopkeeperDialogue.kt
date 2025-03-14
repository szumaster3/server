package content.region.kandarin.dialogue.ardougne

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ArdougneShopkeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HAPPY,
            "Hello, you look like a bold adventurer. You've come to the",
            "right place for adventurers' equipment.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Oh, that sounds interesting.",
                    "How should I use your shop?",
                    "No, sorry, I've come to the wrong place.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, npc.id)
                    }
                    2 ->
                        npc(
                            FaceAnim.HAPPY,
                            "I'm glad you ask! You can buy as many of the items",
                            "stocked as you wish. You can also sell most items to the",
                            "shop.",
                        ).also {
                            stage =
                                0
                        }
                    3 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Hmph. Well, perhaps next time you'll need something",
                            "from me?",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ArdougneShopkeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AEMAD_590, NPCs.KORTAN_591)
    }
}
