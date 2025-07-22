package core.game.world.map.zone.impl

import content.region.island.tutorial.plugin.TutorialStage
import core.api.getRegionBorders
import core.api.inBorders
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
        REGIONS.forEach(::registerRegion)
    }

    override fun teleport(entity: Entity, type: Int, node: Node?): Boolean {
        if (entity is Player) {
            if (entity.rights == Rights.ADMINISTRATOR) {
                return super.teleport(entity, type, node)
            }

            val stage = entity.getAttribute(TutorialStage.TUTORIAL_STAGE, -1)

            // Don't allow teleport if still in tutorial.
            if (stage < 71) return false

            // Block teleport from inside final tutorial area unless it's completed.
            val lastStage = inBorders(entity, getRegionBorders(12592))
            if (lastStage && stage != 72) return false
        }
        return super.teleport(entity, type, node)
    }

    companion object {
        /**
         * Represents the tutorial region ids.
         */
        private val REGIONS = intArrayOf(
            12079, 12180, 12592, 12436, 12335, 12336
        )
    }
}
