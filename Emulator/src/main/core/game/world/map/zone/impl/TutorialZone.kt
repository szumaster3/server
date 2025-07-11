package core.game.world.map.zone.impl

import content.region.island.tutorial.plugin.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.world.map.zone.MapZone

/**
 * Represents the tutorial zone area.
 */
class TutorialZone : MapZone("tutorial", true) {

    override fun configure() {
        for (regionId in REGIONS) {
            registerRegion(regionId)
        }
    }

    override fun teleport(entity: Entity, type: Int, node: Node?): Boolean {
        if (entity is Player) {
            val a = Rights.ADMINISTRATOR
            if (entity.rights == a) {
                return super.teleport(entity, type, node)
            }
            if (entity.getAttribute(TutorialStage.TUTORIAL_STAGE, -1) < 71) {
                return false
            }
        }
        return super.teleport(entity, type, node)
    }

    companion object {
        /**
         * Represents the tutorial region ids.
         */
        private val REGIONS = intArrayOf(
            12079,
            12180,
            12592,
            12436,
            12335,
            12336
        )
    }
}
