package content.global.skill.agility.courses.pyramid

import content.global.skill.agility.courses.pyramid.AgilityPyramidCourse.Companion.addConfig
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.MovementHook
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin

class AgilityPyramidZone :
    MapZone("agility pyramid", true),
    Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e
            addConfig(player, 10872, 1, true)
            addConfig(player, 10873, 3, true)
        }
        return super.enter(e)
    }

    override fun move(
        e: Entity,
        loc: Location,
        dest: Location,
    ): Boolean {
        if (!e.locks.isMovementLocked && e is Player) {
            val hook = LOCATION_TRAPS[loc]
            if (hook != null) {
                e.setDirection(Direction.getLogicalDirection(loc, dest))
                return hook.handle(e, loc)
            }
        }
        return super.move(e, loc, dest)
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        register(ZoneBorders(3352, 2826, 3378, 2854))
        register(ZoneBorders(3007, 4672, 3071, 4735))
    }

    companion object {
        @JvmStatic
        public val LOCATION_TRAPS: Map<Location, MovementHook> = HashMap()
    }
}
