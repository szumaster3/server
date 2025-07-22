package content.region.kandarin.seers.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class LegendsGuardDialogue(player: Player? = null) : Dialogue(player) {

    fun gender(
        male: String = "sir",
        female: String = "madam",
    ) = if (player.isMale) male else female

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "! ! ! Attention ! ! !")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Legends Guild Member Approaching").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "Welcome " + gender() + "!", "I hope you enjoy your time in the Legends Guild.").also { stage = 21 }
            10 -> npc(FaceAnim.FRIENDLY, "I hope the quest is going well " + gender() + ".").also { stage = 21 }
            20 -> npc(FaceAnim.FRIENDLY, "Legends Guild Member Approaching!").also { stage = 21 }
            21 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = LegendsGuardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.LEGENDS_GUARD_398, NPCs.LEGENDS_GUARD_399)
}
