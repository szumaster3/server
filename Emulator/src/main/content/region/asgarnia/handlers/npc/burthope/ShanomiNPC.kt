package content.region.asgarnia.handlers.npc.burthope

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class ShanomiNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
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

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return ShanomiNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHANOMI_4290)
    }

    override fun tick() {
        if (RandomFunction.random(35) == 5) {
            sendChat(forceChat[RandomFunction.random(forceChat.size)])
        }
        super.tick()
    }
}
