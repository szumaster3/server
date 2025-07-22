package content.region.misthalin.varrock.plugin.ge.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Farid Morrisane NPC.
 */
@Initializable
class FaridMorrisaneNPC @JvmOverloads constructor(
    id: Int = NPCs.FARID_MORRISANE_ORES_6523, location: Location? = null
) : AbstractNPC(id, location) {
    private val forceChat = arrayOf(
        "I can make so much money here!",
        "My father shall be so pleased.",
        "Woo hoo! What a sale!",
        "What shall I trade next....",
        "Hmm. If I divide 20 and take off 50%..."
    )

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return FaridMorrisaneNPC(id, location)
    }

    override fun tick() {
        if (RandomFunction.random(100) < 15) {
            sendChat(forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FARID_MORRISANE_ORES_6523)
    }
}