package content.region.asgarnia.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CuffsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello. nice day for a walk, isn't it?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "A walk? Oh, yes, that's what we're doing.",
                    "We're just out here for a walk.",
                ).also {
                    stage++
                }
            1 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "I'm glad you're just out here for a walk. A more suspicious",
                    "person would think you were waiting here to attack weak-",
                    "looking travellers.",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Nope, we'd never do anything like that.",
                    "Just a band of innocent walkers, that's us.",
                ).also {
                    stage++
                }
            3 -> player(FaceAnim.HALF_GUILTY, "Alright, have a nice walk.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CuffsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CUFFS_3237)
    }
}
