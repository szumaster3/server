package core.game.world.map.zone.impl

import core.api.MapArea
import core.api.getRegionBorders
import core.api.inBorders
import core.api.openOverlay
import core.api.closeOverlay
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import shared.consts.Components

/**
 * Handles the snow zone.
 * @author szu
 */
class SnowZone : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            BASE_ZONE,
            MIDDLE_ZONE,
            WINDSWEPT_AREA,
            TROLLWEIS_MOUNTAIN_0,
            TROLLWEIS_MOUNTAIN_1,
            TROLLWEIS_MOUNTAIN_2
        )
    }

    override fun areaEnter(entity: Entity) {
        if(entity is Player){
            val p = entity
            openSnowOverlay(p)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if(entity is Player){
            val p = entity
            closeOverlay(p)
        }
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            val p = entity
            openSnowOverlay(p)
        }
    }

    companion object {
        /**
         * Represents the zones.
         */
        private val BASE_ZONE = ZoneBorders(2728, 3716, 2732, 3731)
        private val MIDDLE_ZONE = ZoneBorders(2733, 3716, 2738, 3730)
        private val WINDSWEPT_AREA = ZoneBorders(2751, 3740, 2735, 3712)
        private val TROLLWEIS_MOUNTAIN_0 = getRegionBorders(11066)
        private val TROLLWEIS_MOUNTAIN_1 = getRegionBorders(11067)
        private val TROLLWEIS_MOUNTAIN_2 = getRegionBorders(11068)

        /**
         * Open overlay with snow.
         */
        private fun openSnowOverlay(player: Player) {
            val overlayComponent =
                when {
                    inBorders(player, BASE_ZONE) -> Components.BLUE_OVERLAY_483
                    inBorders(player, MIDDLE_ZONE) -> Components.SNOW_A_OVERLAY_482
                    else -> Components.SNOW_B_OVERLAY_481
                }
            openOverlay(player, overlayComponent)
        }
    }
}
