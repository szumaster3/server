package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class MurkyMattNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Sure be a busy place, today.",
        "Yarrr! I'm gonna be rich, I tell ye!",
        "I'm lovin' this Grand Exchange! Arrr!",
        "Arrr! Another good sale!",
        "No! Me prices, they be goin' down!",
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        MurkyMattNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.MURKY_MATT_RUNES_6525)

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
