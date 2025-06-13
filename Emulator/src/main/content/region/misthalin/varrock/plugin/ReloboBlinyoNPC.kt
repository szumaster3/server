package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class ReloboBlinyoNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Such a joy it is to be in this building.",
        "Hmmm. These numbers don't quite add up.",
        "My people shall be so proud!",
        "Yes! Yet another great deal.",
        "Just one more offer! One more!",
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        ReloboBlinyoNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.RELOBO_BLINYO_LOGS_6526)

    override fun handleTickActions() {
        if (!isPlayerNearby(12)) return

        if (--chatDelay <= 0) {
            if (RandomFunction.roll(8)) {
                sendChat(this, forceChat.random())
            }
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(30, 90)
}
