package content.region.kandarin.dialogue.stronghold

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class GnomeTrainerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val rand = RandomFunction.random(0, 3)
        when (rand) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello there.").also { stage = 0 }
            1 -> player(FaceAnim.HALF_GUILTY, "Hello, what is this place?").also { stage = 3 }
            2 -> player(FaceAnim.HALF_GUILTY, "Hello how are you?").also { stage = 7 }
            3 -> player(FaceAnim.HALF_GUILTY, "This is fun!").also { stage = 10 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "This isn't a grannies' tea party, let's see some sweat",
                    "human. Go! Go! Go!",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "This, my friend, is where we train. Here we improve",
                    "out agility. It's an essential skill.",
                ).also {
                    stage++
                }

            4 -> player(FaceAnim.HALF_GUILTY, "It looks easy enough.").also { stage++ }
            5 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "If you complete the course in order from the slippery",
                    "log to the end, your agility will increase much faster",
                    "than by repeating just one obstacle.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            7 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "I'm amazed by how much humans chat. The sign over",
                    "there says training area, not pointless conversation area.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            10 ->
                npc(FaceAnim.OLD_NORMAL, "This is training soldier. If you want fun go make some", "cocktails.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GnomeTrainerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_TRAINER_162)
    }
}
