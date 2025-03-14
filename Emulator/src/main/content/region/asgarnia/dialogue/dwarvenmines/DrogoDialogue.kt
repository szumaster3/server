package content.region.asgarnia.dialogue.dwarvenmines

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DrogoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "'Ello. Welcome to my Mining shop, friend.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Do you want to trade?",
                    "Hello, shorty.",
                    "Why don't you ever restock ores and bars?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Do you want to trade?").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "Hello, shorty.").also { stage = 3 }
                    3 -> player(FaceAnim.FRIENDLY, "Why don't you ever restock ores and bars?").also { stage = 4 }
                }
            2 -> {
                end()
                openNpcShop(player, NPCs.DROGO_DWARF_579)
            }
            3 ->
                npc(
                    FaceAnim.OLD_ANGRY1,
                    "I may be short, but at least I've got manners.",
                ).also { stage = END_DIALOGUE }
            4 ->
                npc(FaceAnim.OLD_DEFAULT, "The only ores and bars I sell are those sold to me.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DrogoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DROGO_DWARF_579)
    }
}
