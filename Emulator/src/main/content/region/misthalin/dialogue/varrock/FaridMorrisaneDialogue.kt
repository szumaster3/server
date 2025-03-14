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
class FaridMorrisaneDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello, little boy.")
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
                    "I would prefer it if you didn't speak to me in such a",
                    "manner. I'll have you know I'm an accomplished",
                    "merchant.",
                ).also {
                    stage++
                }
            1 ->
                options(
                    "Calm down, junior.",
                    "Can you help me out with the prices for ores?",
                    "I best go and speak with someone my height.",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Calm down, junior.").also { stage++ }
                    2 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Can you help me out with the prices for ores?",
                        ).also { stage = 16 }
                    3 ->
                        player(FaceAnim.HALF_GUILTY, "I best go and speak with someone more my height.").also {
                            stage = 17
                        }
                }
            10 -> npc(FaceAnim.CHILD_NORMAL, "Don't tell me to calm down! And don't call me 'junior'.").also { stage++ }
            11 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "I'll have you know I am Farid Morrisane, son of Ali",
                    "Morrisane, the worlds greatest merchant!",
                ).also {
                    stage++
                }
            12 -> player(FaceAnim.HALF_GUILTY, "Then why are you here and not him?").also { stage++ }
            13 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "My dad has given me the responsibility of",
                    "expanding our business here.",
                ).also {
                    stage++
                }
            14 -> player(FaceAnim.HALF_GUILTY, "And you're up to the task?").also { stage++ }
            15 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "What a grown up boy you are! Mummy and",
                    "daddy must be very pleased!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            16 -> {
                end()
                GuidePrices.open(player, GuideType.ORES)
            }
            17 ->
                npc(FaceAnim.CHILD_NORMAL, "Then I shall not stop you, mister. I've too", "much work to do.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FaridMorrisaneDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FARID_MORRISANE_ORES_6523)
    }
}
