package content.region.karamja.tzhaar.plugin

import core.api.sendMessage
import core.api.teleport
import core.game.activity.ActivityManager
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class TzHaarCityPlugin : InteractionListener {

    override fun defineListeners() {
        on(Scenery.CRACK_IN_WALL_9358, IntType.SCENERY, "go-through") { player, _ ->
            sendMessage(player, "Nothing interesting happens.")
            return@on true
        }

        on(Scenery.CAVE_ENTRANCE_9356, IntType.SCENERY, "enter") { player, _ ->
            if (player.familiarManager.hasFamiliar()) {
                sendMessage(player, "You can't enter this with a follower.")
            } else {
                ActivityManager.start(player, "fight caves", false)
            }
            return@on true
        }

        on(Scenery.CAVE_ENTRANCE_31284, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(2480, 5175, 0))
            return@on true
        }

        on(Scenery.CAVE_EXIT_9359, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(2866, 9571, 0))
            return@on true
        }

        on(Scenery.HOT_VENT_DOOR_9369, IntType.SCENERY, "pass") { player, _ ->
            ActivityManager.start(player, "fight pits", false)
            return@on true
        }

        /*
         * Special interaction for obsidian wall.
         */

        on(Scenery.OBSIDIAN_WALL_31230, IntType.SCENERY, "mine") { player, _ ->
            sendMessage(player, "There is no obsidian currently available in this wall.")
            return@on true
        }
    }
}