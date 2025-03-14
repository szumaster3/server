package content.region.misc.dialogue.zanaris

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GateKeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("What happened to the old man who used to", "be the doorman?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_CALM_TALK1,
                    "You mean my father? He went into retirement.",
                    "I've taken over the family business.",
                ).also {
                    stage++
                }
            1 -> player("Your father? But you don't look anything like him!").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_CALM_TALK1,
                    "No. Fortunately for me, I inherited my good looks from",
                    "my mother.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GateKeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GATEKEEPER_3307)
    }
}
