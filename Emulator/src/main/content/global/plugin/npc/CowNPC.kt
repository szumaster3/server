package content.global.plugin.npc

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class CowNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        CowNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.COW_81,
        NPCs.COW_397,
        NPCs.COW_955,
        NPCs.COW_1767,
        NPCs.COW_3309
    )

    override fun handleTickActions() {
        if (--chatDelay > 0) return

        if (RandomFunction.roll(20)) {
            sendChat(this, "Moo")
        }

        chatDelay = randomDelay()
    }

    private fun randomDelay() = RandomFunction.random(10, 20)
}
