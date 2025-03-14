package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CaptainDaerkinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    /*
     * He is found on the viewing walls of the Duel Arena near Jadid.
     */
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_DEFAULT, "Hello old chap.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "What are you doing here? Shouldn't you be looking after your glider?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "I'm pretty much retired these days old fellow. My test piloting days are over. I'm just relaxing here and enjoying the primal clash between man and man.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "You're watching the duels then. Are you going to challenge someone yourself?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "I do find the duels entertaining to watch, but I suspect that actually being involved would be a lot less fun for me. I'm a lover, not a fighter!").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "Errm, I suppose you are.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CaptainDaerkinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_DAERKIN_4595)
    }
}