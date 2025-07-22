package content.region.asgarnia.burthope.npc

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
    private var timer = 0L
    private val forceChat: Array<String> = arrayOf(
        "Armour and axes to suit your needs.",
        "Imported weapons from the finest smithys around the lands!",
        "Ow, my toe! That armour is heavy."
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = AntonNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.ANTON_4295)

    override fun tick() {
        val now = System.currentTimeMillis()
        if (now < timer) return
        if (RandomFunction.random(100) < 3) {
            sendChat(forceChat.random())
            timer = now + 3000
        }
    }
}
