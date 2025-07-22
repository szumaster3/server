package content.region.misthalin.varrock.plugin.ge.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Murky Matt NPC.
 */
@Initializable
class MurkyMattNPC @JvmOverloads constructor(id: Int = NPCs.MURKY_MATT_RUNES_6525, location: Location? = null) : AbstractNPC(id, location) {

    private val forceChat = arrayOf(
        "Sure be a busy place, today.",
        "Yarrr! I'm gonna be rich, I tell ye!",
        "I'm lovin' this Grand Exchange! Arrr!",
        "Arrr! Another good sale!",
        "No! Me prices, they be goin' down!"
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return MurkyMattNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MURKY_MATT_RUNES_6525)
    }
}