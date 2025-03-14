package content.region.karamja.handlers.npc

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
class FlyTrapNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private var attackDelay = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return FlyTrapNPC(id, location)
    }

    override fun tick() {
        val players = getLocalPlayers(this, 1)
        if (players.size != 0) {
            if (attackDelay < ticks) {
                for (p in players) {
                    faceTemporary(p, 2)
                    animator.forceAnimation(Animation(1247, Priority.HIGH))
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
        return intArrayOf(NPCs.FLY_TRAP_151)
    }
}
