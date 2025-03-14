package content.region.kandarin.dialogue.ardougne

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ArdougneBakerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Good day, monsieur. Would you like ze nice", "freshly-baked bread? Or perhaps a nice piece of cake?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Let's see what you have.", "No thank you.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, npc.id)
                    }

                    2 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ArdougneBakerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BAKER_571)
    }
}
