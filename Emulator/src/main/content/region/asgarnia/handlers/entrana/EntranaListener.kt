package content.region.asgarnia.handlers.entrana

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class EntranaListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles searching the bookcase in the house near Fritz on Entrana.
         */

        on(ENTRANA_BOOKCASE, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the bookcase...")

            if (inInventory(player, GLASSBLOWING_BOOK)) {
                sendMessage(player, "You don't find anything that you'd ever want to read.")
                return@on true
            }

            sendMessage(player, "and find a Glassblowing Book.")

            if (freeSlots(player) == 0) {
                sendMessage(player, "...but you don't have enough room to take it.")
            } else {
                addItem(player, GLASSBLOWING_BOOK)
            }

            return@on true
        }

        /*
         * Handles entering the Entrana dungeon (Lost City quest).
         */

        on(Scenery.LADDER_2408, IntType.SCENERY, "climb-down") { player, _ ->
            openDialogue(player, CAVE_MONK, findNPC(CAVE_MONK)!!.asNpc())
            return@on true
        }

        /*
         * Handles exit from the Entrana dungeon.
         */

        on(MAGIC_DOOR, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You feel the world around you dissolve...")
            teleport(player, Location(3093, 3224, 0), TeleportManager.TeleportType.ENTRANA_MAGIC_DOOR)
            return@on true
        }
    }

    companion object {
        const val ENTRANA_BOOKCASE = Scenery.BOOKCASE_33964
        const val GLASSBLOWING_BOOK = Items.GLASSBLOWING_BOOK_11656
        const val CAVE_MONK = NPCs.CAVE_MONK_656
        const val MAGIC_DOOR = Scenery.MAGIC_DOOR_2407
    }
}