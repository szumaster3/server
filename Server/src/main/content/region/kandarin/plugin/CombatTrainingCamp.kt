package content.region.kandarin.plugin

import core.api.MapArea
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class CombatTrainingCamp : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        val cage = ZoneBorders(2521, 3371, 2533, 3377)
        cage.addException(ZoneBorders(2523, 3373, 2533, 3377))

        return arrayOf(
            cage,
            ZoneBorders(2522, 3370, 2533, 3372),
            ZoneBorders(2523, 3369, 2533, 3369)
        )
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            removeAttribute(entity, OgreNPCBehavior.ATTACKED_BY_ATTRIBUTE)
        }
    }
}