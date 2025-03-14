package content.region.asgarnia.dialogue.falador

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HerquinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Do you wish to trade?", "Sorry, I don't want to talk to you, actually.").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Do you wish to trade?").also { stage = 3 }
            1 -> player(FaceAnim.HALF_GUILTY, "Sorry, I don't want to talk to you, actually.").also { stage++ }
            2 -> npc(FaceAnim.ROLLING_EYES, "Huh, charming.").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.FRIENDLY, "Why, yes, this is a jewel shop after all.").also { stage++ }
            4 -> {
                end()
                openNpcShop(player, NPCs.HERQUIN_584)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HERQUIN_584)
    }
}
