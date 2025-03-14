package content.region.kandarin.dialogue.fishingplatform

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class Fisherman3Dialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> sendDialogue(player!!, "His eyes are staring vacantly into space.").also { stage++ }
            1 -> npcl(FaceAnim.DRUNK, "Free of the deep blue we are... We must find...").also { stage++ }
            2 -> player("Yes?").also { stage++ }
            3 -> npcl(FaceAnim.DRUNK, "a new home... We must leave this place...").also { stage++ }
            4 -> player(FaceAnim.ASKING, "Where will you go?").also { stage++ }
            5 -> npcl(FaceAnim.DRUNK, "Away... Away to her...").also { stage++ }
            6 -> playerl(FaceAnim.AFRAID, "Riiiight.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return Fisherman3Dialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHERMAN_704)
    }
}
