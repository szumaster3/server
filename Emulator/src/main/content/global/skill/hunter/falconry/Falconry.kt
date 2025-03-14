package content.global.skill.hunter.falconry

import core.api.MapArea
import core.api.anyInEquipment
import core.api.anyInInventory
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.Items

class Falconry : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2363, 3574, 2395, 3620))
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)
        if (entity is Player && !entity.isArtificial) {
            val player = entity.asPlayer()
            removeAttribute(player, "falconry")
            if (anyInEquipment(player, Items.FALCONERS_GLOVE_10023, Items.FALCONERS_GLOVE_10024) ||
                anyInInventory(player, Items.FALCONERS_GLOVE_10023, Items.FALCONERS_GLOVE_10024)
            ) {
                FalconryActivityPlugin.removeItems(player)
            }
        }
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS)
    }
}
