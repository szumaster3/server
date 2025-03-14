package content.region.misc.handlers

import content.region.misc.dialogue.keldagrim.BlastFusionHammerDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class KeldagrimListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles entering through doorway.
         */

        on(DOORWAY_1, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2941, 10179, 0))
            return@on true
        }
        on(DOORWAY_2, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2435, 5535, 0))
            return@on true
        }

        /*
         * Handles changing armguards.
         */

        on(REINALD, IntType.NPC, "change-armguards") { player, _ ->
            openInterface(player, Components.REINALD_SMITHING_EMPORIUM_593)
            return@on true
        }

        /*
         * Handles using Fusion Hammer on Foreman NPC.
         */

        onUseWith(IntType.NPC, FUSION_HAMMER, FOREMAN) { player, _, npc ->
            openDialogue(player, BlastFusionHammerDialogue(), npc)
            return@onUseWith true
        }

        /*
         * Handles entering a cave entrance.
         */

        on(ENTRANCE, IntType.SCENERY, "go-through") { player, node ->
            teleport(
                player,
                if (node.id == Scenery.CAVE_ENTRANCE_5973) {
                    Location(2838, 10125)
                } else {
                    Location(2780, 10161)
                },
                TeleportManager.TeleportType.INSTANT,
                1,
            )
            sendMessage(player, "You're just about able to squeeze through.")
            return@on true
        }

        /*
         * Handles searching a bookcase.
         */

        on(Scenery.BOOKCASE_6091, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.EXPLORERS_NOTES_11677)) {
                sendMessage(player, "You search the books...")
                sendMessageWithDelay(player, "You find nothing of interest to you.", 1)
            } else if (freeSlots(player) < 1) {
                sendMessage(player, "You need at least one free inventory space to take from the shelves.")
            } else {
                sendMessage(player, "You search the bookcase and find a book named 'Explorer's Notes'.")
                addItemOrDrop(player, Items.EXPLORERS_NOTES_11677)
            }
            return@on true
        }

        /*
         * Handles entering the tunnel.
         */

        on(TUNNEL, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location(2730, 3713, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        /*
         * Overrides the destination for InnKeeper NPC.
         */

        setDest(IntType.NPC, intArrayOf(INN_KEEPER), "talk-to") { _, _ ->
            return@setDest Location.create(2843, 10193, 1)
        }
    }

    companion object {
        private val ENTRANCE = intArrayOf(Scenery.CAVE_ENTRANCE_5973, Scenery.ENTRANCE_5998)
        private const val DOORWAY_1 = Scenery.DOORWAY_23286
        private const val DOORWAY_2 = Scenery.DOORWAY_23287
        private const val REINALD = NPCs.REINALD_2194
        private const val FUSION_HAMMER = Items.BLAST_FUSION_HAMMER_14478
        private const val FOREMAN = NPCs.BLAST_FURNACE_FOREMAN_2553
        private const val TUNNEL = Scenery.TUNNEL_5014
        private const val INN_KEEPER = NPCs.INN_KEEPER_2176
    }
}
