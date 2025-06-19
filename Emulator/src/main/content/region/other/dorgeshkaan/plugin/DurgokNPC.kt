package content.region.other.dorgeshkaan.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Durgok NPC.
 */
@Initializable
class DurgokNPC @JvmOverloads constructor(
    id: Int = NPCs.DURGOK_5794, location: Location? = null
) : AbstractNPC(id, location) {
    private val forceChat = arrayOf(
        "Burgers!",
        "Hot frogburgers!",
        "Frogburgers!",
        "Frog in a bun!",
        "Tasty frogburgers!",
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DurgokNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DURGOK_5794)
    }
}