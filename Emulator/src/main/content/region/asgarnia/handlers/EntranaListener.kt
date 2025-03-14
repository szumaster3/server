package content.region.asgarnia.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class EntranaListener : InteractionListener {
    override fun defineListeners() {
        on(ENTRANA_BOOKCASE, IntType.SCENERY, "Search") { player, _ ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space.")
                return@on true
            }

            sendMessage(player, "You search the bookcase...")
            if (!inInventory(player, GLASSBLOWING_BOOK)) {
                sendMessageWithDelay(player, "You search the bookcase and find a book named 'Glassblowing Book'.", 1)
                addItem(player, GLASSBLOWING_BOOK)
            } else {
                sendMessageWithDelay(player, "You don't find anything interesting.", 1)
            }
            return@on true
        }

        on(Scenery.LADDER_2408, IntType.SCENERY, "climb-down") { player, _ ->
            openDialogue(player, CAVE_MONK, findNPC(CAVE_MONK)!!.asNpc())
            return@on true
        }

        on(MAGIC_DOOR, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You feel the world around you dissolve...")
            teleport(player, Location(3208, 3764, 0))
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
