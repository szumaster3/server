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
class HallaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.FRIENDLY,
            "Welcome to Miscellania's first clothing store!",
            "We sell clothing made especially for Fremenniks",
            "and Dwarves.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.ASKING, "Can I help you with anything, your Royal Highness?").also { stage++ }
            1 ->
                options(
                    "I'd like to look at what you have for sale.",
                    "No thank you, I'm fine.",
                    "What's it like living down here?",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "I'd like to look at what you have for sale.").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "No thank you, I'm fine.").also { stage = END_DIALOGUE }
                    3 -> player(FaceAnim.ASKING, "What's it like living down here?").also { stage = 4 }
                }
            3 -> {
                end()
                openNpcShop(player, NPCs.HALLA_3921)
            }
            4 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "It's very spacious down here.",
                    "One of the dwarves said that the caves go on for miles!",
                ).also {
                    stage++
                }
            5 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "The only problem I find is that the lighting's not very good,",
                    "which means I make mistakes when cutting cloth.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HallaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HALLA_3921)
    }
}
