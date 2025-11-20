package content.region.karamja.tzhaar_city.plugin

import core.api.replaceScenery
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Scenery

@Initializable
class TzHaarCity : MapZone("TzHaar City", true, ZoneRestriction.CANNON), Plugin<Any?> {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        if(e is Player) {
            replaceScenery(core.game.node.scenery.Scenery(Scenery.TUNNEL_ENTRANCE_31221, Location(2526,5181), 0), Scenery.CAVE_ENTRANCE_31292, -1, Direction.NORTH_WEST, Location(2526,5181))
            replaceScenery(core.game.node.scenery.Scenery(Scenery.TUNNEL_ENTRANCE_31221, Location(2476, 5213), 0), Scenery.CAVE_ENTRANCE_31292, -1, Direction.NORTH, Location(2476, 5213))
        }
        return true
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? = null

    override fun configure() {
        register(ZoneBorders(2369, 5054, 2549, 5188))
        // TzHaar City
        register(ZoneBorders.forRegion(10064))
        // TzHaar Library
        register(ZoneBorders.forRegion(9809))
    }
}
