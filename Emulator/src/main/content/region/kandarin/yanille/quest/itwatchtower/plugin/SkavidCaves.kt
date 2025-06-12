package content.region.kandarin.yanille.quest.itwatchtower.plugin

import content.data.GameAttributes
import core.api.MapArea
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class SkavidCaves : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2496, 9408, 2559, 9471))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON)
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if(entity is Player) if (entity.settings.runEnergy < 100.0) {
            entity.settings.updateRunEnergy(-100.0)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        removeAttribute(entity.asPlayer(), GameAttributes.WATCHTOWER_DARK_AREA)
    }

}