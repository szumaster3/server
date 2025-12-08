package content.region.kandarin.witchaven.plugin

import core.api.teleport
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Scenery

class WitchavenDungeonPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles exit from Witchaven dungeon.
         */

        on(Scenery.EXIT_33246, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(2697, 3283, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }


        /*
         * Handles enter to the Witchaven dungeon.
         */

        on(Scenery.OLD_RUIN_ENTRANCE_18266, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2696, 9683, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

    }
}