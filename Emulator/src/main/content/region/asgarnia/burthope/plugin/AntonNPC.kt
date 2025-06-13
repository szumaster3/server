package content.region.asgarnia.burthope.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class AntonNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Armour and axes to suit your needs.",
        "Imported weapons from the finest smithys around the lands!",
        "Ow, my toe! That armour is heavy."
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        AntonNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.ANTON_4295)

    override fun handleTickActions() {
        if (!isPlayerNearby(10)) return

        if (--chatDelay <= 0) {
            if (RandomFunction.random(5) == 0) {
                sendChat(forceChat.random())
            }
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(10, 30)
}