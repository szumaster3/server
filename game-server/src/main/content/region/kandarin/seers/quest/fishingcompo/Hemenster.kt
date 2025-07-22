package content.region.kandarin.seers.quest.fishingcompo

import content.data.GameAttributes
import core.api.MapArea
import core.api.getAttribute
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class Hemenster : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2625, 3411, 2643, 3448)
    )

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val p = entity.asPlayer()
            if (getAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false)) {
                removeAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST)
            }
        }
    }
}