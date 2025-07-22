package content.region.morytania.swamp.plugin

import core.api.getRegionBorders
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneRestriction

class MortMyreSwamp : MapZone("Mort Myre Swamp", true) {

    override fun configure() {
        register(getRegionBorders(13621))
        register(getRegionBorders(13877))
        register(getRegionBorders(13876))
        register(getRegionBorders(13875))
    }

    fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON)


    override fun leave(e: Entity?, logout: Boolean): Boolean {
        if (e is Player) {
            removeAttribute(e, "lastRepelTick")
        }
        return super.leave(e, logout)
    }

    companion object {
        val instance = MortMyreSwamp()
    }
}
