package content.region.misthalin.varrock.plugin.ge

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Bob Barter NPC.
 */
@Initializable
class BobBarterNPC @JvmOverloads constructor(id: Int = NPCs.BOB_BARTER_HERBS_6524, location: Location? = null) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Now, what should I buy?",
        "I'm in the money!",
        "I could have sworn that would have worked.",
        "Hope this item sells well.",
        "Please, please, please work."
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BobBarterNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOB_BARTER_HERBS_6524)
    }
}