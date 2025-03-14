package content.region.kandarin.dialogue.stronghold

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HudoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hello there, traveller. Would you like some groceries? I have a large selection.",
                ).also { stage++ }

            1 -> options("I'll take a look.", "No, thank you.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I'll take a look.").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
                }

            3 -> npcl(FaceAnim.OLD_NORMAL, "Great stuff.").also { stage++ }
            4 -> end().also { openNpcShop(player, NPCs.HUDO_600) }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HudoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HUDO_600)
    }
}
