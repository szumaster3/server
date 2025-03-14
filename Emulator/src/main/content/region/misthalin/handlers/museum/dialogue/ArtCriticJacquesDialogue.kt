package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ArtCriticJacquesDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "I sit in the sky like a sphinx misunderstood")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I combine a heart of snow to the whiteness of swans;",
                    "I hate the movement that moves the lines;",
                    " ",
                    "And I never cry and I never laugh."
                )
                stage++
            }

            1 -> {
                end()
                npc.faceLocation(Location.create(3257, 3455, 2))
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ArtCriticJacquesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ART_CRITIC_JACQUES_5930)
    }
}
