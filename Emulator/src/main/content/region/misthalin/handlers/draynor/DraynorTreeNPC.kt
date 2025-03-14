package content.region.misthalin.handlers.draynor

import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.AbstractNPC
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class DraynorTreeNPC : AbstractNPC {
    private var attackDelay = 0

    constructor() : super(0, null, false)

    private constructor(id: Int, location: Location) : super(id, location, false)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DraynorTreeNPC(id, location)
    }

    override fun tick() {
        val players = getLocalPlayers(this, 1)
        if (players.size != 0) {
            if (attackDelay < ticks) {
                for (p in players) {
                    faceTemporary(p, 2)
                    animator.forceAnimation(ANIMATION)
                    val hit = RandomFunction.random(2)
                    p.impactHandler.manualHit(this, hit, if (hit > 0) HitsplatType.NORMAL else HitsplatType.MISS)
                    attackDelay = ticks + 3
                    p.animate(p.properties.defenceAnimation)
                    return
                }
            }
        }
        super.tick()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UNDEAD_TREE_5208, NPCs.TREE_5207)
    }

    companion object {
        private val ANIMATION = Animation(73, Priority.HIGH)
    }
}
