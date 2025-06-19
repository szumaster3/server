package content.region.misthalin.varrock.plugin.ge

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Hofuthand NPC.
 */
@Initializable
class HofuthandNPC @JvmOverloads constructor(
    id: Int = NPCs.HOFUTHAND_ARMOUR_AND_WEAPONS_6527, location: Location? = null
) : AbstractNPC(id, location) {
    private val forceChat = arrayOf(
        "I've hit the jackpot!",
        "Wow. That's cheap.",
        "This is trading, the likes of which I've never seen",
        "Oh, that didn't sell so well.",
        "Jackpot! I'm in the money now!",
        "Hmmm. If I spend twenty thousand on that, then...",
        "Hahaha! Trading the likes of which I have never seen."
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return HofuthandNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOFUTHAND_ARMOUR_AND_WEAPONS_6527)
    }
}