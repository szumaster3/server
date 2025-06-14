package content.minigame.mta.dialogue

import content.minigame.mta.plugin.TelekineticTheatrePlugin
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MazeGuardianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hi!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Well done on releasing me. Would you like to try",
                    "another maze?",
                ).also { stage++ }
            1 -> options("Yes please!", "No thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Yes please!").also { stage = 4 }
                    2 -> player("No thanks.").also { stage++ }
                }
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Very well. Talk to me if you want to move onto the",
                    "next maze, or you can return to the entrance hall",
                    "through the portal.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 -> npc(FaceAnim.OLD_NORMAL, "Very well, I shall teleport you.").also { stage++ }
            5 -> {
                npc.clear()
                TelekineticTheatrePlugin.getZone(player).setUp()
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MAZE_GUARDIAN_3102)
}
