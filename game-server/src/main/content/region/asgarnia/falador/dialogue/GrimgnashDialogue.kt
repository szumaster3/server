package content.region.asgarnia.falador.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE

/**
 * Represents the Grimngnash dialogue.
 *
 * # Relations
 * - varbit id: 3717
 * - scenery id: 24839
 */
@Initializable
class GrimgnashDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "What you want, little human? Grimgnash hungry. Want", "tasty morsel like you!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Like me? Why?", "Who are you?").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "I Grimngnash and I hungry! Perhaps I eat you!").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "I'm really not that tasty. I think I should be going now.", "Goodbye.").also { stage++ }
            3 -> npc(FaceAnim.OLD_ANGRY1, "Human lucky Grimgnash too tired to hunt for food. Stupid", "wolves keep Grimgnsh awake with howling. Grimgnash", "can't sleep.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(5997)
}
