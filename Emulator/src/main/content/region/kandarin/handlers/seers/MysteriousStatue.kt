package content.region.kandarin.handlers.seers

import core.api.MapArea
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.Vector3d

class MysteriousStatue : MapArea {
    var origin: Vector3d = Vector3d(2740.5, 3490.5, 0.0)
    var n: Vector3d = Vector3d(0.0, 0.0, 1.0)

    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        if (entity is Player) {
            val player = entity.asPlayer()

            if (player.getAttribute<Any?>("diary:seers:statue-walk-start") != null) {
                val start = player.getAttribute<Vector3d>("diary:seers:statue-walk-start")
                val a = player.getAttribute<Vector3d>("diary:seers:statue-walk-a")
                val b = Vector3d(lastLocation).sub(origin)

                val angle = Vector3d.signedAngle(a, b, n) * 360.0 / 2 / 3.14159265355

                if (angle >= 0) {
                    player.removeAttribute("diary:seers:statue-walk-start")
                }
                if (b.epsilonEquals(start, .001)) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 0, 1)
                    player.removeAttribute("diary:seers:statue-walk-start")
                }

                player.setAttribute("diary:seers:statue-walk-a", b)
            } else {
                val start = Vector3d(player.location).sub(origin)
                player.setAttribute("diary:seers:statue-walk-start", start)
                player.setAttribute("diary:seers:statue-walk-a", start)
            }
        } else {
            entity.asPlayer().removeAttribute("diary:seers:statue-walk-start")
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            entity.removeAttribute("diary:seers:statue-walk-start")
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2739, 3489, 2742, 3492))
    }
}
