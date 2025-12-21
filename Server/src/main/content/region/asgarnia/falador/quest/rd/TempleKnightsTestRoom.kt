package content.region.asgarnia.falador.quest.rd

import core.api.MapArea
import core.api.getRegionBorders
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Regions

class TempleKnightsTestRoom : MapArea {

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val player = entity.asPlayer()
            RDUtils.resetPlayerState(player)
        }
    }

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.CANNON,
            ZoneRestriction.FOLLOWERS,
            ZoneRestriction.TELEPORT
        )

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(Regions.TEMPLE_KNIGHTS_TESTING_ROOM_9805))
}