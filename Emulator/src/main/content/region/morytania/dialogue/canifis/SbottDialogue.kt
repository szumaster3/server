package content.region.morytania.dialogue.canifis

import content.global.skill.crafting.Tanning
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SbottDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "Hello stranger. Would you like to me to tan any hides for you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HAPPY,
                    "Soft leather - 1 gp per hide",
                    "Hard leather - 3 gp per hide",
                    "Snakeskins - 20 gp per hide",
                    "Dragon leather - 20 gp per hide.",
                ).also {
                    stage++
                }

            1 -> {
                var hasHides = false
                for (tanningProduct in Tanning.values()) {
                    if (inInventory(player, tanningProduct.item)) {
                        hasHides = true
                        break
                    }
                }
                if (hasHides) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "I see you have brought me some hides. Would you like me to tan them for you?",
                    ).also {
                        stage =
                            10
                    }
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "No thanks, I haven't any hides.").also { stage = END_DIALOGUE }
                }
            }

            10 -> options("Yes please.", "No thanks.").also { stage++ }

            11 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HAPPY, "Yes please.").also { stage = 12 }
                    2 -> playerl(FaceAnim.NEUTRAL, "No thanks.").also { stage = 13 }
                }

            12 -> end().also { Tanning.open(player, NPCs.SBOTT_1041) }
            13 -> npcl(FaceAnim.FRIENDLY, "Very well, @g[sir,madam], as you wish.").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SbottDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SBOTT_1041)
    }
}
