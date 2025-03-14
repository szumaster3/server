package content.region.misc.handlers

import core.game.node.entity.Entity
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class Crandor :
    MapZone("crandor", true),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(entity: Entity): Boolean {
        return super.enter(entity)
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        register(ZoneBorders(2813, 3223, 2864, 3312))
    }
}
