package content.region.kandarin.dialogue.stronghold

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LocalGnomesDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello little man.")
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "Little man stronger than big man. Hee hee, lardi dee, lardi da.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LocalGnomesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOCAL_GNOME_484)
    }
}
