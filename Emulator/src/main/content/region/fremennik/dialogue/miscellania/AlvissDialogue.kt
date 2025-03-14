package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AlvissDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "Good day, sir.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What are you doing down here?", "Good day.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "What are you doing down here?").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "Good day.").also { stage = END_DIALOGUE }
                }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "I'm waiting for my shift, of course.",
                    "We can't dig all the time, you know.",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "I'm also researching the links between the",
                    "Fremenniks and the Dwarves.",
                ).also {
                    stage++
                }
            4 ->
                npc(FaceAnim.OLD_DEFAULT, "I've found that we have some mythology in common.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AlvissDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALVISS_3933)
    }
}
