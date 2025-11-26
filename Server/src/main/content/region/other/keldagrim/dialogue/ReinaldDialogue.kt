package content.region.other.keldagrim.dialogue

import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.NPCs

/**
 * Represents the Reinald dialogue.
 */
@Initializable
class ReinaldDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Welcome to my home. I have a fine selection of home-made arm guards, as well as fine clothes imported from Thessalia in Varrock, if you're interested.").also { stage++ }
            1 -> options("Yes, I'm interested.", "No, thanks.").also { stage++ }
            2 -> when (buttonId) {
                1 -> {
                    end()
                    if (!player.equipment.isEmpty) {
                        npcl(FaceAnim.OLD_DEFAULT, "You're not able to try on my clothes with all that armour. Take it off and then speak to me again.")
                    } else {
                        openInterface(player, Components.REINALD_SMITHING_EMPORIUM_593)
                    }
                }

                2 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ReinaldDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.REINALD_2194)
}
