package content.region.misthalin.varrock.plugin.museum

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class MuseumGuardNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Another boring day.",
        "Nothing new there.",
        "Keep 'em coming!",
        "Don't daudle there!"
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        MuseumGuardNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.MUSEUM_GUARD_5941,
        NPCs.MUSEUM_GUARD_5942,
        NPCs.MUSEUM_GUARD_5943
    )

    override fun handleTickActions() {
        if (!isPlayerNearby(15)) return

        if (--chatDelay <= 0) {
            if (RandomFunction.roll(8)) {
                sendChat(this, forceChat.random())
            }
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(20, 40)
}
