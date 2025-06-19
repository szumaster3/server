package content.region.desert.quest.smki_miniquest.plugin

import core.api.sendMessages
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class DragonSlayerDungeonPlugin : InteractionListener {

    // You shift back to reality, having defeated this boss. You may now pass this barrier freely.

    override fun defineListeners() {

        /*
         * Handles climb to Pollnivneach slayer dungeon.
         */

        on(Scenery.WELL_31359, IntType.SCENERY, "climb-down") { player, _ ->
            player.teleporter.send(Location.create(3358, 9354, 0))
            sendMessages(player, "You descend into the somewhat smoky depths of the well, to the accompaniment of", "eery wails.")
            return@on true
        }

        /*
         * Handles exit from the dungeon.
         */

        on(Scenery.BUCKET_ROPE_31316, IntType.SCENERY, "climb-up") { player, _ ->
            player.teleporter.send(Location.create(3358, 2970, 0))
            return@on true
        }
    }
}