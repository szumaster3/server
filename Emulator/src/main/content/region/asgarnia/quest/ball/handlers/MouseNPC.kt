package content.region.asgarnia.quest.ball.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import org.rs.consts.NPCs

/**
 * Represents Mouse NPC in Witch's house.
 * @author Ethan Kyle Millard (March 15, 2020, 10:56 AM)
 */
class MouseNPC : AbstractNPC {
    var player: Player? = null
    private var endTime = 0

    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location) {
        endTime = (ticks + 4 / 0.6).toInt()
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return MouseNPC(id, location)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (player!!.getAttribute<Any?>("mouse_out") == null) {
            clear()
        }
        if (ticks > endTime) {
            clear()
        }
        if (!player!!.isActive || player!!.location.getDistance(getLocation()) > 8) {
            clear()
        }
    }

    override fun clear() {
        super.clear()
        player!!.removeAttribute("mouse_out")
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOUSE_901)
    }
}
