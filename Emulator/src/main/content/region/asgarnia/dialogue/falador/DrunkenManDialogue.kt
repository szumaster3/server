package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DrunkenManDialogue(
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
            0 -> npc(FaceAnim.DRUNK, "... whassup?").also { stage++ }
            1 -> player(FaceAnim.ASKING, "Are you alright?").also { stage++ }
            2 -> npc(FaceAnim.DRUNK, "... see... two of you... why there two of you?").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "There's only one of me, friend.").also { stage++ }
            4 ->
                npc(
                    FaceAnim.DRUNK,
                    "... no, two of you... you can't count...",
                    "... maybe you drunk too much...",
                ).also { stage++ }
            5 -> player(FaceAnim.FRIENDLY, "Whatever you say, friend.").also { stage++ }
            6 -> npc(FaceAnim.DRUNK, "... giant hairy cabbages...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRUNKEN_MAN_3222)
    }
}
