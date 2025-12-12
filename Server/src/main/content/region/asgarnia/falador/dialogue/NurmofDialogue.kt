package content.region.asgarnia.falador.dialogue

import core.api.getAttribute
import core.api.openNpcShop
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import shared.consts.NPCs

/**
 * Represents the Nurmof dialogue.
 */
class NurmofDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Greetings and welcome to my pickaxe shop. Do you want to buy my premium quality pickaxes?").also { stage++ }
            1 -> showTopics(
                Topic("Yes, please.", 2),
                Topic("No, thank you.", 13),
                Topic("Are your pickaxes better than other pickaxes, then?", 10)
            )
            2 -> {
                end()
                openNpcShop(player!!, NPCs.NURMOF_594)
            }
            10 -> npc(FaceAnim.OLD_NORMAL, "Of course they are! My pickaxes are made of higher grade metal than your ordinary bronze pickaxes, allowing you to mine ore just that little bit faster.").also {
                stage = if (!getAttribute(player!!, "pre-dq:said-hi", true)) 11 else 13
            }
            11 -> player(FaceAnim.FRIENDLY, "By the way, Doric says hello!").also { stage++ }
            12 -> npc(FaceAnim.OLD_HAPPY, "Oh! Thank you for telling me, adventurer!").also { stage = 13 }
            13 -> {
                setAttribute(player!!, "pre-dq:said-hi", true)
                end()
            }
        }
    }
}
