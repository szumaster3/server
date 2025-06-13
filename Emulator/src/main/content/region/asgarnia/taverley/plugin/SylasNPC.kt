package content.region.asgarnia.taverley.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class SylasNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Selling Magic Beans!",
        "You there, you look like you could use some beans!",
        "All beans must go!",
        "Buy 1 bean get a bean free!",
        "Selling bags of Magic Beans!",
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = SylasNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.SYLAS_5987)

    override fun handleTickActions() {
        if (!isPlayerNearby(15)) return

        if (--chatDelay <= 0) {
            if (RandomFunction.roll(8)) {
                sendChat(this, forceChat.random())
            }
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(10, 30)
}
