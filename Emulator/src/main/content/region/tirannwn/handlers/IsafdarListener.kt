package content.region.tirannwn.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Scenery

class IsafdarListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CAVE_ENTRANCE_4006, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2314, 9624, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.CAVE_EXIT_4007, IntType.SCENERY, "exit") { player, _ ->
            teleport(player, Location(2312, 3217, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(Scenery.BOOKCASE_8752, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the bookshelves...")
            if (!inInventory(player, Items.PRIFDDINAS_HISTORY_6073)) {
                sendMessageWithDelay(player, "and find a book named 'Prifddinas History'.", 1)
                addItem(player, Items.PRIFDDINAS_HISTORY_6073)
            } else {
                sendMessageWithDelay(player, "You don't find anything interesting.", 1)
            }
            return@on true
        }
    }
}
