package content.region.karamja.handlers.tzhaar

import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class Tzhaar :
    MapZone("Tzhaar zone", true, ZoneRestriction.CANNON),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        register(ZoneBorders(2369, 5054, 2549, 5188))
    }
}
