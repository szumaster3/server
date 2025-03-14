package content.region.asgarnia.dialogue.dwarvenmines

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DwarvenMineGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_ANGRY1,
                    "Don't distract me while I'm on duty! This mine has to be protected!",
                ).also { stage++ }
            1 -> player(FaceAnim.FRIENDLY, "What's going to attack a mine?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.OLD_ANGRY1,
                    "Goblins! They wander everywhere, attacking anyone they think is small enough to be an easy victim. We need more cannons to fight them off properly.",
                ).also {
                    stage++
                }
            3 -> player(FaceAnim.FRIENDLY, "Well, I've done my bit to help with that.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.OLD_ANGRY1,
                    "Yes, I heard. Now please let me get on with my guard duties.",
                ).also { stage++ }
            5 -> player(FaceAnim.FRIENDLY, "Alright, I'll leave you alone now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DwarvenMineGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_206)
    }
}
