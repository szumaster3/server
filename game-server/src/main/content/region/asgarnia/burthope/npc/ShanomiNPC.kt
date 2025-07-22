package content.region.asgarnia.burthope.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the ShanomiNPC.
 */
@Initializable
class ShanomiNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    private var timer = 0L
    private val forceChat: Array<String> =
        arrayOf(
            "Those things which cannot be seen, perceive them.",
            "Do nothing which is of no use.",
            "Think not dishonestly.",
            "The Way in training is.",
            "Gain and loss between you must distinguish.",
            "Trifles pay attention even to.",
            "Way of the warrior this is.",
            "Acquainted with every art become.",
            "Ways of all professions know you.",
            "Judgment and understanding for everything develop you must.",
        )

    override fun construct(id: Int, location: Location, vararg objects: Any, ): AbstractNPC = ShanomiNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.SHANOMI_4290)

    override fun handleTickActions() {
        val now = System.currentTimeMillis()
        if (now < timer) return
        if (RandomFunction.random(100) < 3) {
            sendChat(forceChat[RandomFunction.random(forceChat.size)])
            timer = now + 3000
        }
    }
}
