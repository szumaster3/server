package content.region.tirannwn.quest.roving_elves.handlers

import core.api.impact
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class IsafdarArea :
    MapZone("Isafdar", true),
    Plugin<Any?> {
    private val leafTrapPit = Location(2336, 9656, 0)
    private val wireTraps = arrayOf(Location(2215, 3154, 0), Location(2220, 3153, 0), Location(2285, 3188, 0))
    private val leafTraps = arrayOf(Location(2274, 3174, 0))
    private val stickTraps = arrayOf(Location(2236, 3181, 0), Location(2201, 3169, 0), Location(2276, 3163, 0))

    override fun configure() {
        register(ZoneBorders(2178, 3150, 2304, 3196))
    }

    override fun locationUpdate(
        e: Entity,
        last: Location,
    ) {
        if (e is Player) {
            val player = e
            if (leafTraps.contains(player.location)) {
                sendMessage(player, "You fall through and onto some spikes.")
                impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
                player.teleport(leafTrapPit)
            } else if (stickTraps.contains(player.location)) {
                sendMessage(player, "You set off the trap as you pass.")
                impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
            } else if (wireTraps.contains(player.location)) {
                sendMessage(player, "You snag the trip wire as you step over it.")
                impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
            }
        }
    }

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
}
