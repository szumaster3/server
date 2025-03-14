package content.minigame.barbassault.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PrivatePaldonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hi.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Shhh. Don't talk to me.").also { stage++ }
            1 -> playerl(FaceAnim.HALF_GUILTY, "What? Why?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If I'm seen slacking off by talking to you, I'll be in deep trouble!",
                ).also { stage++ }

            3 -> playerl(FaceAnim.HALF_GUILTY, "Oh... Sorry.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRIVATE_PALDON_5034)
    }
}
