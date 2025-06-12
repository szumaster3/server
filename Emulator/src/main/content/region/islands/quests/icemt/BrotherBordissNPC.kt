package content.region.islands.quests.icemt

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BrotherBordissNPC(id: Int = 0, location: Location? = null, ) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        content.region.islands.quests.icemt.BrotherBordissNPC(id, location)

    override fun getIds(): IntArray = content.region.islands.quests.icemt.BrotherBordissNPC.Companion.ID

    companion object {
        private val ID = intArrayOf(NPCs.BROTHER_BORDISS_7724)
    }
}