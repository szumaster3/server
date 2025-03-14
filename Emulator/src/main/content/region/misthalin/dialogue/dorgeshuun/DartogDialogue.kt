package content.region.misthalin.dialogue.dorgeshuun

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DartogDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Hello, surface-dweller.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Who are you?",
                    "Can you show me the way to the mine?",
                    "Can you show me the way to Lumbridge Castle cellar?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Who are you?").also { stage++ }
                    2 ->
                        npcl(FaceAnim.OLD_NORMAL, "Of course! You're always welcome in our mines!").also {
                            stage =
                                END_DIALOGUE
                        }
                    3 ->
                        player(
                            FaceAnim.ASKING,
                            "Can you show me the way to Lumbridge Castle cellar?",
                        ).also { stage = 6 }
                }
            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The council posted me here to guard this new tunnel. I can also give you directions through the tunnels. A hero like you is always welcome in our mines!",
                ).also {
                    stage++
                }
            3 ->
                options(
                    "Can you show me the way to the mine?",
                    "Can you show me the way to Lumbridge Castle cellar?",
                    "Maybe some other time",
                ).also { stage++ }
            4 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Can you show me the way to the mine?").also { stage = 5 }
                    2 ->
                        player(
                            FaceAnim.ASKING,
                            "Can you show me the way to Lumbridge Castle cellar?",
                        ).also { stage = 6 }
                    3 -> player(FaceAnim.FRIENDLY, "Maybe some other time.").also { stage = END_DIALOGUE }
                }
            5 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Of course! You're always welcome in our mines!",
                ).also { stage = END_DIALOGUE }
            6 -> npc(FaceAnim.OLD_NORMAL, "Of course!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DartogDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DARTOG_4314)
    }
}
