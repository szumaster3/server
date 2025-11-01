package content.region.misthalin.draynor.quest.swept

import content.region.misthalin.draynor.quest.swept.plugin.SweptUtils
import core.api.MapArea
import core.api.getRegionBorders
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

/**
 * Represents the aggie clearing area.
 */
class ClearingArea : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(13126))
    }

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            SweptUtils.resetLines(entity)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            SweptUtils.resetLines(entity)
        }
    }
}
