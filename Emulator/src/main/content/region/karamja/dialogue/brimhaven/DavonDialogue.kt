package content.region.karamja.dialogue.brimhaven

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DavonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.SUSPICIOUS, "Pssst! Come here if you want to do some amulet", "trading.")
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
                    "What do you mean pssst?",
                    "Why don't you ever restock some types of amulets?",
                ).also {
                    stage++
                }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "What are you selling?").also { stage = 10 }
                    2 -> player(FaceAnim.SUSPICIOUS, "What do you mean pssst?").also { stage = 20 }
                    3 ->
                        player(FaceAnim.HALF_ASKING, "Why don't you ever restock some types of amulets?").also {
                            stage = 30
                        }
                }

            10 -> {
                end()
                openNpcShop(player, NPCs.DAVON_588)
            }

            20 -> npc(FaceAnim.SUSPICIOUS, "Errr, I was...I was clearing my throat.").also { stage = END_DIALOGUE }
            30 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Some of these amulets are very hard to get. I have to",
                    "wait until an adventurer supplies me.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DavonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAVON_588)
    }
}
