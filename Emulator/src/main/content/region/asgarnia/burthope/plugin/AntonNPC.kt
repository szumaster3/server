package content.region.asgarnia.burthope.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the AntonNPC.
 */
@Initializable
class AntonNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val forceChat: Array<String> = arrayOf(
        "Armour and axes to suit your needs.",
        "Imported weapons from the finest smithys around the lands!",
        "Ow, my toe! That armour is heavy."
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = AntonNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.ANTON_4295)

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }
}
