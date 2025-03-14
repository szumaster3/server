package content.region.misthalin.dialogue.dorgeshuun

import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class UrtagDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val foodPermission = "/save:dorgesh-kaan:food_permission"
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Greetings, surface-dweller. Welcome to Dorgesh-Kaan.",
                ).also { stage++ }
            1 ->
                if (getAttribute(player, foodPermission, false)) {
                    options(
                        "What can I do in the city?",
                        "Why did you decide to open the city?",
                    ).also { stage++ }
                } else {
                    options(
                        "What can I do in the city?",
                        "Why did you decide to open the city?",
                        "May I have permission to sell food in the market?",
                    ).also { stage++ }
                }
            2 ->
                when (buttonId) {
                    1 -> player("What can I do in the city?").also { stage = 7 }
                    2 -> player("Why did you decide to open the city?").also { stage = 5 }
                    3 -> player("May I have permission to sell food in the market?").also { stage = 3 }
                }
            3 -> npcl(FaceAnim.OLD_NORMAL, "Of course you can.").also { stage++ }
            4 -> {
                end()
                setAttribute(player, foodPermission, true)
            }
            5 -> npcl(FaceAnim.OLD_NORMAL, "For trade, and for fostering goodwill between races!").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I am sure that it will benefit both our races.",
                ).also { stage = END_DIALOGUE }
            7 -> npcl(FaceAnim.OLD_NORMAL, "Why don't you visit the market?").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I hear there is quite a craze for surface foods at the moment, so you might be able to sell something there.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You might also want to visit the caves to the south of the city.",
                ).also { stage++ }
            10 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "There are many monsters to fight there, and Zanik tells me she has also found ways to cross over their heads!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return UrtagDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UR_TAG_5799)
    }
}
