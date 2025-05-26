package content.global.ame.grave

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class Graveyard : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(1921, 4993, 1934, 5006))
    }

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            GravediggerListener.init(entity)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)

        val graveScenery = listOf(
            Triple(1924, 4996, 12721),
            Triple(1926, 4999, 12722),
            Triple(1928, 4996, 12723),
            Triple(1930, 4999, 12724),
            Triple(1932, 4996, 12725)
        )

        for ((x, y, newId) in graveScenery) {
            val location = Location(x, y, 0)
            getScenery(location)?.asScenery()?.let {
                replaceScenery(it, newId, -1)
            }
        }

        if (entity is Player) {
            val player = entity

            if (anyInInventory(player, *GravediggerListener.COFFIN_IDS)) {
                GravediggerListener.COFFIN_IDS.forEach { removeAll(player, it) }
            }

            GravediggerListener.reset(player)
            removeAttributes(player, "random:talk-to")
        }
    }
}
