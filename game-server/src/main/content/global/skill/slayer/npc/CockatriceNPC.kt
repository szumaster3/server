package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import content.global.skill.slayer.items.MirrorShieldHandler
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * The type Cockatrice npc.
 */
@Initializable
class CockatriceNPC : AbstractNPC {
    /**
     * Instantiates a new Cockatrice npc.
     */
    constructor() : super(0, null)

    /**
     * Instantiates a new Cockatrice npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return CockatriceNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return MirrorShieldHandler
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        MirrorShieldHandler.checkImpact(state)
    }

    override fun getIds(): IntArray {
        return Tasks.COCKATRICES.npcs
    }
}
