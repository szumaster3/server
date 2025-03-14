package content.region.kandarin.handlers.guilds.legends

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 *  Represents the Siegfried Erkle dialogue.
 */
@Initializable
class SiegfriedErkleDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Hello there and welcome to the shop of useful items. Can I help you at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes please. What are you selling?",
                    "No thanks.",
                    "Didn't you once sell Silverlight?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Yes please. What are you selling?").also { stage = 4 }
                    2 -> player("No thanks.").also { stage = 5 }
                    3 -> player("Didn't you once sell Silverlight?").also { stage++ }
                }
            2 ->
                npcl(
                    FaceAnim.SUSPICIOUS,
                    "Silverlight? Oh, Sir Prysin of Varrock explained that was a unique sword and told us to stop selling it.",
                ).also {
                    stage++
                }
            3 ->
                npcl(FaceAnim.NEUTRAL, "If you want Silverlight, but don't have it, you should speak to him.").also {
                    stage =
                        END_DIALOGUE
                }
            4 -> end().also { openNpcShop(player, npc.id) }
            5 -> npcl(FaceAnim.NOD_YES, "Ok, well, if you change your mind, do pop back.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SiegfriedErkleDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIEGFRIED_ERKLE_933)
    }
}
