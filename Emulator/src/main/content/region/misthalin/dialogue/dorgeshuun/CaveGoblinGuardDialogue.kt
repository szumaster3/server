package content.region.misthalin.dialogue.dorgeshuun

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CaveGoblinGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((0..2).random()) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Have a nice day!").also { stage = END_DIALOGUE }
            1 -> npc(FaceAnim.OLD_NORMAL, "I'm keeping an eye on you, surface-dweller.").also { stage = END_DIALOGUE }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Surface-dweller! You will never find your way into the city of ",
                    "Dorgesh-Kaan!",
                )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_ASKING, "Isn't it through that big door?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    " Well, yes, but you'll never find your way in!",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CaveGoblinGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAVE_GOBLIN_GUARD_2073, NPCs.CAVE_GOBLIN_GUARD_2074)
    }
}
