package content.region.karamja.dialogue

import core.api.interaction.openNpcShop
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TiadecheDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val randomConversation =
        arrayOf(
            "Just leave a depressed fish hunter alone...",
            "I'll never be able to catch a Karambwan...",
            "Go away! Can't you see I'm concentrating?",
        )

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, "Tai Bwo Wannai Trio", false)) {
            npcl(FaceAnim.NEUTRAL, randomConversation.random()).also { stage = END_DIALOGUE }
        } else {
            player("Hello, Tiadeche.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Hello, Bwana! Would you like to buy some Karambwan?").also { stage++ }
            1 -> options("Yes", "No.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, npc.id) }
                    2 ->
                        npc(FaceAnim.FRIENDLY, "Very well. Let me know if you change your mind.").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TIADECHE_1164)
    }
}
