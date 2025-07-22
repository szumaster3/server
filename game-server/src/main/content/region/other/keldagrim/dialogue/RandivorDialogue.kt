package content.region.other.keldagrim.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Randivor dialogue.
 */
@Initializable
class RandivorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.OLD_NORMAL,
            "Welcome to my stall! Come and buy some",
            "tasty bread, freshly baked just 2 months ago!",
        )
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Sounds delicious!", "I think I'll pass.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "Sounds delicious!").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "I think I'll pass.").also { stage = END_DIALOGUE }
                }
            2 -> {
                end()
                openNpcShop(player, NPCs.RANDIVOR_2156)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RandivorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.RANDIVOR_2156)
}
