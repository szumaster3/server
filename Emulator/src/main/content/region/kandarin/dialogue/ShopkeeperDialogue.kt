package content.region.kandarin.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ShopkeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.THINKING,
                    "So, are you looking to buy weapons? King Lathas keeps us very well stocked.",
                ).also {
                    stage++
                }
            1 -> options("What do you have?", "No thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What do you have?").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "No thanks.").also { stage = END_DIALOGUE }
                }
            3 -> npc(FaceAnim.NEUTRAL, "Take a look.").also { stage++ }
            4 -> {
                end()
                openNpcShop(player, NPCs.SHOPKEEPER_561)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ShopkeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOPKEEPER_561)
    }
}
