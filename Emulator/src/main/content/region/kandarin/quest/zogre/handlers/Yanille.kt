package content.region.kandarin.quest.zogre.handlers

import core.api.MapArea
import core.api.getAttribute
import core.api.setVarbit
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Vars

class Yanille : MapArea {
    override fun areaEnter(entity: Entity) {
        if (entity is Player && getAttribute(entity, ZUtils.SITHIK_TURN_INTO_OGRE, false)) {
            setVarbit(entity, Vars.VARBIT_QUEST_SITHIK_OGRE_TRANSFORMATION_495, 1, true)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2598, 3103, 2590, 3108, 1, true))
    }
}
