package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MuseumGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc(FaceAnim.HALF_GUILTY, "Hello there. Come to see the new museum?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                player(FaceAnim.HALF_GUILTY, "Yes, how do I get in?")
                stage++
            }

            1 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, the main entrance is 'round the front. Just head",
                    "west then north slightly, you can't miss it!"
                )
                stage++
            }

            2 -> {
                player(FaceAnim.HALF_GUILTY, "What about these doors?")
                stage++
            }

            3 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "They're primarily for the workmen bringing finds from the",
                    "Dig Site, but you can go through if you want."
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return MuseumGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUSEUM_GUARD_5943, NPCs.MUSEUM_GUARD_5941)
    }
}
