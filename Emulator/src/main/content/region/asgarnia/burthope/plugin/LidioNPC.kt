package content.region.asgarnia.burthope.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class LidioNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Potatoes are filling and healthy too!",
        "Come try my lovely pizza or maybe some fish!",
        "Stew to fill the belly, on sale here!"
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = LidioNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.LIDIO_4293)

    override fun handleTickActions() {
        if (!isPlayerNearby(15)) return

        if (--chatDelay <= 0) {
            if (RandomFunction.random(8) == 0) {
                sendChat(forceChat.random())
            }
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(10, 30)
}