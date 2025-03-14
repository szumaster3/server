package content.region.karamja.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TamayuDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id == NPCs.TAMAYU_1167) {
            npc(FaceAnim.ANNOYED, randomConversation.random()).also { stage = END_DIALOGUE }
        } else {
            player("Hello, Tamayu, first son of Timfraku.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_ASKING, "Stranger, why you have returned?").also { stage++ }
            1 -> player("I'm just passing through.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Using your new karambwan poison I have been creating spears. Would you like to buy some?",
                ).also {
                    stage++
                }
            3 -> options("Yes.", "No.").also { stage++ }
            4 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, NPCs.TAMAYU_1167) }
                    2 -> npc(FaceAnim.FRIENDLY, "As you wish, stranger.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(2487, NPCs.TAMAYU_1167, NPCs.TAMAYU_1168, NPCs.TAMAYU_1169, NPCs.TAMAYU_1170)
    }

    companion object {
        val randomConversation =
            arrayOf("Get out of my way! Argh! I'm hunting!", "Move, lest I slay not the beast but you!", "Die! Die!!")
    }
}
