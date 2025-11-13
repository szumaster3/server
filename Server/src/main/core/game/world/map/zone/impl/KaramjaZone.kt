package core.game.world.map.zone.impl

import core.api.amountInInventory
import core.api.removeAll
import core.api.sendMessage
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import shared.consts.Items
import shared.consts.Regions

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

    override fun teleport(e: Entity, type: Int, node: Node?): Boolean {
        if (e is Player) {
            val p = e
            val amt = amountInInventory(p, KARAMJAN_RUM)
            if (amt != 0) {
                removeAll(p, KARAMJAN_RUM)
                sendMessage(p, "During the trip you lose your rum to a sailor in a game of dice. Better luck next time!")
            }
        }
        return super.teleport(e, type, node)
    }

    companion object {
        /**
         * Represents the region ids.
         */
        private val REGIONS = intArrayOf(
            Regions.KARAMJA_11309,
            Regions.KARAMJA_11054,
            Regions.KARAMJA_11566,
            Regions.KARAMJA_11565,
            Regions.KARAMJA_11567,
            Regions.KARAMJA_11568,
            Regions.KARAMJA_11053,
            Regions.KARAMJA_11821,
            Regions.KARAMJA_11055,
            Regions.KARAMJA_11057,
            Regions.KARAMJA_11569,
            Regions.KARAMJA_11822,
            Regions.KARAMJA_11823,
            Regions.KARAMJA_11310,
            Regions.KARAMJA_11311,
            Regions.KARAMJA_11312,
            Regions.KARAMJA_11313,
            Regions.KARAMJA_11314,
            Regions.KARAMJA_11056,
            Regions.KARAMJA_11057,
            Regions.KARAMJA_11058,
            Regions.KARAMJA_10802,
            Regions.KARAMJA_10801
        )

        /**
         * Represents the karamjan rum.
         */
        private const val KARAMJAN_RUM = Items.KARAMJAN_RUM_431
    }
}
