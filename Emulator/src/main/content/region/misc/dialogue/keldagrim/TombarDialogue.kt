package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TombarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_ASKING, "Say, aren't you a bit tall for a dwarf?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Was there anything in particular you wanted?").also { stage++ }
            1 -> options("I'd like a quest please.", "No, I just like talking to strangers.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("I'd like a quest please.").also { stage++ }
                    2 -> player("No, I just like talking to strangers.").also { stage = 4 }
                }
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "I have nothing to do for you, I'm afraid.",
                    "Ask around town, though, there are always people",
                    "who need some work done around here.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 -> npc(FaceAnim.OLD_NORMAL, "Well I don't.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return TombarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOMBAR_2199)
    }
}
