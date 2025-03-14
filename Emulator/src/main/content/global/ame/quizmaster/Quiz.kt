package content.global.ame.quizmaster

import core.api.MapArea
import core.api.removeTabs
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class Quiz : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(1954, 4763, 1950, 4770))
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            val player = entity.asPlayer()
            QuizMaster.cleanup(player)
        }
    }

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            val player = entity.asPlayer()
            player.lock()
            removeTabs(player, 0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14)
            player.dialogueInterpreter.open(QuizMasterDialogue())
        }
    }
}
