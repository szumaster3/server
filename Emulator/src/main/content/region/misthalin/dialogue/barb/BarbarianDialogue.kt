package content.region.misthalin.dialogue.barb

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BarbarianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "What do you want, outerlander?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Let's fight!", "Goodbye.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Let's fight!").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            2 -> end().also { npc.attack(player) }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BarbarianDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.BARBARIAN_12,
            NPCs.BARBARIAN_3246,
            NPCs.BARBARIAN_3247,
            NPCs.BARBARIAN_3248,
            NPCs.BARBARIAN_3249,
            NPCs.BARBARIAN_3250,
            NPCs.BARBARIAN_3251,
            NPCs.BARBARIAN_3252,
            NPCs.BARBARIAN_3253,
            NPCs.BARBARIAN_3255,
            NPCs.BARBARIAN_3256,
            NPCs.BARBARIAN_3257,
            NPCs.BARBARIAN_3258,
            NPCs.BARBARIAN_3259,
            NPCs.BARBARIAN_3260,
            NPCs.BARBARIAN_3261,
            NPCs.BARBARIAN_3262,
            NPCs.BARBARIAN_3263,
            NPCs.BARBARIAN_5909,
        )
    }
}
