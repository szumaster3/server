package content.region.misthalin.quest.dragon.handlers.npc

import content.region.misthalin.quest.dragon.DragonSlayer
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.Location
import org.rs.consts.NPCs

class MazeDemonNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MazeDemonNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer.location.withinDistance(LOCATION)) {
            if (killer is Player) {
                GroundItemManager.create(DragonSlayer.GREEN_KEY, getLocation(), killer)
            }
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LESSER_DEMON_82)

    companion object {
        private val LOCATION: Location = Location.create(2936, 9652, 0)
    }
}
