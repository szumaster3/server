package content.region.kandarin.handlers.guilds.legends

import core.api.sendMessage
import core.api.setVarbit
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 *  Represents the Radimus Erke dialogue.
 */
@Initializable
class RadimusErkeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Excuse me a moment won't you. Do feel free to explore the rest of the building.")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                end()
                sendMessage(player, "Radimus looks busy...")
                setVarbit(player, 5511, 2, true)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return RadimusErkeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RADIMUS_ERKLE_400)
    }
}
