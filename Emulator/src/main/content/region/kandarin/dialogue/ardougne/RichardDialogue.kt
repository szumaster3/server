package content.region.kandarin.dialogue.ardougne

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RichardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Hello. How can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What are you selling?",
                    "Can you give me any farming advice?",
                    "I'm okay, thank you.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.RICHARD_2306)
                    }

                    2 -> player("Can you give me any farming advice?").also { stage++ }
                    3 -> player("I'm okay, thank you.").also { stage = END_DIALOGUE }
                }

            2 -> npc("Yes - ask a gardener.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RichardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RICHARD_2306)
    }
}
