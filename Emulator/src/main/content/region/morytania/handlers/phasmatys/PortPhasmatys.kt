package content.region.morytania.handlers.phasmatys

import core.api.MapArea
import core.api.inBorders
import core.api.inEquipment
import core.api.registerLogoutListener
import core.game.global.action.EquipHandler
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items

class PortPhasmatys : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(14646), ZoneBorders.forRegion(14747))
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            val player = entity.asPlayer()
            if (!inBorders(player, 3673, 9955, 3685, 9964) || !inBorders(player, 3650, 3456, 3689, 3508)) {
                if (inEquipment(player, Items.BEDSHEET_4285)) {
                    registerLogoutListener(player, "bedsheet-uniform") { p ->
                        EquipHandler.unequip(player, 0, itemId = Items.BEDSHEET_4285)
                        player.logoutListeners.remove("bedsheet-uniform")
                        player.appearance.transformNPC(-1)
                        player.appearance.sync()
                    }
                }
            }
        }
    }
}
