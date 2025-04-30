package content.region.karamja.quest.dillo.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

class TokTzKetDillListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles searching the bookcase in Reldo's library in Varrock.
         */

        on(Scenery.BOOKCASE_24282, IntType.SCENERY, "search") { player, _ ->
            val hasGuide = hasAnItem(player, Items.TZHAAR_TOURIST_GUIDE_13244).container != null

            sendMessage(player, "You search the bookcase...")

            if (!hasGuide) {
                sendMessage(player, "You find the TzHaar Tourist Guide.")
                if (freeSlots(player) == 0) {
                    sendMessage(player, "...but you don't have enough room to take it.")
                } else {
                    addItemOrDrop(player, Items.TZHAAR_TOURIST_GUIDE_13244)
                }
            } else {
                sendMessage(player, "You don't find anything that you'd ever want to read.")
            }

            return@on true
        }

        /*
         * Handles cave entrances in TzHaar City.
         */

        on(Scenery.CAVE_ENTRANCE_31292, IntType.SCENERY, "go-through") { player, node ->
            lock(player, 6)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        openInterface(player, Components.FADE_TO_BLACK_115)
                        return@queueScript delayScript(player, 5)
                    }

                    1 -> {
                        val location = when (player.location.y) {
                            // Enter & Exit from obsidian mine.
                            5180 -> Location.create(2543, 5192, 0)
                            5192 -> Location.create(2527, 5180, 0)
                            else -> when (player.location.x) {
                                // Enter & Exit from library.
                                2437 -> Location.create(2475, 5214, 0)
                                2475 -> Location.create(2437, 5146, 0)
                                else -> null
                            }
                        }
                        location?.let { teleport(player, it) } ?: return@queueScript stopExecuting(player)
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        /*
         * Handles read the stone tablet.
         */

        on(Items.STONE_TABLET_13243, IntType.ITEM, "read") { player, _ ->
            openInterface(player, 738)
            return@on true
        }
    }
}