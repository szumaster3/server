package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HistorianMinasDialogue(
    player: Player? = null,
) : Dialogue(player) {


    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npcl(FaceAnim.HALF_GUILTY, "Hello there, welcome to Varrock Museum! Can I help you?")
                stage++
            }

            1 -> {
                playerl(FaceAnim.HALF_GUILTY, "Tell me about the Museum.")
                stage++
            }

            2 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, as you can see we have recently expanded a great deal to cope with the influx of finds from the Dig Site."
                )
                stage++
            }

            3 -> {
                npc(FaceAnim.HALF_GUILTY, "Also, of course, to prepare for the new dig we're", "opening soon.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HISTORIAN_MINAS_5931)
    }
}
