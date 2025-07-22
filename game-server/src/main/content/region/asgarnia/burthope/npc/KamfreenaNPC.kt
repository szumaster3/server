package content.region.asgarnia.burthope.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Kamfreena NPC.
 */
@Initializable
class KamfreenaNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    var ticks = 0
    private val forceChat: Array<String> =
        arrayOf(
            "When you aim for perfection, you discover it's a moving target.",
            "Patience and persistence can bring down the tallest tree.",
            "Be master of mind rather than mastered by mind.",
            "A reflection on a pool of water does not reveal its depth.",
            "Life isn't fair, that doesn't mean you can't win.",
        )

    override fun construct(id: Int, location: Location, vararg objects: Any, ): AbstractNPC = KamfreenaNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.KAMFREENA_4289)

    override fun tick() {
        ticks++
        if (ticks < 20) return

        ticks = 0
        if (RandomFunction.random(100) < 3) {
            sendChat(forceChat[RandomFunction.random(forceChat.size)])
        }
        super.tick()
    }
}
