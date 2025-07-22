package content.region.tirannwn.lletya.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Dalldav dialogue.
 */
@Initializable
class DalldavArcheryShopDialogue(player: Player? = null) : Dialogue(player) {

    /*
     * Info: Elf who runs the Lletya Archery Shop in the rebel elf settlement of Lletya.
     * He sells arrows up to rune and bows up to willow, as well as a crossbow and bronze bolts.
     * Location: 2323,3163
     */

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Can I help you at all?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please. What are you selling?", "Why do you sell this stuff?", "No thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.FRIENDLY, "Yes please. What are you selling?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Why do you sell this stuff? The Crystal Bow is so much better.").also { stage = 3 }
                3 -> player(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
            }
            2 -> {
                end()
                openNpcShop(player, NPCs.DALLDAV_7447)
            }
            3 -> npcl(FaceAnim.FRIENDLY, "We keep all these old toys to train our children with, but if people will part with coins for them, then they are theirs!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DALLDAV_7447) // NPCs.DALLDAV_2356
    }
}
