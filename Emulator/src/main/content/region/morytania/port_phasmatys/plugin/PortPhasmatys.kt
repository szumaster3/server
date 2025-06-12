package content.region.morytania.port_phasmatys.plugin

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
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders.forRegion(14646), ZoneBorders.forRegion(14747))

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            if (!inBorders(entity, 3673, 9955, 3685, 9964) || !inBorders(entity, 3650, 3456, 3689, 3508)) {
                if (inEquipment(entity, Items.BEDSHEET_4285)) {
                    registerLogoutListener(entity, "bedsheet-uniform") {
                        EquipHandler.unequip(entity, 0, itemId = Items.BEDSHEET_4285)
                        entity.logoutListeners.remove("bedsheet-uniform")
                        entity.appearance.transformNPC(-1)
                        entity.appearance.sync()
                    }
                }
            }
        }
    }
}
