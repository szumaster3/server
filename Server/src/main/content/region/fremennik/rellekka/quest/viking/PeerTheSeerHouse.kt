package content.region.fremennik.rellekka.quest.viking

import core.api.MapArea
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class PeerTheSeerHouse : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2629, 3659,2638, 3666))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.TELEPORT)
    }
}