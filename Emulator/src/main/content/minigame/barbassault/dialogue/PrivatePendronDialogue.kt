package content.minigame.barbassault.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PrivatePendronDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hi there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Don't suppose you've seen a battleaxe around here?").also { stage++ }
            1 -> playerl(FaceAnim.HALF_GUILTY, "A battleaxe? Nope, afraid not.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The captain is going to kill me if he finds out I've lost my weapon.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRIVATE_PENDRON_5032)
    }
}
