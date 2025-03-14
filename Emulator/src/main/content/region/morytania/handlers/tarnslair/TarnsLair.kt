package content.region.morytania.handlers.tarnslair

import content.region.morytania.handlers.tarnslair.traps.FloorTrap
import core.api.*
import core.api.MapArea
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.Sounds
import kotlin.random.Random

class TarnsLair : MapArea {
    companion object {
        val borders = ZoneBorders(3121, 4537, 3204, 4673)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(borders)
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON)
    }

    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        if (entity is Player) {
            val player = entity.asPlayer()
            val trap = FloorTrap.getFromCoords(player.location.x, player.location.y)

            if (trap != null && player.location == trap.location) {
                stopWalk(player)
                animateScenery(Scenery(trap.trap.id, trap.trap.location), 5631)
                queueScript(player, 1, QueueStrength.SOFT) {
                    animate(player, 1441)
                    sendChat(player, "Ouch!")
                    forceMove(
                        player,
                        trap.location,
                        player.location.transform(Direction.getLogicalDirection(player.location, trap.location), 1),
                        0,
                        25,
                        trap.direction,
                        1441,
                    )
                    impact(player, Random.nextInt(0, 5))
                    playAudio(player, Sounds.LOTR_LOGTRAP_3318)
                    return@queueScript stopExecuting(player)
                }
            }
        }
    }
}
