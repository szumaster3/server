package content.region.kandarin.dialogue.guthanoth

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class OgreMerchantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_CALM_TALK2, "Does the little creature want to buy sumfin'?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes I do.", "No I don't.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Yes I do.").also { stage = 3 }
                    2 -> player("No I don't.").also { stage++ }
                }

            2 -> npcl(FaceAnim.OLD_CALM_TALK2, "Suit yourself.").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.OLD_CALM_TALK2, "Welcome to Grud's Herblore Stall.").also { stage++ }
            4 -> {
                end()
                openNpcShop(player, NPCs.OGRE_MERCHANT_874)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OgreMerchantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OGRE_MERCHANT_874)
    }
}
