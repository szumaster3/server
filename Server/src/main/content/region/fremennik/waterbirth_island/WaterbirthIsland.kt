package content.region.fremennik.waterbirth_island

import core.api.MapArea
import core.api.closeOverlay
import core.api.openOverlay
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import shared.consts.Components

class WaterbirthIsland : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2487, 3711, 2565, 3776, 0, true)
    )

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            val player = entity.asPlayer()
            openOverlay(player, Components.SNOW_OVERLAY_370)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            val player = entity.asPlayer()
            closeOverlay(player)
        }
    }
}
