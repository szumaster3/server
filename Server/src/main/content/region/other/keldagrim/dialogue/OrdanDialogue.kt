package content.region.other.keldagrim.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Ordan dialogue.
 */
@Initializable
class OrdanDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Can you un-note any of my items?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "I can un-note Tin, Copper, Iron, Coal, and Mithril.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "I can even un-note Adamantite and Runite, but you're gonna need deep pockets for that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = OrdanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ORDAN_2564)

}
