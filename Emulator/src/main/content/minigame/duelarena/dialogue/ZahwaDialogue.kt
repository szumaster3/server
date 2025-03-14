package content.minigame.duelarena.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ZahwaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hi!")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when ((1..7).random()) {
                    1 -> npc(FaceAnim.HALF_GUILTY, "Ughhhh....").also { stage = END_DIALOGUE }
                    2 -> npc("I could've 'ad 'im!").also { stage = 10 }
                    3 -> player("Are you alright?").also { stage = 16 }
                    4 -> player("Are you alright?").also { stage = 17 }
                    5 -> player("Are you alright?").also { stage = 18 }
                    6 -> player("Are you alright?").also { stage = 19 }
                    7 -> player("Are you alright?").also { stage = 20 }
                }

            10 -> player("Er...").also { stage++ }
            11 -> npc("I was robbed!").also { stage++ }
            12 -> player("Right.").also { stage++ }
            13 -> npc("It was rigged I tell you!").also { stage++ }
            14 -> player("Uh huh.").also { stage++ }
            15 -> npc("Leave me alone!").also { stage = END_DIALOGUE }
            16 -> npc("NURSE!").also { stage = END_DIALOGUE }
            17 -> npc("Do I look alright?!").also { stage = END_DIALOGUE }
            18 -> npc("Yeh. The nurses here are...wonderful!").also { stage = END_DIALOGUE }
            19 -> npc("It's just a flesh wound!").also { stage = END_DIALOGUE }
            20 -> npc("Can't....go....on!").also { stage++ }
            21 -> npc("Leave me behind!").also { stage++ }
            22 -> player("I'll leave you here, OK?").also { stage++ }
            23 -> npc("Oh. OK.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAHWA_963)
    }
}
