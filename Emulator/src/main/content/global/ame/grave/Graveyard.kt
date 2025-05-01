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
        if (entity !is Player) return
        val player = entity.asPlayer()
        GravediggerListener.init(player)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        val scenery0 = getScenery(Location(1924, 4996, 0))
        replaceScenery(scenery0!!.asScenery(), 12721, -1)
        val scenery1 = getScenery(Location(1926, 4999, 0))
        replaceScenery(scenery1!!.asScenery(), 12722, -1)
        val scenery2 = getScenery(Location(1928, 4996, 0))
        replaceScenery(scenery2!!.asScenery(), 12723, -1)
        val scenery3 = getScenery(Location(1930, 4999, 0))
        replaceScenery(scenery3!!.asScenery(), 12724, -1)
        val scenery4 = getScenery(Location(1932, 4996, 0))
        replaceScenery(scenery4!!.asScenery(), 12725, -1)

        if (entity is Player) {
            val player = entity.asPlayer()
            if (anyInInventory(player, *GravediggerListener.COFFIN)) {
                for (itemId in GravediggerListener.COFFIN) {
                    removeAll(player, itemId)
                }
            }
            GravediggerListener.reset(player)
            removeAttributes(player, "random:talk-to")
        }
    }
}