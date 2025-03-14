package content.region.misthalin.handlers.draynor

import core.api.LogoutListener
import core.api.MapArea
import core.api.removeAttribute
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class DraynorMarket :
    MapArea,
    LogoutListener {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(DraynorUtils.draynorMarket)
    }

    override fun logout(player: Player) {
        if (!defineAreaBorders().any { it.insideBorder(player.location) }) {
            removeAttribute(player, DraynorUtils.feedGuardAttribute)
            return
        }
    }
}
