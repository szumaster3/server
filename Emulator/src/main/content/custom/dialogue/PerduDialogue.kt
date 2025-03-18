package content.custom.dialogue

import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class PerduDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.OLD_DEFAULT,
            "Hello. I've found some stuff that I think is yours. You can pay me with cash you're carrying. Would you like to buy anything from me?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Show me what you've found.",
                    "No thanks.",
                    "Why do you charge for returning lost items?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> end().also { openInterface(player, Components.LOST_PROPERTY_834) }
                    2 -> player("No thanks.").also { stage = 6 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "Why do you charge for returning lost items?").also { stage++ }
                    4 -> playerl(FaceAnim.NEUTRAL, "About items that break when people kill me...").also { stage = 4 }
                }
            2 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You could get your stuff back elsewhere, sure. Now, maybe it'd be free, or maybe you'd have to pay someone, but you'd definitely have to go on a journey to get it back.",
                ).also {
                    stage++
                }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "So I'm earning my fee by saving you a trip. Do you want to have a look?").also { stage = 0 }
            4 -> player(FaceAnim.HALF_ASKING, "Can you fix them?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "For a fee, yes. Just show me the broken item.").also { stage = END_DIALOGUE }
            6 -> npcl(FaceAnim.OLD_DEFAULT, "Fair enough.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PERDU_8592)
}
