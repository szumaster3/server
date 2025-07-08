package content.global.skill.slayer.npc

import content.global.skill.slayer.Tasks
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Bug swarm npc.
 */
@Initializable
class BugSwarmNPC : AbstractNPC {
    /**
     * Instantiates a new Bug swarm npc.
     */
    constructor() : super(0, null)


    /**
     * Instantiates a new Bug swarm npc.
     *
     * @param id       the id
     * @param location the location
     */
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BugSwarmNPC(id, location)
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        if (state.attacker is Player) {
            val player = state.attacker as Player
            if (!player.equipment.containsItem(LIT_LANTERN)) {
                if (state.estimatedHit > -1) {
                    state.estimatedHit = 0
                }
                if (state.secondaryHit > -1) {
                    state.secondaryHit = 0
                }
            }
        }
    }

    override fun getIds(): IntArray {
        return Tasks.HARPIE_BUG_SWARMS.npcs
    }

    companion object {
        private val LIT_LANTERN = Item(Items.LIT_BUG_LANTERN_7053)
    }
}
