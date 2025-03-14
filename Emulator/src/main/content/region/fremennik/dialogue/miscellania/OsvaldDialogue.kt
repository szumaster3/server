package content.region.fremennik.dialogue.miscellania

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class OsvaldDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Welcome to the Miscellania food store.", "We've only opened recently.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "Would you like to buy anything,", "your Royal Highness?").also { stage++ }
            1 ->
                options(
                    "Could you show me what you have for sale?",
                    "No thank you, I don't need food just now.",
                    "What's it like living down here?",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Could you show me what you have for sale?").also { stage++ }
                    2 ->
                        player(FaceAnim.NEUTRAL, "No thank you, I don't need food just now.").also {
                            stage =
                                END_DIALOGUE
                        }
                    3 -> player(FaceAnim.ASKING, "What's it like living down here?").also { stage = 4 }
                }
            3 -> {
                end()
                openNpcShop(player, NPCs.OSVALD_3923)
            }
            4 ->
                npc(FaceAnim.FRIENDLY, "The town's thriving.", "I'm sure it'll soon be as busy as Rellekka!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return OsvaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OSVALD_3923)
    }
}
