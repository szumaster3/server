package content.region.karamja.shilo.npc

import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.AbstractNPC
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class FlyTrapNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    private var delay = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = FlyTrapNPC(id, location)

    override fun tick() {
        val players = RegionManager.getLocalPlayers(this, 1)
        if (players.isNotEmpty() && delay < GameWorld.ticks) {
            val p = players.first()
            faceTemporary(p, 2)
            animator.forceAnimation(ANIMATION)
            val hit = RandomFunction.random(2)
            p.impactHandler.manualHit(
                this,
                hit,
                if (hit > 0) ImpactHandler.HitsplatType.NORMAL else ImpactHandler.HitsplatType.MISS
            )
            delay = GameWorld.ticks + 3
            p.animate(p.properties.defenceAnimation)
            return
        }
        super.tick()
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FLY_TRAP_151)

    companion object {
        private val ANIMATION = Animation(1247, Animator.Priority.HIGH)
    }
}