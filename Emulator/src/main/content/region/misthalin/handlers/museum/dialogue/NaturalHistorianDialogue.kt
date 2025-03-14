package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class NaturalHistorianDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Hello there and welcome to the Natural History exhibit of the Varrock Museum!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_GUILTY, "Hello. So what is it you do here?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.HALF_GUILTY, "Well, we look after all of the research in this section.")
                stage++
            }

            2 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "When I'm not doing that, I'm teaching people like yourself about how wonderful the natural world is."
                )
                stage++
            }

            3 -> {
                playerl(FaceAnim.HALF_GUILTY, "Nice. So then, I've got a few questions for you.")
                stage++
            }

            4 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, fire away and I'll give you an insight into the exciting world of animals."
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return NaturalHistorianDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NATURAL_HISTORIAN_5970)
    }
}
