package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * The type Cave bug npc.
 */
@Initializable
class CaveBugNPC : AbstractNPC {
    /**
     * Instantiates a new Cave bug npc.
     */
    constructor() : super(0, null)

    /**
     * Instantiates a new Cave bug npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val p = killer
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return CaveBugNPC(id, location)
    }

    override fun getIds(): IntArray {
        return Tasks.CAVE_BUG.npcs
    }
}
