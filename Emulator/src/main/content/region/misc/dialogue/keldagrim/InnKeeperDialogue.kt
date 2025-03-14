package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class InnKeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.CHILD_NEUTRAL,
                    "Welcome to the King's Axe inn!",
                    "What can I help you with?",
                ).also { stage++ }
            1 -> player(FaceAnim.ASKING, "Can I have some beer please?").also { stage++ }
            2 -> npc(FaceAnim.CHILD_NORMAL, "Go to the bar downstairs.", "I only deal with residents.").also { stage++ }
            3 -> player(FaceAnim.THINKING, "Residents? People live here?").also { stage++ }
            4 ->
                npc(
                    FaceAnim.CHILD_LOUDLY_LAUGHING,
                    "No, just guests that stay the night.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return InnKeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.INN_KEEPER_2177)
    }
}
