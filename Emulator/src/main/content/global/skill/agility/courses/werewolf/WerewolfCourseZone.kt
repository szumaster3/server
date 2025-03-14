package content.global.skill.agility.courses.werewolf

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items

class WerewolfCourseZone : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(3510, 9851, 3592, 9920))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            setAttribute(entity, "/save:werewolf-agility-course", false)
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            val p = entity.asPlayer()
            removeAttribute(p, "werewolf-agility-course")
            if (anyInInventory(p, Items.STICK_4179)) {
                removeAll(p, Items.STICK_4179)
                sendMessage(entity, "The werewolf trainer removes your stick as you leave.")
            }
        }
    }
}
