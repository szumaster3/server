package content.region.kandarin.seers_village.plugin

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
 *
 * Relations:
 * - [SeersVillageAchievementDiary]
 */
class MysteriousStatue : MapArea {
    /**
     * Represents the central position of the Mysterious Statue.
     */
    var origin: Vector3d = Vector3d(2740.5, 3490.5, 0.0)

    /**
     * Represents the vector used for calculating angles (upwards in Z-axis).
     */
    var n: Vector3d = Vector3d(0.0, 0.0, 1.0)

    /**
     * Called when an [Entity] takes a step within the statue's map area.
     *
     * @param entity The entity that moved.
     * @param location The entity's current location.
     * @param lastLocation The entity's previous location.
     */
    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        if (entity is Player) {
            val player = entity.asPlayer()

            val statueWalkStart = player.getAttribute<Vector3d>("diary:seers:statue-walk-start")
            val statueWalkA = player.getAttribute<Vector3d>("diary:seers:statue-walk-a")

            if (statueWalkStart != null) {
                val a = statueWalkA ?: Vector3d(0.0, 0.0, 0.0)
                val b = Vector3d(lastLocation).sub(origin)
                val angle = Vector3d.signedAngle(a, b, n) * 360.0 / (2 * Math.PI)

                if (angle >= 0) {
                    removeAttribute(player, "diary:seers:statue-walk-start")
                }

                if (b.epsilonEquals(statueWalkStart, 0.001)) {
                    finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 0, 1)
                    removeAttribute(player, "diary:seers:statue-walk-start")
                }

                setAttribute(player,"diary:seers:statue-walk-a", b)
            } else {
                val start = Vector3d(player.location).sub(origin)
                setAttribute(player, "diary:seers:statue-walk-start", start)
                setAttribute(player,"diary:seers:statue-walk-a", start)
            }
        } else {
            removeAttribute(entity.asPlayer(), "diary:seers:statue-walk-start")
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            removeAttribute(entity.asPlayer(), "diary:seers:statue-walk-start")
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2739, 3489, 2742, 3492))
}
