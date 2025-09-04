package content.region.fremennik.rellekka.npc

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles the Rock Crab NPC.
 */
@Initializable
class RockCrabNPC(id: Int = -1, location: Location? = null) : AbstractNPC(id, location, false) {

    private var aggressor = false
    private var target: Player? = null

    init {
        if (id != -1 && location != null) {
            aggressiveHandler = AggressiveHandler(this, AGGRO_BEHAVIOR)
            isAggressive = true
            isWalks = false
        }
    }

    override fun onAttack(target: Entity) {
        if (target is Player) {
            this.aggressor = true
            this.target = target
            if (id == originalId) {
                transform(originalId - 1)
            }
        }
    }

    override fun handleTickActions() {
        super.handleTickActions()

        val nearbyPlayers = RegionManager.getLocalPlayers(this, 3)
            .filterIsInstance<Player>()

        if (nearbyPlayers.isNotEmpty() && !inCombat() && !aggressor) {
            aggressor = true
            transform(originalId - 1)

            val nearest = nearbyPlayers.minByOrNull { it.location.getDistance(location) }
            nearest?.let { target = it }
        }

        val closestDistance = nearbyPlayers.minOfOrNull { it.location.getDistance(location) } ?: Double.MAX_VALUE
        if (aggressor && !inCombat() && (closestDistance > 12 || isInvisible)) {
            reTransform()
            aggressor = false
            target = null
            walkingQueue.reset()
            isWalks = false
        }
    }

    override fun sendImpact(state: BattleState) {
        val hit = state.estimatedHit
        if ((hit >= 3 && RandomFunction.random(30) != 5) || (hit == 2 && RandomFunction.random(30) != 5)) {
            state.estimatedHit = 0
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return RockCrabNPC(id, location)
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.ROCKS_1266,
        NPCs.ROCKS_1268,
        NPCs.BOULDER_2453,
        NPCs.ROCK_2890
    )

    companion object {
        private val AGGRO_BEHAVIOR = object : AggressiveBehavior() {
            override fun ignoreCombatLevelDifference(): Boolean = true

            override fun canSelectTarget(entity: Entity, target: Entity): Boolean {
                return target is Player &&
                        super.canSelectTarget(entity, target) &&
                        entity.location.withinDistance(target.location, 3)
            }
        }
    }
}
