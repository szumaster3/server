package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * The type Desert lizard npc.
 */
@Initializable
class DesertLizardNPC : AbstractNPC {
    /**
     * Instantiates a new Desert lizard npc.
     */
    constructor() : super(0, null)

    /**
     * Instantiates a new Desert lizard npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DesertLizardNPC(id, location)
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        var lifepoints = getSkills().lifepoints

        if (state.estimatedHit > -1) {
            lifepoints -= state.estimatedHit
            if (lifepoints < 1) {
                state.estimatedHit = lifepoints - 1
            }
            if (state.estimatedHit < 0) {
                state.estimatedHit = 0
                getSkills().lifepoints = 2
            }
        }

        if (state.secondaryHit > -1) {
            lifepoints -= state.secondaryHit
            if (lifepoints < 1) {
                state.secondaryHit = lifepoints - 1
            }
            if (state.secondaryHit < 0) {
                state.secondaryHit = 0
            }
        }
    }

    override fun getIds(): IntArray {
        return Tasks.DESERT_LIZARDS.npcs
    }
}
