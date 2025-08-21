package core.game.world.map.zone.impl

import content.region.island.tutorial.plugin.TutorialStage
import core.api.getRegionBorders
import core.api.inBorders
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Regions

/**
 * Represents the tutorial zone area.
 */
@Initializable
class TutorialZone : MapZone("tutorial", true), Plugin<Any?> {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun configure() {
        for (regionId in REGIONS) {
            registerRegion(regionId)
        }
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

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }

    companion object {
        /**
         * Represents the tutorial region ids.
         */
        private val REGIONS = intArrayOf(
            Regions.TUTORIAL_ISLAND_12079, Regions.TUTORIAL_ISLAND_12180, Regions.TUTORIAL_ISLAND_12592, Regions.TUTORIAL_ISLAND_12436, Regions.TUTORIAL_ISLAND_12335, Regions.TUTORIAL_ISLAND_12336
        )
    }
}
