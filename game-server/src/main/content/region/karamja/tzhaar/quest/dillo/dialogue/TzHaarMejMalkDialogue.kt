package content.region.karamja.tzhaar.quest.dillo.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TzHaarMejMalkDialogue(player: Player? = null) : Dialogue(player) {


    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_FRIENDLY, "You try to destroy city, JalYt? You risk more collapses!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "I'm sorry.").also { stage = END_DIALOGUE }
        }
        return true

    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TZHAAR_MEJ_MALK_7746)
}