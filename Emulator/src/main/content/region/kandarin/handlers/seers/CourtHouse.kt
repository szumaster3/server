package content.region.kandarin.handlers.seers

import core.api.MapArea
import core.api.finishDiaryTask
import core.api.hasDiaryTaskComplete
import core.api.isPrayerActive
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.world.map.zone.ZoneBorders

class CourtHouse : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2735, 3471, 2736, 3471))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player && !entity.isArtificial) {
            if (!hasDiaryTaskComplete(entity, DiaryType.SEERS_VILLAGE, 2, 3) &&
                isPrayerActive(
                    entity.asPlayer(),
                    PrayerType.PIETY,
                )
            ) {
                finishDiaryTask(entity, DiaryType.SEERS_VILLAGE, 2, 3)
            }
        }
    }
}
