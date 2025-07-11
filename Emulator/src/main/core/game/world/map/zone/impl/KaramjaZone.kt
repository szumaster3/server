package core.game.world.map.zone.impl

import core.api.amountInInventory
import core.api.removeAll
import core.api.sendMessage
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import org.rs.consts.Items

/**
 * Represents the karamja zone area.
 *
 * @author Vexia
 */
class KaramjaZone : MapZone("karamja", true) {

    override fun configure() {
        for (regionId in REGIONS) {
            registerRegion(regionId)
        }
    }

    override fun teleport(entity: Entity, type: Int, node: Node): Boolean {
        if (entity is Player) {
            val p = entity
            val amt = amountInInventory(p, KARAMJAN_RUM)
            if (amt != 0) {
                removeAll(p, KARAMJAN_RUM)
                sendMessage(p, "During the trip you lose your rum to a sailor in a game of dice. Better luck next time!")
            }
        }
        return super.teleport(entity, type, node)
    }

    companion object {
        /**
         * Represents the region ids.
         */
        private val REGIONS = intArrayOf(
            11309,
            11054,
            11566,
            11565,
            11567,
            11568,
            11053,
            11821,
            11055,
            11057,
            11569,
            11822,
            11823,
            11310,
            11311,
            11312,
            11313,
            11314,
            11056,
            11057,
            11058,
            10802,
            10801
        )

        /**
         * Represents the karamjan rum.
         */
        private val KARAMJAN_RUM = Items.KARAMJAN_RUM_431
    }
}
