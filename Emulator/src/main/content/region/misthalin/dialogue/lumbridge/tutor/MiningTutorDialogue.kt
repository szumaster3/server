package content.region.misthalin.dialogue.lumbridge.tutor

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MiningTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("Can you teach me the basics of mining please?", "Are there any mining related quests?", "Goodbye.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Can you teach me the basics of mining please?",
                        ).also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Are there are mining related quests?").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "If you want to know what ore's in a rock before you",
                    "mine it, right-click the rock and select prospect from the",
                    "menu, it will take a little time, but you'll find out what's",
                    "in the rock before you mine.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You can also tell the ore you'll get from the colour of",
                    "the rock.",
                ).also { stage++ }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "To mine, simply click on the rock to mine it, but make",
                    "sue you have your pick with you.",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When you have a full inventory, take it to the bank,",
                    "you can find it on the roof of the castle in Lumbridge.",
                ).also {
                    stage++
                }
            14 ->
                options(
                    "Can you teach me the basics of mining please?",
                    "Are there any mining related quests?",
                    "Goodbye.",
                ).also {
                    stage =
                        0
                }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh yes, if you haven't already, speak to Doric who can",
                    "be found around the anvils north of Falador. I'm sure",
                    "he can help you out.",
                ).also {
                    stage++
                }
            21 ->
                options(
                    "Can you teach me the basics of mining please?",
                    "Are there any mining related quests?",
                    "Goodbye.",
                ).also {
                    stage =
                        0
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MINING_TUTOR_4902)
    }
}
