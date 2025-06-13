package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class HofuthandNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "I've hit the jackpot!",
        "Wow. That's cheap.",
        "This is trading, the likes of which I've never seen",
        "Oh, that didn't sell so well.",
        "Jackpot! I'm in the money now!",
        "Hmmm. If I spend twenty thousand on that, then...",
        "Hahaha! Trading the likes of which I have never seen.",
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        HofuthandNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.HOFUTHAND_ARMOUR_AND_WEAPONS_6527)

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
