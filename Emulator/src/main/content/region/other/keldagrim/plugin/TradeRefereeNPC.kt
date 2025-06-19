package content.region.other.keldagrim.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the CartConductorNPC.
 */
@Initializable
class TradeRefereeNPC
@JvmOverloads constructor(id: Int = NPCs.TRADE_REFEREE_2127, location: Location? = null) : AbstractNPC(id, location) {
    private val forceChat =
        arrayOf(
            "Stay inside your triangle!",
            "Who's next?",
            "Next bid please!",
            "Need an offer, now!",
            "Keep the goods flowing!",
            "Keep it civil!",
            "Come on, come on!",
            "Go again please.",
            "Keep the goods flowing!",
            "Hold on there!",
        )
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return TradeRefereeNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRADE_REFEREE_2127)
    }
}
