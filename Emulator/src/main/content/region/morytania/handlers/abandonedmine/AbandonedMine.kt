package content.region.morytania.handlers.abandonedmine

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items
import org.rs.consts.Music
import org.rs.consts.Scenery

class AbandonedMine : MapArea {
    private val FUNGUS = intArrayOf(Scenery.GLOWING_FUNGUS_4932, Scenery.GLOWING_FUNGUS_4933)

    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(
            ZoneBorders.forRegion(10822),
            ZoneBorders.forRegion(11077),
            ZoneBorders.forRegion(11078),
            ZoneBorders.forRegion(11079),
            ZoneBorders.forRegion(10821),
            ZoneBorders.forRegion(10822),
        )

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            val player = entity.asPlayer()

            if (anyInInventory(player, *FUNGUS)) {
                removeAll(player, FUNGUS)
                addItem(player, Items.ASHES_592, 1)
                sendMessage(player, "The fungus you are carrying crumbles to dust.")
            }

            val musicPlayer = player.musicPlayer

            if (!musicPlayer.hasUnlocked(Music.SPOOKY2_11)) {
                musicPlayer.unlock(Music.SPOOKY2_11)
            }

            when (player.location.regionId) {
                11079 -> if (!musicPlayer.hasUnlocked(Music.DEEP_DOWN_278)) {
                    musicPlayer.unlock(Music.DEEP_DOWN_278)
                }
                11078 -> if (!musicPlayer.hasUnlocked(Music.CHAMBER_282)) {
                    musicPlayer.unlock(Music.CHAMBER_282)
                }
            }
        }
    }
}
