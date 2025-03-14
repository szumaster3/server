package content.region.fremennik.handlers

import core.api.*
import core.api.MapArea
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import org.rs.consts.Scenery

class WaterbirthIslandListener :
    MapArea,
    InteractionListener {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2487, 3711, 2565, 3776))
    }

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            val player = entity.asPlayer()
            openOverlay(player, Components.SNOW_OVERLAY_370)
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            val player = entity.asPlayer()
            closeOverlay(player)
        }
    }

    override fun defineListeners() {
        on(intArrayOf(Scenery.CAVE_ENTRANCE_8929), IntType.SCENERY, "enter") { player, _ ->
            if (inBorders(player, getRegionBorders(10042))) {
                teleport(player, Location.create(2442, 10147, 0))
            } else {
                sendMessage(player, "You venture into the icy cavern.")
                teleport(player, Location(3056, 9555, 0))
            }
            return@on true
        }

        on(intArrayOf(Scenery.CAVE_ENTRANCE_8930), IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2545, 10143, 0), TeleportManager.TeleportType.INSTANT)
        }
    }
}
