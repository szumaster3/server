package content.global.activity.creation

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Homunculus dialogue.
 */
@Initializable
class HomunculusDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
           0 -> player(FaceAnim.HALF_ASKING, "Hi there, you mentioned something about creating monsters...?").also { stage++ }
           1 -> npc(FaceAnim.OLD_NORMAL, "Good! I gain know from alchemists and builders. Me make beings.").also { stage++ }
           2 -> player(FaceAnim.THINKING, "Interesting. Tell me if I'm right.").also { stage++ }
           3 -> player(FaceAnim.THINKING, "By the alchemists and builders creating you, you have inherited their combined knowledge in much the same way that a child might inherit the looks of their parents.").also { stage++ }
           4 -> npc(FaceAnim.OLD_NORMAL, "Yes, you right!").also { stage++ }
           5 -> player(FaceAnim.HALF_ASKING, "So what do you need me to do?").also { stage++ }
           6 -> npc( FaceAnim.OLD_NORMAL, "Inspect symbol of life altars around dungeon. You see item give. Use item on altar. Activate altar to create, you fight.").also { stage++ }
           7 -> player(FaceAnim.NOD_YES, "Okay. Sounds like a challenge.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOMUNCULUS_5581)
    }
}