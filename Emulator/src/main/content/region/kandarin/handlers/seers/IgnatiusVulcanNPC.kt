package content.region.kandarin.handlers.seers

import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.NPCs

@Initializable
class IgnatiusVulcanNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private var lastFire = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return IgnatiusVulcanNPC(id, location)
    }

    override fun tick() {
        if (lastFire < ticks) {
            createFire(this, getLocation())
            lastFire = ticks + RandomFunction.random(50, 200)
        }
        super.tick()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IGNATIUS_VULCAN_4946)
    }

    companion object {
        private val ANIMATION = Animation(Animations.TINDERBOX_733, Priority.HIGH)

        fun createFire(
            npc: NPC,
            location: Location?,
        ) {
            npc.walkingQueue.reset()
            npc.animator.forceAnimation(ANIMATION)
            if (getObject(location!!) == null) {
                val fire = Scenery(2732, location)
                SceneryBuilder.add(fire, RandomFunction.random(100, 130))
                npc.faceLocation(fire.location)
            }
        }
    }
}
