package content.region.karamja.dialogue.apeatoll.marim

import core.api.toIntArray
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ElderGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val ids = 4025..4031

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        var outside = true

        var cornerGuard = false

        if (player.equipment.containsAtLeastOneItem(ids.toIntArray())) {
            if (outside) {
                npc(FaceAnim.OLD_ANGRY1, "Grrr ... What do you want?").also { stage = 10 }
            } else if (cornerGuard) {
                npc(FaceAnim.OLD_ANGRY1, "Grrr ... What do you want?").also { stage = 20 }
            } else {
                npc(FaceAnim.OLD_ANGRY1, "Move!").also { stage = 99 }
            }
        } else {
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            10 -> playerl(FaceAnim.ASKING, "I must speak with Awowogei on a subject of great import.").also { stage++ }
            11 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Only the Captain of the Monkey Guard or those he authorizes may enter this building. You will need his permission to enter.",
                ).also {
                    stage++
                }

            12 -> player(FaceAnim.ASKING, "Who is the Captain of the Monkey Guard?").also { stage++ }
            13 -> player(FaceAnim.ASKING, "He goes by the name of Kruk").also { stage = 99 }

            20 -> player(FaceAnim.ASKING, "I would like to leave now.").also { stage++ }
            21 -> npc(FaceAnim.OLD_NORMAL, "As you wish.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ElderGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELDER_GUARD_1461, NPCs.ELDER_GUARD_1462)
    }
}
