package content.region.kandarin.seers.plugin

import core.api.MapArea
import core.api.finishDiaryTask
import core.api.removeAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.Vector3d

/**
 * Handles the logic for the Mysterious Statue area in Seers' Village.
 */
class MysteriousStatue : MapArea {

    /**
     * Represents the central position of the Mysterious Statue.
     */
    private val origin = Vector3d(2740.5, 3490.5, 0.0)

    /**
     * Represents the axis vector for signed angle calculation (Z-axis).
     */
    private val n = Vector3d(0.0, 0.0, 1.0)

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        val player = entity as? Player ?: return

        var start = player.getAttribute<Vector3d>("diary:seers:statue-start")
        var last = player.getAttribute<Vector3d>("diary:seers:statue-last")
        var rotation = player.getAttribute<Double>("diary:seers:statue-rotation") ?: 0.0

        val current = Vector3d(location).sub(origin)

        if (start == null) {
            start = current
            setAttribute(player, "diary:seers:statue-start", start)
            setAttribute(player, "diary:seers:statue-last", current)
            setAttribute(player, "diary:seers:statue-rotation", 0.0)
            player.debug ("${player.username}: Starting statue run at $current")
            return
        }

        if (last != null) {
            val delta = Vector3d.signedAngle(last, current, n)
            rotation += Math.toDegrees(delta)
            setAttribute(player, "diary:seers:statue-rotation", rotation)
        }

        setAttribute(player, "diary:seers:statue-last", current)
        if (rotation >= 360.0 && current.epsilonEquals(start, 0.5)) {
            clearProgress(player)
            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 0, 1)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            clearProgress(entity)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(ZoneBorders(2739, 3489, 2742, 3492))

    /**
     * Clears saved run progress attributes.
     */
    private fun clearProgress(player: Player) {
        removeAttribute(player, "diary:seers:statue-start")
        removeAttribute(player, "diary:seers:statue-last")
        removeAttribute(player, "diary:seers:statue-rotation")
    }
}
