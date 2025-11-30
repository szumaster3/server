package content.global.skill.slayer

import core.api.MapArea
import core.api.getRegionBorders
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Regions

class SlayerTower : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(SLAYER_TOWER)
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON)
    }

    companion object {
        private val SLAYER_TOWER = getRegionBorders(Regions.MORYTANIA_SLAYER_TOWER_13623)
    }
}