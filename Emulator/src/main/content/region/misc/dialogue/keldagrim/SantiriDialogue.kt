package content.region.misc.dialogue.keldagrim

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SantiriDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_NORMAL, "Welcome, human, to the Quality Weapons Shop!", "Can I interest you in a purchase?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, I'm looking for some weapons.", "No thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.GUILTY, "Yes, I'm looking for some weapons.").also { stage++ }
                    2 ->
                        playerl(FaceAnim.NEUTRAL, "No thanks.").also {
                            end()
                            stage = END_DIALOGUE
                        }
                }
            2 -> {
                end()
                openNpcShop(player, NPCs.SANTIRI_2152)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SantiriDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SANTIRI_2152)
    }
}
