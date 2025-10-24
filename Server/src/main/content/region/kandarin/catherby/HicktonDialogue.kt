package content.region.kandarin.catherby

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Hickton dialogue.
 */
@Initializable
class HicktonDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Welcome to Hickton's Archery Emporium. Do you", "want to see my wares?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> stage = if (isMaster(player, Skills.FLETCHING)) {
                options("Can I buy a Skillcape of Fletching?", "Yes, please.", "No, I prefer to bash things close up.")
                2
            } else {
                options("Yes, please.", "No, I prefer to bash things close up.")
                1
            }
            1 -> when (buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> player(FaceAnim.EVIL_LAUGH, "No, I prefer to bash things close up.").also { stage = END_DIALOGUE }

            }
            2 -> when (buttonId) {
                1 -> player("Can I buy a Skillcape of Fletching?").also { stage++ }
                2 -> end().also { npc.openShop(player) }
                3 -> player(FaceAnim.EVIL_LAUGH, "No, I prefer to bash things close up.").also { stage = END_DIALOGUE }

            }
            3 -> npc("You will have to pay a fee of 99,000 gp.").also { stage++ }
            4 -> options("Yes, here you go.", "No, thanks.").also { stage++ }
            5 -> when (buttonId) {
                1 -> player("Yes, here you go.").also { stage++ }
                2 -> end()
            }
            6 -> if (purchase(player, Skills.FLETCHING)) {
                npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = HicktonDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.HICKTON_575)
}
