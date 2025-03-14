package content.region.misthalin.dialogue.lumbridge.tutor

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CraftingTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Can you teach me the basics of crafting please?").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Firstly, you should know that not all places associated",
                    "with crafting will be marked on your mini map. Some",
                    "take quite a bit of hunting down to find, don't lose",
                    "heart!",
                ).also {
                    stage++
                }
            1 -> player(FaceAnim.HALF_GUILTY, "I see... so where should I start?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When you have a full inventory, take it to the bank,",
                    "you can find it on the roof of this very castle.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CraftingTutorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CRAFTING_TUTOR_4900)
    }
}
