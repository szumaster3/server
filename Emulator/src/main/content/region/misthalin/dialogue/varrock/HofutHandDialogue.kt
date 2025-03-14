package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.ge.GuidePrices
import core.game.ge.GuidePrices.GuideType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HofutHandDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "What? Oh, hello. I was deep in thought. Did",
                    "you say something?",
                ).also { stage++ }
            1 ->
                options(
                    "Do you know about the prices for armour and weapons?",
                    "I didn't say anything at all.",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 ->
                        player(FaceAnim.HALF_GUILTY, "Do you know about the prices for armour and weapons?").also {
                            stage =
                                3
                        }
                    2 -> player(FaceAnim.HALF_GUILTY, "I didn't say anything at all.").also { stage = END_DIALOGUE }
                }
            3 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "I thought you at least said. 'Hello' I must be",
                    "going mad. Do you think I'm going mad?",
                ).also {
                    stage++
                }
            4 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Oh, most definitely. You should see a doctor before it's",
                    "too late.",
                ).also {
                    stage++
                }
            5 -> {
                end()
                GuidePrices.open(player, GuideType.WEAPONS_AND_ARMOUR)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return HofutHandDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOFUTHAND_ARMOUR_AND_WEAPONS_6527)
    }
}
