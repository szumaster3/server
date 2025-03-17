package content.custom.dialogue

import core.api.interaction.openNpcShop
import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components

@Initializable
class ChrisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.HALF_ASKING,
            "Hello, I've got some wares in you might have lost. Care to take a look?"
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Who are you and what do you sell?", "Show me what you've got.", "No thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("Who are you and what do you sell?").also { stage++ }
                2 -> player("Show me what you've got.").also { stage = 7 }
                3 -> player("No thanks.").also { stage = END_DIALOGUE }
            }
            2 -> npcl(FaceAnim.FRIENDLY, "I'm Chris, travelling repairman. If you need something repaired, I can do it for you if you've got the money for it.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, " I've also got a collection of items people end up losing a lot. Nothing fancy, and certainly nothing you couldn't get elsewhere, maybe for free.").also { stage++ }
            4 -> npcl(FaceAnim.HALF_ASKING, "Still, I charge a convenience fee for having it right here. Interested?").also { stage++ }
            5 -> options("Show me what you've got.", "No thanks.").also { stage++ }
            6 -> when(buttonId){
                1 -> player("Show me what you've got.").also { stage++ }
                2 -> player("No thanks.").also { stage = END_DIALOGUE }
            }
            7 -> end().also { openInterface(player, Components.LOST_PROPERTY_834) }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(8592)
    }
}
