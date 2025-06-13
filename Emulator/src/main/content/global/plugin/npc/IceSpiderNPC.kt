package content.global.plugin.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class IceSpiderNPC(id: Int = 0, location: Location? = null, ) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any, ): AbstractNPC = IceSpiderNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ICE_SPIDER_64)
}
