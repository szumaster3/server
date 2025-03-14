package content.region.desert.quest.rescue.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import org.rs.consts.NPCs

class LadyKeliNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return LadyKeliNPC(id, location)
    }

    override fun isHidden(player: Player): Boolean {
        return player.getAttribute("keli-gone", 0) > ticks
    }

    override fun getIds(): IntArray {
        return ID
    }

    override fun getWalkRadius(): Int {
        return 2
    }

    companion object {
        private val ID = intArrayOf(NPCs.LADY_KELI_919)
    }
}
